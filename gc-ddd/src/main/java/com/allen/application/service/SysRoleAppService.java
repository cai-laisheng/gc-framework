package com.allen.application.service;

import com.allen.application.converter.ValidateConverter;
import com.allen.interfaces.dto.SysRoleFormDto;
import org.springframework.stereotype.Component;


/**
 * @author xuguocai
 * @date 2022/8/5 10:06
 */
@Component
public class SysRoleAppService {

    public void saveRole(SysRoleFormDto formDto){
        ValidateConverter.validateRole(formDto);

    }

}
