package com.allen.application.converter;

import com.allen.domain.aggregate.entity.SysUserDo;
import com.allen.infrastructure.po.SysUserPo;
import com.allen.interfaces.dto.SysUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xuguocai
 * @date 2022/8/5 16:06
 */
@Mapper
public interface SysUserAssembler {
    SysUserAssembler INSTANCT = Mappers.getMapper(SysUserAssembler.class);

    SysUserDo converDo(SysUserDto userDto);

    SysUserPo converPo(SysUserDo userDo);

}
