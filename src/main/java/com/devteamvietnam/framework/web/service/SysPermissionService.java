package com.devteamvietnam.framework.web.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.devteamvietnam.common.core.domain.entity.SysUser;
import com.devteamvietnam.system.service.ISysMenuService;
import com.devteamvietnam.system.service.ISysRoleService;

/**
 * User authority processing
 */
@Component
public class SysPermissionService
{
    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysMenuService menuService;

    /**
     * Get role data permissions
     *
     * @param user user information
     * @return role permission information
     */
    public Set<String> getRolePermission(SysUser user)
    {
        Set<String> roles = new HashSet<String>();
        // The administrator has all permissions
        if (user.isAdmin())
        {
            roles.add("admin");
        }
        else
        {
            roles.addAll(roleService.selectRolePermissionByUserId(user.getUserId()));
        }
        return roles;
    }

    /**
     * Get menu data permissions
     *
     * @param user user information
     * @return menu permission information
     */
    public Set<String> getMenuPermission(SysUser user)
    {
        Set<String> perms = new HashSet<String>();
        // The administrator has all permissions
        if (user.isAdmin())
        {
            perms.add("*:*:*");
        }
        else
        {
            perms.addAll(menuService.selectMenuPermsByUserId(user.getUserId()));
        }
        return perms;
    }
}
