package com.devteamvietnam.system.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devteamvietnam.common.annotation.DataScope;
import com.devteamvietnam.common.constant.UserConstants;
import com.devteamvietnam.common.core.domain.entity.SysRole;
import com.devteamvietnam.common.exception.CustomException;
import com.devteamvietnam.common.utils.StringUtils;
import com.devteamvietnam.common.utils.spring.SpringUtils;
import com.devteamvietnam.system.domain.SysRoleDept;
import com.devteamvietnam.system.domain.SysRoleMenu;
import com.devteamvietnam.system.mapper.SysRoleDeptMapper;
import com.devteamvietnam.system.mapper.SysRoleMapper;
import com.devteamvietnam.system.mapper.SysRoleMenuMapper;
import com.devteamvietnam.system.mapper.SysUserRoleMapper;
import com.devteamvietnam.system.service.ISysRoleService;

/**
 * Role business layer processing
 *
 * @author ivan
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService
{
    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysRoleDeptMapper roleDeptMapper;

    /**
     * Paging query role data based on conditions
     *
     * @param role role information
     * @return role data collection information
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysRole> selectRoleList(SysRole role)
    {
        return roleMapper.selectRoleList(role);
    }

    /**
     * Query permissions based on user ID
     *
     * @param userId user ID
     * @return permission list
     */
    @Override
    public Set<String> selectRolePermissionByUserId(Long userId)
    {
        List<SysRole> perms = roleMapper.selectRolePermissionByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm: perms)
        {
            if (StringUtils.isNotNull(perm))
            {
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * Query all roles
     *
     * @return role list
     */
    @Override
    public List<SysRole> selectRoleAll()
    {
        return SpringUtils.getAopProxy(this).selectRoleList(new SysRole());
    }

    /**
     * Get the list of role selection boxes based on user ID
     *
     * @param userId user ID
     * @return selected role ID list
     */
    @Override
    public List<Integer> selectRoleListByUserId(Long userId)
    {
        return roleMapper.selectRoleListByUserId(userId);
    }

    /**
     * Query role by role ID
     *
     * @param roleId role ID
     * @return role object information
     */
    @Override
    public SysRole selectRoleById(Long roleId)
    {
        return roleMapper.selectRoleById(roleId);
    }

    /**
     * Verify that the role name is unique
     *
     * @param role role information
     * @return result
     */
    @Override
    public String checkRoleNameUnique(SysRole role)
    {
        Long roleId = StringUtils.isNull(role.getRoleId())? -1L: role.getRoleId();
        SysRole info = roleMapper.checkRoleNameUnique(role.getRoleName());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * Verify that the role permissions are unique
     *
     * @param role role information
     * @return result
     */
    @Override
    public String checkRoleKeyUnique(SysRole role)
    {
        Long roleId = StringUtils.isNull(role.getRoleId())? -1L: role.getRoleId();
        SysRole info = roleMapper.checkRoleKeyUnique(role.getRoleKey());
        if (StringUtils.isNotNull(info) && info.getRoleId().longValue() != roleId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * Verify whether the role allows operation
     *
     * @param role role information
     */
    @Override
    public void checkRoleAllowed(SysRole role)
    {
        if (StringUtils.isNotNull(role.getRoleId()) && role.isAdmin())
        {
            throw new CustomException("Operation of super administrator role is not allowed");
        }
    }

    /**
     * Query the number of roles used by role ID
     *
     * @param roleId role ID
     * @return result
     */
    @Override
    public int countUserRoleByRoleId(Long roleId)
    {
        return userRoleMapper.countUserRoleByRoleId(roleId);
    }

    /**
     * Added saving role information
     *
     * @param role role information
     * @return result
     */
    @Override
    @Transactional
    public int insertRole(SysRole role)
    {
        // New role information
        roleMapper.insertRole(role);
        return insertRoleMenu(role);
    }

    /**
     * Modify and save role information
     *
     * @param role role information
     * @return result
     */
    @Override
    @Transactional
    public int updateRole(SysRole role)
    {
        // Modify role information
        roleMapper.updateRole(role);
        // Delete the role and menu association
        roleMenuMapper.deleteRoleMenuByRoleId(role.getRoleId());
        return insertRoleMenu(role);
    }

    /**
     * Modify role status
     *
     * @param role role information
     * @return result
     */
    @Override
    public int updateRoleStatus(SysRole role)
    {
        return roleMapper.updateRole(role);
    }
    /**
     * Modify data permission information
     *
     * @param role role information
     * @return result
     */
    @Override
    @Transactional
    public int authDataScope(SysRole role)
    {
        // Modify role information
        roleMapper.updateRole(role);
        // Delete the role and department association
        roleDeptMapper.deleteRoleDeptByRoleId(role.getRoleId());
        // Add role and department information (data permissions)
        return insertRoleDept(role);
    }

    /**
     * Added role menu information
     *
     * @param role role object
     */
    public int insertRoleMenu(SysRole role)
    {
        int rows = 1;
        // Add user and role management
        List<SysRoleMenu> list = new ArrayList<SysRoleMenu>();
        for (Long menuId: role.getMenuIds())
        {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getRoleId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size()> 0)
        {
            rows = roleMenuMapper.batchRoleMenu(list);
        }
        return rows;
    }

    /**
     * Added role department information (data permissions)
     *
     * @param role role object
     */
    public int insertRoleDept(SysRole role)
    {
        int rows = 1;
        // Added role and department (data authority) management
        List<SysRoleDept> list = new ArrayList<SysRoleDept>();
        for (Long deptId: role.getDeptIds())
        {
            SysRoleDept rd = new SysRoleDept();
            rd.setRoleId(role.getRoleId());
            rd.setDeptId(deptId);
            list.add(rd);
        }
        if (list.size()> 0)
        {
            rows = roleDeptMapper.batchRoleDept(list);
        }
        return rows;
    }

    /**
     * Delete role by role ID
     *
     * @param roleId role ID
     * @return result
     */
    @Override
    public int deleteRoleById(Long roleId)
    {
        return roleMapper.deleteRoleById(roleId);
    }

    /**
     * Batch delete role information
     *
     * @param roleIds ID of the role to be deleted
     * @return result
     */
    @Override
    public int deleteRoleByIds(Long[] roleIds)
    {
        for (Long roleId: roleIds)
        {
            checkRoleAllowed(new SysRole(roleId));
            SysRole role = selectRoleById(roleId);
            if (countUserRoleByRoleId(roleId)> 0)
            {
                throw new CustomException(String.format("%1$s has been allocated and cannot be deleted", role.getRoleName()));
            }
        }
        return roleMapper.deleteRoleByIds(roleIds);
    }
}
