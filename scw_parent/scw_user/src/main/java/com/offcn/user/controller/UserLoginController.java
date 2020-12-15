package com.offcn.user.controller;

import com.offcn.common.response.AppResponse;
import com.offcn.user.pojo.TMember;
import com.offcn.user.pojo.TMemberAddress;
import com.offcn.user.service.UserService;
import com.offcn.user.utils.SmsTemplate;
import com.offcn.user.vo.UserResisVo;
import com.offcn.user.vo.response.UserResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@Api(tags = "用户登录和注册模块")
public class UserLoginController {

    @Autowired
    private SmsTemplate smsTemplate;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserService userService;
    @ApiOperation(value = "获取用户验证码信息")
    @PostMapping("/sendSms")
    public AppResponse<Object> sendSms(String phoneNum){
        //1.生成验证码
        String code = UUID.randomUUID().toString().substring(0, 4);
        System.out.println("当前验证码：" + code);
        //2.将验证码存储在redis中  5分钟有效
        redisTemplate.opsForValue().set(phoneNum,code,5,TimeUnit.MINUTES);
        //3.短信发送

        try {
            String okMsg = "ok";//smsTemplate.sendCode(phoneNum, code);
            return AppResponse.ok(okMsg);
        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.fail("短信发送失败");
        }

    }
    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public AppResponse<UserResponseVo> login(String loginacct, String password){
        //使用service完成登录
        TMember member = userService.login(loginacct, password);
        if (member == null) {//登录没有成功
            AppResponse<UserResponseVo> fail = AppResponse.fail(null);
            fail.setMsg("用户名或密码错误");
            return fail;
        }
        //登录成功
        //通过UUID 为登录之后的用户创建令牌
        String token = UUID.randomUUID().toString().replace("-", "");
        UserResponseVo userResponseVo = new UserResponseVo();
        userResponseVo.setAccessToken(token);
        //将service返回的 Tmember对象中的属性值 赋值给 当前userResponsvo
        BeanUtils.copyProperties(member,userResponseVo);
        //将数据存储到redis中
        redisTemplate.opsForValue().set(token,member.getId()+"",2,TimeUnit.HOURS);
        //返回结果
        return AppResponse.ok(userResponseVo);

    }


    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    public AppResponse<Object> register(UserResisVo userResisVo){
        //1.获取验证码
        //redis中存储的验证码
        String code = redisTemplate.opsForValue().get(userResisVo.getLoginacct());
        if (code != null && code.length() > 0){
            boolean flag = code.equalsIgnoreCase(userResisVo.getCode());//用户自己填入的验证码
            if (flag){
                //完成注册
                TMember tMember = new TMember();
//                tMember.setLoginacct(userResisVo.getLoginacct());
//                tMember.setUserpswd(userResisVo.getUserpswd());
//                tMember.setEmail(userResisVo.getEmail());
                //该方法要求属性名必须一致，否则不一样的属性赋值为空
                BeanUtils.copyProperties(userResisVo,tMember);
                userService.registerUser(tMember);
                //删除验证码
                redisTemplate.delete(tMember.getLoginacct());
                return AppResponse.ok("注册成功");
            }else {
                //用户输入的验证码和存储的验证码不一致
                return AppResponse.fail("验证码错误");
            }
        }else {
            return AppResponse.fail("当前验证码失效");
        }

    }
    @ApiOperation(value = "通过id获取用户信息")
    @GetMapping("/findUserId/{id}")
    public AppResponse<UserResponseVo> findById(@PathVariable("id") Integer id){
        TMember tMember = userService.findById(id);
        UserResponseVo userResponseVo = new UserResponseVo();
        BeanUtils.copyProperties(tMember,userResponseVo);
        return AppResponse.ok(userResponseVo);
    }

    @ApiOperation(value = "通过id获取用户收货地址")
    @GetMapping("/findAddressList")
    public AppResponse<List<TMemberAddress>> findAddressList(String accessToken){
        //根据用户令牌获取用户id
        String memberId = redisTemplate.opsForValue().get(accessToken);
        if (memberId == null || memberId.length() == 0){
            AppResponse response = AppResponse.fail(null);
            response.setMsg("当前用户非法登录");
            return response;
        }
        //查询
        List<TMemberAddress> address = userService.findAddressByMemberId(Integer.parseInt(memberId));
        return AppResponse.ok(address);

    }
}
