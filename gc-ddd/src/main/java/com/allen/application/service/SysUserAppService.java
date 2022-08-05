package com.allen.application.service;

import com.allen.application.converter.SysUserAssembler;
import com.allen.application.converter.ValidateConverter;
import com.allen.domain.aggregate.entity.SysUserDo;
import com.allen.domain.aggregate.service.SysUserDomainService;
import com.allen.interfaces.dto.SysUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xuguocai
 * @date 2022/8/5 15:59
 */
@Component
public class SysUserAppService {
    @Autowired
    SysUserDomainService sysUserDomainService;

   public void saveUser(SysUserDto user){
        // 首先校验入参
       ValidateConverter.validateUser(user);
        // dto转化do，传到领域层
       sysUserDomainService.saveUser(SysUserAssembler.INSTANCT.converDo(user));
   }

}
