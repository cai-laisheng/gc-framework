package com.allen.upload.controller;

import com.allen.upload.base.Result;
import com.allen.upload.entity.SysAttachment;
import com.allen.upload.service.ISysAttachmentService;
import com.allen.upload.vo.SysAttachmentVo;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/attachment")
public class SysAttachmentController {

    //@Autowired
    private ISysAttachmentService sysAttachmentService;

    @PostMapping("updateAttachment")
    public Result updateAttachment(String fileIds , String businessId , String businessType) throws Exception {
        sysAttachmentService.updateAttachment(fileIds , businessId , businessType);
        return Result.ok("更新成功");
    }

    @PostMapping("getAttachmentList")
    public Result<List<SysAttachmentVo>> getAttachmentList(String businessId , String businessType){
        List<SysAttachmentVo> attachmentList = sysAttachmentService.getAttachmentList(businessId, businessType);
        return Result.ok(attachmentList).setMsg("查询文件列表成功");
    }
    @PostMapping("getMultiAttachmentList")
    public Result<List<SysAttachmentVo>> getMultiAttachmentList(@RequestParam("businessIds") List<String> businessIds) {
        List<SysAttachmentVo> attachmentList = sysAttachmentService.getMultiAttachmentList(businessIds);
        return Result.ok(attachmentList).setMsg("查询文件列表成功");
    }

    @PostMapping("getMultiAttachmentListByFileIds")
    public Result<List<SysAttachmentVo>> getMultiAttachmentListByFileIds(@RequestParam("ids") List<String> ids) {
        List<SysAttachmentVo> attachmentList = sysAttachmentService.getMultiAttachmentListByFileIds(ids);
        return Result.ok(attachmentList).setMsg("查询文件列表成功");
    }

    @PostMapping("deleteAttach")
    public Result<String> deleteAttach(String attachIds){
        if (StringUtils.isEmpty(attachIds)){
            return Result.error("ID 不能为空");
        }
        List<String> attachArr = Arrays.asList(attachIds.split(","));
        for (String itemId : attachArr){
            sysAttachmentService.removeById(itemId);
        }
        return Result.ok();
    }

    public Result<Object> saveAttach(String attachId, String businessId , String type) {
        if (StringUtils.isEmpty(attachId) || StringUtils.isEmpty(businessId)){
            return Result.error("附件ID或业务ID不能为空");
        }
        SysAttachment attachment = new SysAttachment();
        attachment.setId(attachId);
        attachment.setBusinessId(businessId);
        attachment.setBusinessType(type);
        sysAttachmentService.updateById(attachment);
        return Result.ok("保存成功");
    }

}
