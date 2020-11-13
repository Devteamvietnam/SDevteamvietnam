package com.devteamvietnam.system.mapper;

import java.util.List;

import com.devteamvietnam.system.domain.SysRoleDept;

/**
 * Role and department association table Data layer
 *
 * @author ivan
 */
public interface SysRoleDeptMapper
{
    /**
     * Delete role and department association by role ID
     *
     * @param roleId role ID
     * @return result
     */
    public int deleteRoleDeptByRoleId(Long roleId);

    /**
     * Batch delete role department related information
     *
     * @param ids ID of the data to be deleted
     * @return result
     */
    public int deleteRoleDept(Long[] ids);

    /**
     * Query the number of departments used
     *
     * @param deptId department ID
     * @return result
     */
    public int selectCountRoleDeptByDeptId(Long deptId);

    /**
     * Add role department information in batches
     *
     * @param roleDeptList role department list
     * @return result
     */
    public int batchRoleDept(List<SysRoleDept> roleDeptList);
}
