package com.allen.interfaces.dto;

import lombok.Data;

@Data
public class SysRoleFormDto {

	private Integer id;

	/**
     * 名称
     */
    private String name;
    
    /**
     * 角色代码
     */
    private String code;
    /**
     * 是否可用
     */
    private boolean enabled;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 菜单ID
     */
    private String menuIds;
}
