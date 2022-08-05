package com.allen.interfaces.façade;

import com.allen.application.service.SysUserAppService;
import com.allen.interfaces.commons.ResponseBean;
import com.allen.interfaces.commons.ResponseBeanUtil;
import com.allen.interfaces.dto.SysUserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


/**
 * @author: allen小哥 2020-04-14 11:18
 **/
@RestController
@CrossOrigin
@RequestMapping("/admin/auth")
public class UserController {

	@Autowired
	private SysUserAppService sysUserAppService;

	/**
	 * create token
	 *
	 * @return
	 */
	@PostMapping("/ssoLogin")
	public ResponseBean ssoLogin() {

		return ResponseBeanUtil.ok();
	}

	/**
	 * 退出登录
	 * @param request
	 * @return
	 */
	@GetMapping("/logout")
	public ResponseBean deleteToken(HttpServletRequest request){
		String tokenHeader = request.getHeader("Authorization");

		return ResponseBeanUtil.ok();
	}

    /**
     * Save current user info response entity.
     *
     * @return the response entity
     */
    @PostMapping(value = "/user/info")
    public ResponseBean saveCurrentUserInfo() {

        return ResponseBeanUtil.ok();
    }
    
    /**
     * Reset password response entity.
     *
     * @return the response entity
     */
    @PostMapping(value = "/user/password")
    public ResponseBean updatePassword(){

		return ResponseBeanUtil.fail();

    }
    
    /**
     * List page info.
     *
     * @return the page info
     */
    @PostMapping(value = "/user/list")
    public ResponseBean userList() {

		return ResponseBeanUtil.ok();
    }

    /**
     * Gets user.
     *
     * @param userId the user id
     * @return the user
     */
    @GetMapping(value = "/user/getUserInfo")
    public ResponseBean getUserInfo(Integer userId) {

		return ResponseBeanUtil.ok();
    }

    /**
     * Save user sys user.
     *
     * @param user
     * @return the sys user
     */
    @PostMapping(value = "/user/edit")
    public ResponseBean saveUser( @RequestBody SysUserDto user) {
		sysUserAppService.saveUser(user);
		return ResponseBeanUtil.ok();
    }

    /**
     * Delete response entity.
     *
     * @param userId the user id
     * @return the response entity
     */
    @GetMapping(value = "/user/deleteUser")
    public ResponseBean userDelete(Integer userId) {

        return ResponseBeanUtil.ok();
    }
    
    /**
     * 获取用户已授权列表
     * @param userId
     * @return
     */
	@GetMapping(value = "/user/getUserRoleByUserId")
	public ResponseBean getUserRoleByUserId(Integer userId) {

		return ResponseBeanUtil.ok();
	}

    /**
     * 保存用户与角色权限关系
     * @return
     */
    @PostMapping(value = "/user/saveUserRole")
    public ResponseBean saveUserRole() {

		return ResponseBeanUtil.ok();
    }

}
