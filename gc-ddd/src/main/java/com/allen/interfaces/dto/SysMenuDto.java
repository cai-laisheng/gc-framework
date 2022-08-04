package com.allen.interfaces.dto;

import lombok.Data;

/**
 * @author xuguocai
 * @date 2022/8/4 15:48
 */
@Data
public class SysMenuDto {
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

}
