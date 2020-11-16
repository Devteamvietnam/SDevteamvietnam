package com.devteamvietnam.framework.web.service;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.devteamvietnam.common.core.domain.entity.SysRole;
import com.devteamvietnam.common.core.domain.model.LoginUser;
import com.devteamvietnam.common.utils.ServletUtils;
import com.devteamvietnam.common.utils.StringUtils;

/**
 * RuoYi pioneered the implementation of custom permissions, ss is taken from the initials of Spring Security
 */
@Service("ss")
public class PermissionService
{
    /** All permissions identification */
    private static final String ALL_PERMISSION = "*:*:*";

    /** Administrator role authority identification */
    private static final String SUPER_ADMIN = "admin";

    private static final String ROLE_DELIMETER = ",";

    private static final String PERMISSION_DELIMETER = ",";

    @Autowired
    private TokenService tokenService;

    /**
     * Verify that the user has certain permissions
     *
     * @param permission permission string
     * @return whether the user has a certain permission
     */
    public boolean hasPermi(String permission)
    {
        if (StringUtils.isEmpty(permission))
        {
            return false;
        }
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions()))
        {
            return false;
        }
        return hasPermissions(loginUser.getPermissions(), permission);
    }

    /**
     * Verify whether the user does not have a certain permission, contrary to the logic of hasPermi
     *
     * @param permission permission string
     * @return whether the user does not have a certain permission
     */
    public boolean lacksPermi(String permission)
    {
        return hasPermi(permission) != true;
    }

    /**
     * Verify that the user has any of the following permissions
     *
     * @param permissions list of permissions with PERMISSION_NAMES_DELIMETER as the separator
     * @return Does the user have any of the following permissions
     */
    public boolean hasAnyPermi(String permissions)
    {
        if (StringUtils.isEmpty(permissions))
        {
            return false;
        }
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getPermissions()))
        {
            return false;
        }
        Set<String> authorities = loginUser.getPermissions();
        for (String permission: permissions.split(PERMISSION_DELIMETER))
        {
            if (permission != null && hasPermissions(authorities, permission))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine whether the user has a certain role
     *
     * @param role role string
     * @return whether the user has a certain role
     */
    public boolean hasRole(String role)
    {
        if (StringUtils.isEmpty(role))
        {
            return false;
        }
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getUser().getRoles()))
        {
            return false;
        }
        for (SysRole sysRole: loginUser.getUser().getRoles())
        {
            String roleKey = sysRole.getRoleKey();
            if (SUPER_ADMIN.equals(roleKey) || roleKey.equals(StringUtils.trim(role)))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Verify whether the user does not have a certain role, which is the opposite of isRole logic.
     *
     * @param role role name
     * @return whether the user does not have a certain role
     */
    public boolean lacksRole(String role)
    {
        return hasRole(role) != true;
    }

    /**
     * Verify that the user has any of the following roles
     *
     * @param roles list of roles with ROLE_NAMES_DELIMETER as the separator
     * @return Does the user have any of the following roles
     */
    public boolean hasAnyRoles(String roles)
    {
        if (StringUtils.isEmpty(roles))
        {
            return false;
        }
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        if (StringUtils.isNull(loginUser) || CollectionUtils.isEmpty(loginUser.getUser().getRoles()))
        {
            return false;
        }
        for (String role: roles.split(ROLE_DELIMETER))
        {
            if (hasRole(role))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine whether to include permissions
     *
     * @param permissions permission list
     * @param permission permission string
     * @return whether the user has a certain permission
     */
    private boolean hasPermissions(Set<String> permissions, String permission)
    {
        return permissions.contains(ALL_PERMISSION) || permissions.contains(StringUtils.trim(permission));
    }
}
