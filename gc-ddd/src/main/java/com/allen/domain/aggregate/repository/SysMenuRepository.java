package com.allen.domain.aggregate.repository;

import com.allen.domain.aggregate.entity.SysMenuDo;
import com.allen.interfaces.dto.SysMenuDto;

/**
 * 领域层的仓储
 * @author xuguocai
 * @date 2022/8/4 17:14
 */
public interface SysMenuRepository {

    void saveMenu(SysMenuDo menuDo);

}
