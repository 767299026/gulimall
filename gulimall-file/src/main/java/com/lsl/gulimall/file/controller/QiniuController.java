package com.lsl.gulimall.file.controller;

import com.lsl.common.utils.R;
import com.lsl.gulimall.file.utils.QiniuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class QiniuController {

    @Autowired
    private QiniuUtil qiniuUtil;

    @PostMapping("/image/upload")
    public R uploadImage(@RequestParam(value = "file",required = false) MultipartFile[] multipartFile){
        if(ObjectUtils.isEmpty(multipartFile)){
            return R.error("请选择图片");
        }
        Map<String, List<String>> uploadImagesUrl = qiniuUtil.uploadImages(multipartFile);
        return R.ok().put( "url", uploadImagesUrl);
    }

}
