package com.offcn.common.test;

import com.offcn.common.utils.HttpUtils;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

public class SmsTest {

    public static void main(String[] args) throws Exception {
        String host = "http://dingxin.market.alicloudapi.com";
        String path = "/dx/sendSms";
        String method = "POST";
        String AppCode = "APPCODE 1c59a131c267402d8e4337664e139d98";

        //请求头
        Map<String, String> header = new HashMap<>();
        header.put("Authorization",AppCode);
        header.put("Content-Type","application/x-www-form-urlencoded");

        //请求参数
        Map<String, String> query = new HashMap<>();
        query.put("mobile","15993233152");
        query.put("param","code:123456");
        query.put("tpl_id","TP1711063");
        HttpResponse httpResponse = HttpUtils.doPost(host, path, method, header, query, "");
        System.out.println(httpResponse.toString());
    }

}
