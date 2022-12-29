package com.allen.demoserver.entity;

import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 自定义用户实体，主要是存储用户信息。全局获取用户
 */
@Data
@FieldNameConstants
public class SysUserDetails implements Serializable, UserDetails{
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
	 * 当前拥有角色集合
	 */
	private List<String> roles;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>(roles.size());
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
}
