package com.offcn.common.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OSSTemplate {

   private String endpoint; //"http://oss-cn-hangzhou.aliyuncs.com";
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    private String accessKeyId;//= "<yourAccessKeyId>";
    private String accessKeySecret;//"<yourAccessKeySecret>";
    private String bucketName;
    private String bucketDomain;

    /**
     * 上传文件
     * @param inputStream 文件流
     * @param fileName 文件名
     * @return 文件url路径
     */
    public String upload(InputStream inputStream,String fileName){
        // 由于未来项目中文件比较多管理不方便，因此每天创建一个目录，存放当天的文件
        SimpleDateFormat dateFormer = new SimpleDateFormat("yyyy-MM-dd");
        String dirName = dateFormer.format(new Date());
        //避免文件名重复，需要在文件名前加前缀UUID
        fileName = UUID.randomUUID().toString().replace("-","")+"_"+fileName;
        //OSS 的客户端
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传文件流。
        ossClient.putObject(bucketName, "pic/"+dirName+"/"+fileName, inputStream);
        //上传之后文件的路径
        String url = bucketDomain+"/pic/"+dirName+"/"+fileName;
        //关闭流
        ossClient.shutdown();
        System.out.println("文件存储的路径"+url);
        return url;
    }
}
