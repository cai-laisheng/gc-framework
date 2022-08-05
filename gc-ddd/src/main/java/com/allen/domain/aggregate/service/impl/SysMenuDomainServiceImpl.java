package com.allen.domain.aggregate.service.impl;

import com.allen.domain.aggregate.entity.SysMenuDo;
import com.allen.domain.aggregate.repository.SysMenuRepository;
import com.allen.domain.aggregate.service.SysMenuDomainService;
import com.allen.interfaces.assembler.SysMenuAssembler;
import com.allen.interfaces.dto.SysMenuDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xuguocai
 * @date 2022/8/4 17:09
 */
@Service
public class SysMenuDomainServiceImpl implements SysMenuDomainService {

    @Autowired
    private SysMenuRepository sysMenuRepository;

    @Override
    public void saveMenu(SysMenuDto menuDto) {
        sysMenuRepository.saveMenu(SysMenuAssembler.INSTANCT.conver(menuDto));
    }

}
