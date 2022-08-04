package com.allen.application.service;

import com.allen.application.converter.SysMenuConverter;
import com.allen.domain.aggregate.entity.SysMenuDo;
import com.allen.domain.aggregate.service.SysMenuDomainService;
import com.allen.interfaces.assembler.SysMenuAssembler;
import com.allen.interfaces.dto.SysMenuDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 业务编排、校验
 * @author xuguocai
 * @date 2022/8/4 15:55
 */
@Service
@Slf4j
public class SysMenuAppService {

    @Autowired
    private SysMenuDomainService sysMenuDomainService;

    public SysMenuDto saveMenu(SysMenuDto menuDto){
        // 校验
        SysMenuConverter.validate(menuDto);
        // 分发到领域层
        sysMenuDomainService.saveMenu(menuDto);
        return null;
    }

}
