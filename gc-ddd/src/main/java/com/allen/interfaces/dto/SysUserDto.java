package com.allen.interfaces.dto;

import lombok.Data;

@Data
public class SysUserDto {
    /**
     * 编号
     */
    private Integer id;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 密码
     */
    private String password;

    /**
     * 是否是管理员 0:不是  1是
     */
    private boolean adminFlag;

    /**
     * 姓名
     */
    private String name;

    /**
     * 邮件
     */
    private String email;

    /**
     * 电话
     */
    private String phone;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 是否可用
        1：可用
        0：停用
     */
    private boolean enabled;

    /**
     * 备注
     */
    private String remarks;

}