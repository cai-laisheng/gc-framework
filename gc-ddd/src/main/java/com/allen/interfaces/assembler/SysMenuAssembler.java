package com.allen.interfaces.assembler;

import com.allen.domain.aggregate.entity.SysMenuDo;
import com.allen.infrastructure.po.SysMenuPo;
import com.allen.interfaces.dto.SysMenuDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author xuguocai  dto 与 do 的转化
 * @date 2022/8/4 16:09
 */
@Mapper
public interface SysMenuAssembler {

    SysMenuAssembler INSTANCT = Mappers.getMapper(SysMenuAssembler.class);

    @Mapping(target = "parentId", source = "parentId")
    @Mapping(target = "targetType", ignore = true) // 忽略id，不进行映射
    SysMenuDo conver(SysMenuDto menuDto);


    @Mapping(target = "createTime", ignore = true) // 忽略id，不进行映射
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "delFlag", ignore = true)
    SysMenuPo converPo(SysMenuDo menuDo);

}
