package com.allen.demoserver.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;

/**
 * 自定义用户实体，主要是存储用户信息。全局获取用户
 */
@Data
@FieldNameConstants
public class SysUser implements Serializable{
	private static final long serialVersionUID = 1L;

	private String id;

	private String username;

	private String nickName;

	private String email;

	private String phone;

	private String departmentId;

	private String departmentName;

	public String password;
	/**
	 * 性别:1:男 2:女 3:未知
	 */
	private Integer userGender;

	/**
	 * 当前拥有角色集合
	 */
	private List<String> roles;

	@JsonCreator
	public SysUser(String id, String username, String nickName, String email, String phone,
				   String departmentId, String departmentName, String password, Integer userGender,
				   List<String> roles) {
		this.id = id;
		this.username = username;
		this.nickName = nickName;
		this.email = email;
		this.phone = phone;
		this.departmentId = departmentId;
		this.departmentName = departmentName;
		this.password = password;
		this.userGender = userGender;
		this.roles = roles;
	}

	public SysUser() {

	}

	public String getName(){
		return this.username;
	}

}
