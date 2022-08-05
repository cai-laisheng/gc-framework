package com.allen.infrastructure.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "sys_role_menu")
public class SysRoleMenuPo {
    /**
     * 角色编号
     */
    @Id
    @Column(name = "role_id")
    private String roleId;

    /**
     * 菜单编号
     */
    @Id
    @Column(name = "menu_id")
    private String menuId;

}