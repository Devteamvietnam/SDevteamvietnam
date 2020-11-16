package com.devteamvietnam.admin.controller.system;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devteamvietnam.common.annotation.Log;
import com.devteamvietnam.common.constant.Constants;
import com.devteamvietnam.common.constant.UserConstants;
import com.devteamvietnam.common.core.controller.BaseController;
import com.devteamvietnam.common.core.domain.AjaxResult;
import com.devteamvietnam.common.core.domain.entity.SysMenu;
import com.devteamvietnam.common.core.domain.model.LoginUser;
import com.devteamvietnam.common.enums.BusinessType;
import com.devteamvietnam.common.utils.SecurityUtils;
import com.devteamvietnam.common.utils.ServletUtils;
import com.devteamvietnam.common.utils.StringUtils;
import com.devteamvietnam.framework.web.service.TokenService;
import com.devteamvietnam.system.service.ISysMenuService;

/**
 * Menu information
 *
 * @author ivan
 */
@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController
{
    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private TokenService tokenService;

    /**
     * Get the menu list
     */
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    @GetMapping("/list")
    public AjaxResult list(SysMenu menu)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        return AjaxResult.success(menus);
    }

    /**
     * Get detailed information according to the menu number
     */
    @PreAuthorize("@ss.hasPermi('system:menu:query')")
    @GetMapping(value = "/{menuId}")
    public AjaxResult getInfo(@PathVariable Long menuId)
    {
        return AjaxResult.success(menuService.selectMenuById(menuId));
    }

    /**
     * Get the menu drop-down tree list
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysMenu menu)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        List<SysMenu> menus = menuService.selectMenuList(menu, userId);
        return AjaxResult.success(menuService.buildMenuTreeSelect(menus));
    }

    /**
     * Load the corresponding role menu list tree
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public AjaxResult roleMenuTreeselect(@PathVariable("roleId") Long roleId)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        List<SysMenu> menus = menuService.selectMenuList(loginUser.getUser().getUserId());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeSelect(menus));
        return ajax;
    }

    /**
     * New menu
     */
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    @Log(title = "Menu Management", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysMenu menu)
    {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu)))
        {
            return AjaxResult.error("New menu'" + menu.getMenuName() + "'Failed, the menu name already exists");
        }
        else if (UserConstants.YES_FRAME.equals(menu.getIsFrame())
                && !StringUtils.startsWithAny(menu.getPath(), Constants.HTTP, Constants.HTTPS))
        {
            return AjaxResult.error("New menu'" + menu.getMenuName() + "'Failed, the address must start with http(s)://");
        }
        menu.setCreateBy(SecurityUtils.getUsername());
        return toAjax(menuService.insertMenu(menu));
    }

    /**
     * Modify the menu
     */
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @Log(title = "Menu Management", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysMenu menu)
    {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu)))
        {
            return AjaxResult.error("Modify the menu'" + menu.getMenuName() + "'Failed, the menu name already exists");
        }
        else if (UserConstants.YES_FRAME.equals(menu.getIsFrame())
                && !StringUtils.startsWithAny(menu.getPath(), Constants.HTTP, Constants.HTTPS))
        {
            return AjaxResult.error("Modify menu'" + menu.getMenuName() + "'Failed, the address must start with http(s)://");
        }
        else if (menu.getMenuId().equals(menu.getParentId()))
        {
            return AjaxResult.error("Modify the menu'" + menu.getMenuName() + "'Failed, the superior menu cannot choose oneself");
        }
        menu.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(menuService.updateMenu(menu));
    }

    /**
     * Delete menu
     */
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    @Log(title = "Menu Management", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuId}")
    public AjaxResult remove(@PathVariable("menuId") Long menuId)
    {
        if (menuService.hasChildByMenuId(menuId))
        {
            return AjaxResult.error("Sub-menu exists, delete is not allowed");
        }
        if (menuService.checkMenuExistRole(menuId))
        {
            return AjaxResult.error("The menu has been assigned and is not allowed to be deleted");
        }
        return toAjax(menuService.deleteMenuById(menuId));
    }
}
