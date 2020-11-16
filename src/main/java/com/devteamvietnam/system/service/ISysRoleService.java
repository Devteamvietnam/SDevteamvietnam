package com.devteamvietnam.system.service;

import java.util.List;
import java.util.Set;

import com.devteamvietnam.common.core.domain.entity.SysRole;

/**
 * Role business layer
 *
 * @author ivan
 */
public interface ISysRoleService
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
     * @return permission list
     */
    public Set<String> selectRolePermissionByUserId(Long userId);

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
     * Verify that the role name is unique
     *
     * @param role role information
     * @return result
     */
    public String checkRoleNameUnique(SysRole role);

    /**
     * Verify that the role permissions are unique
     *
     * @param role role information
     * @return result
     */
    public String checkRoleKeyUnique(SysRole role);

    /**
     * Verify whether the role allows operation
     *
     * @param role role information
     */
    public void checkRoleAllowed(SysRole role);

    /**
     * Query the number of roles used by role ID
     *
     * @param roleId role ID
     * @return result
     */
    public int countUserRoleByRoleId(Long roleId);

    /**
     * Added saving role information
     *
     * @param role role information
     * @return result
     */
    public int insertRole(SysRole role);

    /**
     * Modify and save role information
     *
     * @param role role information
     * @return result
     */
    public int updateRole(SysRole role);

    /**
     * Modify role status
     *
     * @param role role information
     * @return result
     */
    public int updateRoleStatus(SysRole role);

    /**
     * Modify data permission information
     *
     * @param role role information
     * @return result
     */
    public int authDataScope(SysRole role);

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
