package com.allen.domain.aggregate.repository.jpa;

import com.allen.domain.aggregate.assembler.SysRoleAssembler;
import com.allen.domain.aggregate.entity.SysRoleDo;
import com.allen.domain.aggregate.repository.SysRoleRepository;
import com.allen.infrastructure.po.SysRolePo;
import com.allen.infrastructure.storage.SysRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author xuguocai
 * @date 2022/8/5 11:00
 */
public class SysRoleRepositoryImpl implements SysRoleRepository {

    @Autowired
    SysRoleMapper sysRoleMapper;

    @Override
    public void saveRole(SysRoleDo sysRoleDo) {
        SysRolePo sysRolePo = SysRoleAssembler.INSTANCT.converPo(sysRoleDo);
        if (sysRolePo.getId() == null){
            sysRoleMapper.insertSelective(sysRolePo);
        }else {
            sysRoleMapper.updateByPrimaryKeySelective(sysRolePo);
        }
    }

}
