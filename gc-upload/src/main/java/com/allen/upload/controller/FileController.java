package com.allen.upload.controller;


import com.allen.upload.base.Result;
import com.allen.upload.service.IFileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file")
public class FileController {

   // @Autowired
    private IFileService fileService;

    @PostMapping("getImageUrl")
    public Result getImageUrl(String fileId) throws Exception {
        String imageUrl = fileService.getImageUrl(fileId);
        return Result.ok(imageUrl).setMsg("获取图片文件URL成功");
    }

}
