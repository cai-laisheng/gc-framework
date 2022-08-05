package com.allen.domain.aggregate.service.impl;

import com.allen.domain.aggregate.repository.SysRoleRepository;
import com.allen.domain.aggregate.service.SysRoleDomainService;
import com.allen.domain.aggregate.assembler.SysRoleAssembler;
import com.allen.interfaces.dto.SysRoleFormDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xuguocai
 * @date 2022/8/5 10:49
 */
@Service
public class SysRoleDomainServiceImpl implements SysRoleDomainService {

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Override
    public void saveRole(SysRoleFormDto formDto) {
        sysRoleRepository.saveRole(SysRoleAssembler.INSTANCT.converDo(formDto));
    }

}
