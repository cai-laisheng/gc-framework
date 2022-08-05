package com.allen.application.converter;

import com.allen.interfaces.dto.SysMenuDto;
import com.allen.interfaces.dto.SysRoleFormDto;
import com.allen.interfaces.dto.SysUserDto;
import org.apache.commons.lang3.StringUtils;

/**
 * 1、基于校验的校验器，也可以使用Mapstruct. 2、也可以在dto转化do的过程进行校验 3、也可以基于注解方式处理（推荐）
 * @author xuguocai
 * @date 2022/8/4 16:54
 */
public class ValidateConverter {

    public static void validateMenu(SysMenuDto menuDto){
        if (StringUtils.isEmpty(menuDto.getText())){
            throw new RuntimeException("菜单名称不能为空");
        }

        if (null == menuDto.getParentId()){
            menuDto.setParentId(0);
        }

        if (StringUtils.isEmpty(menuDto.getUrl())){
            throw new RuntimeException("菜单路径不能为空");
        }
    }

    public static void validateRole(SysRoleFormDto roleDto){
        if (StringUtils.isEmpty(roleDto.getName())){
            throw new RuntimeException("角色名称不能为空");
        }

        if (StringUtils.isEmpty(roleDto.getCode())){
            throw new RuntimeException("角色编码不能为空");
        }
    }

    public static void validateUser(SysUserDto userDto){
        if (StringUtils.isEmpty(userDto.getName())){
            throw new RuntimeException("用户名称不为空");
        }
        if (StringUtils.isEmpty(userDto.getPassword())){
            throw new RuntimeException("用户密码不能为空");
        }
    }
}
