package com.allen.demoserver.service.impl;

import com.allen.demoserver.entity.SysUserDetails;
import com.allen.demoserver.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author xuguocai
 * @date 2022/11/24 17:06
 **/
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {


	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * 基于用户名获取数据库中的用户信息
	 * @param username 这个username来自客户端
	 * @return
	 * @throws UsernameNotFoundException
	 */
	@Override
	public UserDetails loadUserByUsername(String username)
		throws UsernameNotFoundException {
		log.info("loadUserByUsername   {}",username);
		//1.基于用户名获取用户信息(远程接口) todo


		//2.基于用于id查询用户权限  账号 admin 123456
		String password = passwordEncoder.encode("123456");
//		log.info("数据:{}",data);

		SysUser details = new SysUser();
		details.setUsername("admin");
		details.setNickName("xuguocai");
		details.setPassword(password);
		List<String> roles = new ArrayList<>();
		roles.add("admin");
		roles.add("user");
		details.setRoles(roles);

//		Set<GrantedAuthority> authorities = new HashSet<>(roles.size());
//		for (String role : roles) {
//			authorities.add(new SimpleGrantedAuthority(role));
//		}
//
//		details.setAuthorities(authorities);

		//3.对查询结果进行封装并返回
//		return new User("admin", password,
//			AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN"));
		//返回给认证中心,认证中心会基于用户输入的密码以及数据库的密码做一个比对
		log.info("用户信息：",details);
		// 自定义用户
		return new CustomUserDetails(details);
	}

	static final class CustomUserDetails extends SysUser implements UserDetails {

		private static final List<GrantedAuthority> ROLE_USER = Collections
				.unmodifiableList(AuthorityUtils.createAuthorityList("ROLE_USER"));

		CustomUserDetails(SysUser sysUser) {
			super(sysUser.getId(), sysUser.getUsername(), sysUser.getNickName(),sysUser.getEmail(),
					sysUser.getPhone(),sysUser.getDepartmentId(),sysUser.getDepartmentName(),
					sysUser.getPassword(),sysUser.getUserGender(),sysUser.getRoles());
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return ROLE_USER;
		}

		@Override
		public String getUsername() {
			return getName();
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

		@Override
		public boolean isEnabled() {
			return true;
		}

	}


}
