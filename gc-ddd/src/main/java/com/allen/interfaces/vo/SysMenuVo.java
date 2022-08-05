package com.allen.interfaces.vo;

import lombok.Data;
import java.util.Date;

@Data
public class SysMenuVo {

    /**
     * 编号
     */
    private Integer id;

    /**
     * 父ID
     */
    private Integer parentId;

    /**
     * 树ID
     */
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
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除标记
        1：删除
        0：未删除
     */
    private boolean delFlag;

}