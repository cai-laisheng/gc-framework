package com.allen.demoserver.controller;

import com.allen.demoserver.entity.SysUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

	@GetMapping("/getUser")
	public SysUser user(@AuthenticationPrincipal SysUser sysUser) {
		return sysUser;
	}

}