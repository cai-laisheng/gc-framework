package com.allen.demoresource.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/messages")
public class UserController {

	@GetMapping("/getUser")
	public UserDetails user(@AuthenticationPrincipal UserDetails sysUser) {
		return sysUser;
	}

	@GetMapping("/getUser2")
	public Object user2(Authentication authentication) {
		Object principal = authentication.getPrincipal();
		System.out.printf("---"+principal);

		return authentication;
	}

	@GetMapping("/getUser1")
	public Authentication user1() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Map<String, Object> attributes;
		if (authentication instanceof JwtAuthenticationToken) {
			attributes = ((JwtAuthenticationToken) authentication).getTokenAttributes();

			System.out.println("getAuthorities:"+authentication.getAuthorities());
			System.out.println("attributes:"+attributes);
			System.out.println("getPrincipal:"+(UserDetails)authentication.getPrincipal());
			System.out.println("getCredentials:"+authentication.getCredentials());
		}
		System.out.println((UserDetails)authentication.getPrincipal());

		return authentication;
	}
}