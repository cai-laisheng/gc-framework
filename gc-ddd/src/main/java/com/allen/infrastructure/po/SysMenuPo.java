package com.allen.infrastructure.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Table(name = "sys_menu")
public class SysMenuPo {

    /**
     * 编号
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 父ID
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 树ID
     */
    @Column(name = "parent_ids")
    private String parentIds;

    /**
     * 菜单名称
     */
    private String text;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 链接
     */
    private String url;

    /**
     * 页面打开方式
     */
    @Column(name = "target_type")
    private String targetType;

    /**
     * 图标
     */
    private String icon;

    /**
     * 是否显示
        1：显示
        0：隐藏
     */
    @Column(name = "is_show")
    private boolean isShow;

    /**
     * 权限标识
     */
    private String permission;

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