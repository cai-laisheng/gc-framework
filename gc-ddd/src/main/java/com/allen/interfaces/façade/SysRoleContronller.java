package com.allen.interfaces.façade;

import com.allen.application.service.SysRoleAppService;
import com.allen.interfaces.commons.ResponseBean;
import com.allen.interfaces.commons.ResponseBeanUtil;
import com.allen.interfaces.commons.ResponseCodeEnum;
import com.allen.interfaces.dto.SysRoleFormDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author: allen小哥 2020-04-14 11:14
 **/
@RestController
@CrossOrigin
@RequestMapping("/admin/auth")
public class SysRoleContronller {

    @Autowired
    SysRoleAppService sysRoleAppService;

    /**
     * All list.
     *
     * @return the list
     */
    @GetMapping(value = "/role/all")
    public ResponseBean all() {

        return ResponseBeanUtil.ok();
    }

    /**
     * Gets role.
     *
     * @param roleId the role id
     * @return the role
     */
    @GetMapping(value = "/role/view")
    public ResponseBean getRole(Integer roleId) {

        return ResponseBeanUtil.ok();
    }

    /**
     * Save role sys role.
     *
     * @param formDto the role
     * @return the sys role
     */
    @PostMapping(value = "/role/edit")
    public ResponseBean saveRole(@Valid @RequestBody SysRoleFormDto formDto) {
        sysRoleAppService.saveRole(formDto);
        return ResponseBeanUtil.ok();
    }

    /**
     * Delete response entity.
     *
     * @param roleId the role id
     * @return the response entity
     */
    @GetMapping(value = "/role/delete")
    public ResponseBean delete(Integer roleId) {

        return ResponseBeanUtil.ok("删除成功");
    }

    /**
     * delete user_role by userId and roleId
     * @param roleId
     * @param userId
     * @return
     */
    @GetMapping(value = "/role/deleteUserRole")
    public ResponseBean deleteUserRole(Integer roleId,Integer userId){
        if (roleId == null || userId == null){
            return ResponseBeanUtil.fail(ResponseCodeEnum.FAIL);
        }

        return ResponseBeanUtil.ok();
    }

    @PostMapping(value = "/role/submitUserRole")
    public ResponseBean submitUserRole(){

        return ResponseBeanUtil.ok();
    }
}
