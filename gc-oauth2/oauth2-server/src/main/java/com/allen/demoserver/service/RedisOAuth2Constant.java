package com.allen.demoserver.service;

/**
 * @Author xuguocai
 * @Date 11:12 2022/12/20  redis 常量,来源oauth2 的 OAuth2ParameterNames
 **/
public interface RedisOAuth2Constant {
	String GRANT_TYPE = "grant_type";
	String RESPONSE_TYPE = "response_type";
	String CLIENT_ID = "client_id";
	String CLIENT_SECRET = "client_secret";
	String CLIENT_ASSERTION_TYPE = "client_assertion_type";
	String CLIENT_ASSERTION = "client_assertion";
	String ASSERTION = "assertion";
	String REDIRECT_URI = "redirect_uri";
	String SCOPE = "scope";
	String STATE = "state";
	String CODE = "code";
	String ACCESS_TOKEN = "access_token";
	String TOKEN_TYPE = "token_type";
	String EXPIRES_IN = "expires_in";
	String REFRESH_TOKEN = "refresh_token";
	String USERNAME = "username";
	String PASSWORD = "password";
	String ERROR = "error";
	String ERROR_DESCRIPTION = "error_description";
	String ERROR_URI = "error_uri";
	String REGISTRATION_ID = "registration_id";
	String TOKEN = "token";
	String TOKEN_TYPE_HINT = "token_type_hint";

	String SSO_LOGOUT = "/oauth2/logout";

	String JSESSIONID = "JSESSIONID";

	// 下划线
	String LINE = "_";

	/**
	 * 无需认证即可访问的请求路径
	 */
	String[] IGNORE_PERM_URLS = {
		//swagger文档
		"/v2/api-docs",
		//公有public路径
		"/public/**",
		//监控服务路径
		"/actuator/**",
		//sql监控控制台
		"/druid/**",
		//密码登录
		"/oauth/login/**",
		//退出登录接口
		"/oauth/revoke",
		//aouth2接口
		"/oauth2/**",
	};

}
