package com.allen.domain.aggregate.assembler;

import com.allen.domain.aggregate.entity.SysRoleDo;
import com.allen.infrastructure.po.SysRolePo;
import com.allen.interfaces.dto.SysRoleFormDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xuguocai
 * @date 2022/8/5 10:52
 */
@Mapper
public interface SysRoleAssembler {

    SysRoleAssembler INSTANCT = Mappers.getMapper(SysRoleAssembler.class);

    SysRoleDo converDo(SysRoleFormDto menuDto);

    SysRolePo converPo(SysRoleDo menuDo);
}
