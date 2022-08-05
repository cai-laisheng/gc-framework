package com.allen.infrastructure.po;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "sys_user")
public class SysUserPo {
    /**
     * 编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 登录名
     */
    @Column(name = "login_name")
    private String loginName;

    /**
     * 密码
     */
    private String password;

    /**
     * 是否是管理员 0:不是  1是
     */
    @Column(name = "admin_flag")
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

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 删除标记
        1：删除
        0：未删除
     */
    @Column(name = "del_flag")
    private boolean delFlag;

}