package com.allen.domain.aggregate.service.impl;

import com.allen.domain.aggregate.entity.SysUserDo;
import com.allen.domain.aggregate.repository.SysUserRepository;
import com.allen.domain.aggregate.service.SysUserDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xuguocai
 * @date 2022/8/5 16:37
 */
@Service
public class SysUserDomainServiceImpl implements SysUserDomainService {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Override
    public void saveUser(SysUserDo user) {
        sysUserRepository.saveUser(user);
    }

}
