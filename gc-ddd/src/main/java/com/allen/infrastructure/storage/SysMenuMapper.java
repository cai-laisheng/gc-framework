package com.allen.infrastructure.storage;

import com.allen.infrastructure.po.SysMenuPo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface SysMenuMapper extends Mapper<SysMenuPo> {

    /**
     * 根据用户id判断用户是否超级管理员
     *
     * @param userId the user id
     * @return the int
     */
    int checkUserIsSysAdmin(@Param("userId") Integer userId);

    /**
     * 菜单权限过滤查询(管理员与非管理员)
     * @param map
     * @return
     */
    List<SysMenuPo> findListByParam(@Param("param") Map<String,Object> map);

    /**
     * 根据用户查询菜单
     *
     * @param userId the user id
     * @return the list
     */
    List<SysMenuPo> findListByUserId(Integer userId);

}