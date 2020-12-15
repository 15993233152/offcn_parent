package com.offcn.user.excpetion;

//运行时异常 | 编译异常

import com.offcn.user.enums.UserExceptionEnum;

public class UserException extends RuntimeException{
    public UserException(UserExceptionEnum userEnum){
        super(userEnum.getMsg());
    }
}
