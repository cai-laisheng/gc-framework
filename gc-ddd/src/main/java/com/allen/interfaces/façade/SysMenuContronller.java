package com.allen.interfaces.façade;

import com.allen.application.service.SysMenuAppService;
import com.allen.interfaces.commons.ResponseBean;
import com.allen.interfaces.commons.ResponseBeanUtil;
import com.allen.interfaces.commons.ResponseCodeEnum;
import com.allen.interfaces.dto.SysMenuDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author: allen小哥 2020-04-14 11:11 菜单代码示例
 **/
@RestController
@CrossOrigin
@RequestMapping("/admin/auth")
public class SysMenuContronller {

    @Autowired
    SysMenuAppService sysMenuAppService;

    /**
     * Gets menu tree. 接口交互层 --》应用层 --》基础设施层
     *
     * @return the menu tree
     */
    @GetMapping(value = "/menu/tree")
    public ResponseBean getMenuTree() {

        return ResponseBeanUtil.ok();
    }

    /**
     * Gets menu list. 接口交互层 --》应用层 --》基础设施层
     *
     * @return the menu list
     */
    @GetMapping(value = "/menu/list")
    public ResponseBean getMenuList() {

        return ResponseBeanUtil.ok();
    }

    /**
     * Delete menu response entity.
     *
     * @param menuId
     * @return the response entity
     */
    @GetMapping(value = "/menu/delete")
    public ResponseBean deleteMenu(Integer menuId) {
        if (null == menuId) {
            return ResponseBeanUtil.fail(ResponseCodeEnum.FAIL);
        }
        return ResponseBeanUtil.ok();
    }

    /**
     * Gets menu.
     *
     * @param menuId the menu id
     * @return the menu
     */
    @GetMapping(value = "/menu/getMenuInfo")
    public ResponseBean getMenu(Integer menuId) {
        return ResponseBeanUtil.ok(sysMenuAppService.getMenu(menuId));
    }

    /**
     * Save menu sys menu.
     *
     * @param menu
     * @return the sys menu
     */
    @PostMapping(value = "/menu/edit")
    public ResponseBean saveMenu(@RequestBody SysMenuDto menu) {
        sysMenuAppService.saveMenu(menu);
        return ResponseBeanUtil.ok();
    }

}
