package com.offcn.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

//格式化输出时间日期
public class AppDateUtiles {

    public static String getFormartTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }
    public static String getFormartTime(String pattern){
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(new Date());
    }
    public static String getFormartTime(Date date,String pattern){
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

}
