package com.allen.demoserver.entity;

import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

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

	private Set<GrantedAuthority> authorities;

	public SysUserDetails(){}

	public SysUserDetails( String userName, String nickName,String password,List<String> roles,
						  Collection<? extends GrantedAuthority> authorities){
		this.userName = userName;
		this.nickName = nickName;
		this.password = password;
		this.roles = roles;
		this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
	}

	public SysUserDetails(String id, String userName, String nickName, String email, String phone,
						  String departmentId, String departmentName, boolean enabled, String password,
						  Integer userGender, List<String> roles, Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.userName = userName;
		this.nickName = nickName;
		this.email = email;
		this.phone = phone;
		this.departmentId = departmentId;
		this.departmentName = departmentName;
		this.enabled = enabled;
		this.password = password;
		this.userGender = userGender;
		this.roles = roles;
		this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
//		List<GrantedAuthority> authorities = new ArrayList<>(roles.size());
//		for (String role : roles) {
//			authorities.add(new SimpleGrantedAuthority(role));
//		}
		return this.authorities;
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

	public boolean isEnabled(){
		return true;
	}

	private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
		Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
		// Ensure array iteration order is predictable (as per
		// UserDetails.getAuthorities() contract and SEC-717)
		SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(new AuthorityComparator());
		for (GrantedAuthority grantedAuthority : authorities) {
			Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
			sortedAuthorities.add(grantedAuthority);
		}
		return sortedAuthorities;
	}

	private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {

		private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

		@Override
		public int compare(GrantedAuthority g1, GrantedAuthority g2) {
			// Neither should ever be null as each entry is checked before adding it to
			// the set. If the authority is null, it is a custom authority and should
			// precede others.
			if (g2.getAuthority() == null) {
				return -1;
			}
			if (g1.getAuthority() == null) {
				return 1;
			}
			return g1.getAuthority().compareTo(g2.getAuthority());
		}

	}

}
