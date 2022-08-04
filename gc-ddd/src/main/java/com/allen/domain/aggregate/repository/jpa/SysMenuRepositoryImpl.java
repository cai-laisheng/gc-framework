package com.allen.domain.aggregate.repository.jpa;

import com.allen.domain.aggregate.entity.SysMenuDo;
import com.allen.domain.aggregate.repository.SysMenuRepository;
import com.allen.infrastructure.po.SysMenuPo;
import com.allen.infrastructure.storage.SysMenuMapper;
import com.allen.interfaces.assembler.SysMenuAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author xuguocai
 * @date 2022/8/4 17:25
 */
@Repository
public class SysMenuRepositoryImpl implements SysMenuRepository {

    @Autowired
    SysMenuMapper sysMenuMapper;

    @Override
    public void saveMenu(SysMenuDo menuDo) {
        SysMenuPo sysMenuPo = SysMenuAssembler.INSTANCT.converPo(menuDo);
        if (sysMenuPo.getId() == null){
            sysMenuMapper.insert(sysMenuPo);
        }else {
            SysMenuPo systmp = sysMenuMapper.selectByPrimaryKey(sysMenuPo.getId());
            if (systmp == null){
                sysMenuPo.setId(null);
                sysMenuMapper.insert(sysMenuPo);
            }else {
                sysMenuMapper.updateByPrimaryKeySelective(sysMenuPo);
            }
        }

    }
}
