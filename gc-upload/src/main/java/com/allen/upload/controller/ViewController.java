package com.allen.upload.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @program: Allen-projects
 * @description: OutController
 *
 * 外部路径配置  E:/Practices/test
 *
 *  适用于图片显示，不分文件层次存放的文件
 *
 * 访问方式：IP+端口/文件名
 * http://localhost:8002/1443749371675.jpeg
 *
 * @author: allen小哥
 * @Date: 2020-01-04 10:38
 **/
@RestController
@RequestMapping("/view")
@Slf4j
public class ViewController {

    @Value("${web.upload-path}")
    private String uploadUrl;

    @PostMapping("upload")
    public String uploadToProject(MultipartFile file , HttpServletRequest request, HttpServletResponse response) throws Exception {

        log.info("上传的首路径:{}",uploadUrl);

        File targetFile2 = new File(uploadUrl+"test");
        if (!targetFile2.exists()&& !targetFile2.isDirectory()){
            targetFile2.mkdir();
        }
        File targetFile = new File(targetFile2,file.getOriginalFilename());

        log.info("targetFile=="+targetFile);
        //上传到外部目录下
        file.transferTo(targetFile);
        // 绝对路径显示  images/file.getOriginalFilename()
        return "http://localhost:8002/image/test/"+file.getOriginalFilename();
    }


}
