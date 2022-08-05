package com.allen.domain.aggregate.repository;

import com.allen.domain.aggregate.entity.SysUserDo;

/**
 * @author xuguocai
 * @date 2022/8/5 16:40
 */
public interface SysUserRepository {

    public void saveUser(SysUserDo user);

}
