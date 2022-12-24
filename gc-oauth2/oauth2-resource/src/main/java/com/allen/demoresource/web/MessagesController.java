/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.allen.demoresource.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Joe Grandja
 * @since 0.0.1
 */
@RestController
@RequestMapping("/messages")
public class MessagesController {

	/**
	 * 不能进入 配置：.hasAnyRole("ROLE_USER")
	 * @return
	 */
	@PreAuthorize("hasAuthority('SCOPE_message.read')")
	@GetMapping("/getMessages")
	public String[] getMessages() {
		return new String[] {"Message 1", "Message 2", "Message 3"};
	}

	/**
	 *不能进入 配置：.hasAnyRole("ROLE_USER")
	 * @return
	 */
	@PreAuthorize("hasAuthority('message.read')")
	@GetMapping("/getMessages1")
	public String[] getMessages1() {
		return new String[] {"Message 1", "Message 2", "Message 3"};
	}

	/**
	 *不能进入 配置：.hasAnyRole("ROLE_USER")
	 * @return
	 */
	@GetMapping("/getMessages2")
	public String[] getMessages2() {
		return new String[] {"Message 1", "Message 2", "Message 3"};
	}

	/**
	 *不能进入 配置：.hasAnyRole("ROLE_USER")
	 * @return
	 */
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/getMessages3")
	public String[] getMessages3() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		System.out.println(authentication.getName());
		System.out.println(authentication.getDetails());
		return new String[] {"Message 1", "Message 2", "Message 3"};
	}

	/**
	 *不能进入 配置：.hasAnyRole("ROLE_USER")
	 * @return
	 */
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/getMessages4")
	public String[] getMessages4() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		System.out.println(authentication.getName());
		System.out.println(authentication.getDetails());
		return new String[] {"Message 1", "Message 2", "Message 3"};
	}

	/**
	 *
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/getMessages5")
	public String[] getMessages5(Authentication authentication) {

		System.out.println(authentication.getName());
		System.out.println(authentication.getDetails());
		return new String[] {"Message 1", "Message 2", "Message 3"};
	}
}
