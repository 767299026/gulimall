package com.lsl.gulimall.file.utils;

import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "qiniu")
public class QiniuUtil {

    // 设置好账号的ACCESS_KEY和SECRET_KEY
    private String accessKey;
    private String secretKey;

    // 要上传的空间（创建空间的名称）
    private String bucketName;
    // 域名
    private String domainName;

    // 密钥配置
    Auth auth = Auth.create(accessKey, secretKey);

    /**
     * 处理多文件
     * @param multipartFiles
     * @return
     */
    public Map<String, List<String>> uploadImages(MultipartFile[] multipartFiles){
        Map<String,List<String>> map = new HashMap<>();
        List<String> imageUrls = new ArrayList<>();
        for(MultipartFile file : multipartFiles){
            imageUrls.add(uploadImageQiniu(file));
        }
        map.put("imageUrl",imageUrls);
        return map;
    }

    /**
     * 上传图片到七牛云
     * @param multipartFile
     * @return
     */
    private String uploadImageQiniu(MultipartFile multipartFile){
        try {
//1、获取文件上传的流
            byte[] fileBytes = multipartFile.getBytes();

//3、获取文件名
            String originalFilename = multipartFile.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = "gulimall"+"/"+ UUID.randomUUID().toString().replace("-", "")+ suffix;

//4.构造一个带指定 Region 对象的配置类
//Region.huabei(根据自己的对象空间的地址选
            Configuration cfg = new Configuration(Region.huanan());
            UploadManager uploadManager = new UploadManager(cfg);

            //5.获取七牛云提供的 token
            String upToken = auth.uploadToken(bucketName);
            uploadManager.put(fileBytes,filename,upToken);

            return domainName+filename;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
