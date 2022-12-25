package com.allen.upload.controller;

import com.allen.upload.base.Result;
import com.allen.upload.service.IUploadDownloadService;
import com.allen.upload.vo.UploadSuccessFileVo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/upload")
public class UploadDownloadController {

    //@Autowired
    IUploadDownloadService uploadDownloadService;

    @PostMapping("multiUpload")
    public Result<List<UploadSuccessFileVo>> multiUpload(@RequestParam(required = false , value = "files") MultipartFile[] files, String relativeDir) throws Exception {
        List<UploadSuccessFileVo> uploadSuccessFileVoList = uploadDownloadService.multiUpload(files, relativeDir);
        return Result.ok(uploadSuccessFileVoList).setMsg("上传成功");
    }

    @PostMapping("upload")
    public Result<UploadSuccessFileVo> upload(MultipartFile file, String relativeDir) throws Exception {
        UploadSuccessFileVo uploadSuccessFileVo = uploadDownloadService.upload(file, relativeDir);
        return Result.ok(uploadSuccessFileVo).setMsg("上传成功");
    }

    @GetMapping("download")
    public Result download(String fileId , HttpServletRequest request, HttpServletResponse response) throws Exception {
        uploadDownloadService.download(fileId , request , response);
        return Result.ok("下载成功");
    }


}
