package com.allen.demoserver.entity;

import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@FieldNameConstants
public class SysUserDetails implements Serializable, UserDetails {
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
	private List<String> roles;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<UserGrantedAuthority> authorities = new ArrayList<>(roles.size());
		for (String role : roles) {
			authorities.add(new UserGrantedAuthority(role));
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
