package com.allen.demoserver.entity;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@FieldNameConstants
public class SysUser implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;

	private String userName;

	private String nickName;

	private String email;

	private String phone;

	private String departmentId;

	private String departmentName;

	private boolean enabled;

	public String password;
	/**
	 * 性别:1:男 2:女 3:未知
	 */
	private Integer userGender;
	/**
	 * 生效开始日期
	 */
	private LocalDateTime effectiveStartDate;
	/**
	 * 生效截至日期
	 */
	private LocalDateTime effectiveEndDate;
	/**
	 * 当前拥有角色集合
	 */
	private Set<String> roles;
}
