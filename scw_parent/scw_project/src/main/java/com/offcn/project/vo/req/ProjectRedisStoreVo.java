package com.offcn.project.vo.req;

import com.offcn.project.pojo.TReturn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//在reidis中存储的数据
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRedisStoreVo {

    //1.项目的临时令牌  （作为redis中获取数据的key）
    private String projectToken;
    //2.project表中的数据
    private String name;//项目名称
    private String remark;//项目简介
    private Long money;//筹措的金额
    private Integer day;//天数
    private Integer memberid;//会员id
    private String createdate;//创建的时间

    //头部图片一张
    private String headerImage;
    //详情图片  多张
    private List<String> datailsImage;
    //标签 多个
    private List<Integer> tagIds;
    //类型 多个
    private List<Integer> typeIds;
    //3. 回报数据
    private List<TReturn> projectReturns;

}
