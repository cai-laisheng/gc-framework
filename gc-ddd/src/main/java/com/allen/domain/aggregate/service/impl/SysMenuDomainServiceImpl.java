package com.allen.domain.aggregate.service.impl;

import com.allen.domain.aggregate.entity.SysMenuDo;
import com.allen.domain.aggregate.service.SysMenuDomainService;
import com.allen.interfaces.assembler.SysMenuAssembler;
import com.allen.interfaces.dto.SysMenuDto;
import org.springframework.stereotype.Service;

/**
 * @author xuguocai
 * @date 2022/8/4 17:09
 */
@Service
public class SysMenuDomainServiceImpl implements SysMenuDomainService {

    @Override
    public void saveMenu(SysMenuDto menuDto) {
        SysMenuDo sysMenuDo = SysMenuAssembler.INSTANCT.conver(menuDto);

    }

}
