package com.allen.demoserver.enums;

/**
 * @Author xuguocai
 * @Date 11:07 2023/1/16 自定义授权类型
 **/
public interface SmarketingGrant {

	/**
	 * 账号密码
	 */
	String PASSWORD = "password";

	/**
	 * 手机号
	 */
	String PHONE = "phone";
	/**
	 * 验证码
	 */
	String CAPTCHA = "captcha";

	String USERNAME = "username";

	String REFRESH_TOKEN = "refresh_token";


}
