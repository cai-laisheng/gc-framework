package com.allen.application.converter;

import com.allen.interfaces.dto.SysMenuDto;
import org.apache.commons.lang3.StringUtils;

/**
 * 基于校验的转化器，也可以使用Mapstruct
 * @author xuguocai
 * @date 2022/8/4 16:54
 */
public class SysMenuConverter {

    public static void validate(SysMenuDto menuDto){
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

}
