package com.allen.application.service;

import com.allen.application.converter.ValidateConverter;
import com.allen.domain.aggregate.service.SysMenuDomainService;
import com.allen.infrastructure.po.SysMenuPo;
import com.allen.infrastructure.storage.SysMenuMapper;
import com.allen.interfaces.assembler.SysMenuAssembler;
import com.allen.interfaces.dto.SysMenuDto;
import com.allen.interfaces.vo.SysMenuVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 业务编排、校验
 * 处理逻辑：涉及领域的，走领域层。若无领域，比较简单的处理，可以直接走基础设施层
 * @author xuguocai
 * @date 2022/8/4 15:55
 */
@Service
@Slf4j
public class SysMenuAppService {

    @Autowired
    private SysMenuDomainService sysMenuDomainService;
    @Autowired
    SysMenuMapper sysMenuMapper;

    public SysMenuDto saveMenu(SysMenuDto menuDto){
        // 校验
        ValidateConverter.validateMenu(menuDto);
        // 分发到领域层
        sysMenuDomainService.saveMenu(menuDto);
        return null;
    }

    public SysMenuVo getMenu(Integer menuId){
        if (menuId == null){
            throw new RuntimeException("菜单ID不能为空");
        }

        return SysMenuAssembler.INSTANCT.converVo(sysMenuMapper.selectByPrimaryKey(menuId));
    }

    void deleteMenu(Integer menuId){
        if (menuId == null){
            throw new RuntimeException("菜单ID不能为空");
        }
        // 查看菜单是否存在
        SysMenuPo sysMenuPo = sysMenuMapper.selectByPrimaryKey(menuId);
        if (sysMenuPo == null){
            throw new RuntimeException("菜单不存在，请重新操作");
        }

        if (sysMenuPo.isDelFlag()){
            throw new RuntimeException("菜单已经被删除，请重新操作");
        }

        sysMenuPo.setDelFlag(Boolean.TRUE);

        sysMenuMapper.updateByPrimaryKeySelective(sysMenuPo);
    }
}
