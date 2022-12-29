package com.allen.demoserver.service.impl;

import com.allen.demoserver.entity.SysUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xuguocai
 * @date 2022/11/24 17:06
 **/
@Slf4j
//@Service
public class UserDetailsServiceImpl implements UserDetailsService {

//	@Autowired
//	private UserClient userClient;
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
		//基于feign方式获取远程数据并封装
		//1.基于用户名获取用户信息
//		UserVO data = userClient.selectUserByUserName(username);
//		if(data==null) {
//			throw new UsernameNotFoundException("用户不存在");
//		}

		//2.基于用于id查询用户权限  账号 admin 123456
		String password = passwordEncoder.encode("123456");
//		log.info("数据:{}",data);

		SysUserDetails details = new SysUserDetails();
		details.setUserName("admin");
		details.setPassword(password);
		List<String> roles = new ArrayList<>();
		roles.add("admin");
		roles.add("user");
		details.setRoles(roles);

		//3.对查询结果进行封装并返回
		return new User("admin", password,
			AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN"));
		//返回给认证中心,认证中心会基于用户输入的密码以及数据库的密码做一个比对

//		return details;
	}

}
