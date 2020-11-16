package com.devteamvietnam.system.mapper;

import java.util.List;

import com.devteamvietnam.common.core.domain.entity.SysRole;

/**
 * Role table data layer
 *
 * @author ivan
 */
public interface SysRoleMapper
{
    /**
     * Paging query role data based on conditions
     *
     * @param role role information
     * @return role data collection information
     */
    public List<SysRole> selectRoleList(SysRole role);

    /**
     * Query role based on user ID
     *
     * @param userId user ID
     * @return role list
     */
    public List<SysRole> selectRolePermissionByUserId(Long userId);

    /**
     * Query all roles
     *
     * @return role list
     */
    public List<SysRole> selectRoleAll();

    /**
     * Get the list of role selection boxes based on user ID
     *
     * @param userId user ID
     * @return selected role ID list
     */
    public List<Integer> selectRoleListByUserId(Long userId);

    /**
     * Query role by role ID
     *
     * @param roleId role ID
     * @return role object information
     */
    public SysRole selectRoleById(Long roleId);

    /**
     * Query role based on user ID
     *
     * @param userName username
     * @return role list
     */
    public List<SysRole> selectRolesByUserName(String userName);

    /**
     * Verify that the role name is unique
     *
     * @param roleName role name
     * @return role information
     */
    public SysRole checkRoleNameUnique(String roleName);

    /**
     * Verify that the role permissions are unique
     *
     * @param roleKey role permissions
     * @return role information
     */
    public SysRole checkRoleKeyUnique(String roleKey);

    /**
     * Modify role information
     *
     * @param role role information
     * @return result
     */
    public int updateRole(SysRole role);

    /**
     * Added role information
     *
     * @param role role information
     * @return result
     */
    public int insertRole(SysRole role);

    /**
     * Delete role by role ID
     *
     * @param roleId role ID
     * @return result
     */
    public int deleteRoleById(Long roleId);

    /**
     * Batch delete role information
     *
     * @param roleIds ID of the role to be deleted
     * @return result
     */
    public int deleteRoleByIds(Long[] roleIds);
}
