package com.allen.demoresource.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
public class UserController {

	@GetMapping("/getUser")
	public UserDetails user(@AuthenticationPrincipal UserDetails sysUser) {
		return sysUser;
	}

	@GetMapping("/getUser1")
	public Object user1(Authentication authentication) {
		Object principal = authentication.getPrincipal();
		System.out.printf("---"+principal);
		return authentication;
	}

}