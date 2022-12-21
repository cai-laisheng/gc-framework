package com.allen.sys.service.impl;

import com.allen.sys.mapper.SysMenuMapper;
import com.allen.sys.mapper.SysRoleMapper;
import com.allen.sys.mapper.SysUserMapper;
import com.allen.sys.mapper.SysUserRoleMapper;
import com.allen.sys.model.dto.LoginForm;
import com.allen.sys.model.dto.SysUserRoleDto;
import com.allen.sys.model.dto.UserParam;
import com.allen.sys.model.dto.UserRoleForm;
import com.allen.sys.model.po.SysMenu;
import com.allen.sys.model.po.SysRole;
import com.allen.sys.model.po.SysUser;
import com.allen.sys.model.po.SysUserRole;
import com.allen.sys.service.SystemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * @author: allen小哥 2020-04-14 11:19
 **/
@Slf4j
@Service
@Transactional(readOnly = false)
public class SystemServiceImpl implements SystemService {

    /**
     * 系统用户Mapper
     */
    @Autowired
    private SysUserMapper sysUserMapper;
    /**
     * 系统菜单Mapper
     */
    @Autowired
    private SysMenuMapper sysMenuMapper;
    /**
     * 系统角色Mapper
     */
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    SysUserRoleMapper userRoleMapper;

    @Override
    public SysUser ssoLogin(LoginForm loginForm) {
        String md5Pwd = DigestUtils.md5DigestAsHex(loginForm.getPassword().getBytes());
        log.info("加密后的密码为:{}",md5Pwd);
        SysUser sysUser = sysUserMapper.ssoLogin(loginForm.getLoginName(), md5Pwd);
        return sysUser;
    }

    @Override
    public SysUser getUserMsgByToken(String token) {
        return sysUserMapper.getUserMsgByToken(token);
    }

    @Override
    public SysUser getUserByLoginName(String loginName) {
        SysUser sysUser =new SysUser();
        sysUser.setLoginName(loginName);
        SysUser user = sysUserMapper.selectOne(sysUser);
        if (user == null) {
            return null;
        }

        Integer userId = user.getId();
        user = convertSysUser(user, userId);
        return user;
    }

    /**
     * 通过sso登陆获取用户信息
     *
     * @param loginAccount
     * @return
     * @throws Exception
     */
    @Override
    public SysUser getSsoLoginUserByLoginAccount(String loginAccount) throws Exception {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(loginAccount);
        sysUser = convertSysUser(sysUser, Integer.parseInt(loginAccount));
        return sysUser;
    }

    private SysUser convertSysUser(SysUser sysUser, Integer userId) {
        List<SysRole> sysRoles = sysRoleMapper.findListByUserId(userId);
        List<SysRole> roles = new ArrayList<>();
        sysRoles.forEach(sysRole -> {
            sysRole.setName(sysRole.getCode());
            roles.add(sysRole);
        });
        sysUser.setRoles(roles);
        List<SysMenu> menuList;
        //超级管理员
        if (sysUser.getAdminFlag()) {
            SysMenu menu = new SysMenu();
            menu.setDelFlag(false);
            menuList = sysMenuMapper.select(menu);
        } else {
            menuList = sysMenuMapper.findListByUserId(userId);
        }

        sysUser.setMenus(menuList);
        return sysUser;
    }

    @Override
    public PageInfo<SysUser> findUserPage(UserParam param) {
        // 执行分页查询
        PageHelper.startPage(param.getPageNum(), param.getPageSize(), param.getOrderBy());
        List<SysUser> list = sysUserMapper.findUserPage(param.getName(),param.getLoginName());
        return new PageInfo<>(list);
    }

    @Override
    public SysUser getUserById(Integer userId) {
        return sysUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    @Transactional(readOnly = false)
    public SysUser saveUser(SysUser user) {
        if ( user.getId() == null ) {
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());
            sysUserMapper.insert(user);
        } else {
            // 更新用户数据
            user.setUpdateTime(new Date());
            sysUserMapper.updateByPrimaryKeySelective(user);
        }

        return user;
    }

    @Override
    @Transactional(readOnly = false)
    public void updateUserInfo(SysUser user) {
        // 更新用户数据
        user.setUpdateTime(new Date());
        sysUserMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteUserById(Integer userId) {
        SysUser user = new SysUser();
        user.setId(userId);
        user.setDelFlag(true);
        sysUserMapper.updateByPrimaryKeySelective(user);
        //删除用户对应的角色信息
        sysUserMapper.deleteUserRole(userId);
    }

    @Override
    @Transactional(readOnly = false)
    public void updateUserPasswordById(Integer userId, String newPassword) {
        SysUser user = new SysUser();
        user.setId(userId);
        user.setPassword(newPassword);
        sysUserMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public List<SysRole> getUserRoleByUserId(Integer userId) {
        return sysUserMapper.getUserRoleByUserId(userId);
    }

    @Override
    public void saveUserRole(UserRoleForm userRoleDto) {
        // 删除原来的权限
        Example example = new Example(SysUserRole.class);
        example.createCriteria().andEqualTo("userId",userRoleDto.getUserId());
        userRoleMapper.deleteByExample(example);
        if (StringUtils.isBlank(userRoleDto.getRoleId())){
            return;
        }
        String[] roleIds = userRoleDto.getRoleId().split(",");
        SysUserRole sysUserRole = null;
        for (String roleId :roleIds){
            sysUserRole = new SysUserRole();
            sysUserRole.setUserId(userRoleDto.getUserId());
            sysUserRole.setRoleId(Integer.valueOf(roleId));
            userRoleMapper.insertSelective(sysUserRole);
        }
    }

	@Override
	public Map<String, Object> userLoginLimit(String account) {
		Map<String, Object> map = new HashMap<String, Object>();
		/**
		 * 1、实现方式为，将当前用户的登陆时间记录到缓存系统中，设置失效时间为1分钟
		 * 2、第二次登陆的时候，查询缓存是否存在上次的登陆记录，如果存在，则更新当前的缓存信息
		 * 3、当连续到某次登陆与缓存中的第一次登录时间大于1分钟时，失效该缓存重新记录缓存
		 * 4、如果连续超过5次的登陆都集中在1分钟内时，锁定该用户15分钟之内不可登陆
		 */
//		if( redisRepository.exists("lock_user_" + account) ){
//			//可以拿到锁定的时间
//			String lockTime = redisRepository.get("lock_user_" + account);
//			long time = Long.parseLong(lockTime);
//			long nowTime = System.currentTimeMillis();
//			float exeTime = (float)(nowTime - time)/60000;
//			int seco = (int) exeTime;
//			int count = 15 -seco;
//			if( count == 0 ){
//				count = 1;
//			}
//			map.put("isSuccess", false);
//			map.put("responseMsg", "您的账号由于登陆过于频繁，已经被系统锁定，请" + count + "分钟之后再试");
//		}else{
//			//检查系统是否存在当前用户登陆的次数限制
//			if( redisRepository.exists("count_user_login_" + account)){
//				String countUserLogin = redisRepository.get("count_user_login_" + account);
//				List<Long> list = new ArrayList<Long>();
//				JSONArray jsonArray = JSONArray.parseArray(countUserLogin);
//				list = (List<Long>) JSONArray.parseArray(jsonArray.toJSONString(), Long.class);
//				//获取第一次登陆的时间
//				long firstTime = list.get(0);
//				long nowTime = System.currentTimeMillis();
//				float exeTime = (float)(nowTime - firstTime)/1000;
//				int seco = (int) exeTime/60;
//				int count = 15 -seco;
//				if( count == 0 ){
//					count = 1;
//				}
//				//如果当前时间与第一次登陆时间的时间差大于1分钟
//				if( exeTime > 60 ){
//					redisRepository.del("count_user_login_" + account);
//					map.put("isSuccess", true);
//					map.put("responseMsg", "检验成功");
//				}else{
//					//检查当前list里面的登陆次数
//					if( list.size() < 5 ){
//						list.add(nowTime);
//						redisRepository.setExpire("count_user_login_" + account, list.toString(), 60);
//						map.put("isSuccess", true);
//						map.put("responseMsg", "检验成功");
//					}else{
//						redisRepository.setExpire("lock_user_" + account, nowTime+"", 900);
//						map.put("isSuccess", false);
//						map.put("responseMsg", "您的账号由于登陆过于频繁，已经被系统锁定，请" + count + "分钟之后再试");
//					}
//				}
//			}else{
//				//存入当前登陆时间
//				List<Long> list = new ArrayList<Long>();
//				long nowTime = System.currentTimeMillis();
//				list.add(nowTime);
//				redisRepository.setExpire("count_user_login_" + account, list.toString(), 60);
//
//				map.put("isSuccess", true);
//				map.put("responseMsg", "检验成功");
//			}
//		}
		return map;
	}
}
