package com.allen.domain.aggregate.repository.jpa;

import com.allen.application.converter.SysUserAssembler;
import com.allen.domain.aggregate.entity.SysUserDo;
import com.allen.domain.aggregate.repository.SysUserRepository;
import com.allen.infrastructure.po.SysUserPo;
import com.allen.infrastructure.storage.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author xuguocai
 * @date 2022/8/5 16:43
 */
@Repository
public class SysUserRepositoryImpl implements SysUserRepository {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public void saveUser(SysUserDo user) {
        SysUserPo sysUserPo = SysUserAssembler.INSTANCT.converPo(user);
        if (sysUserPo.getId() == null){
            sysUserMapper.insertSelective(sysUserPo);
        }else{
            SysUserPo dbtmp = sysUserMapper.selectByPrimaryKey(sysUserPo.getId());
            if (dbtmp != null){
                sysUserMapper.updateByPrimaryKeySelective(sysUserPo);
            }
        }
    }
}
