package com.devteamvietnam.system.service;

import java.util.List;

import com.devteamvietnam.common.core.domain.TreeSelect;
import com.devteamvietnam.common.core.domain.entity.SysDept;

/**
 * Department management service layer
 *
 * @author ivan
 */
public interface ISysDeptService
{
    /**
     * Query department management data
     *
     * @param dept department information
     * @return department information collection
     */
    public List<SysDept> selectDeptList(SysDept dept);

    /**
     * Build the tree structure required by the front end
     *
     * @param depts department list
     * @return tree structure list
     */
    public List<SysDept> buildDeptTree(List<SysDept> depts);

    /**
     * Build the drop-down tree structure required by the front end
     *
     * @param depts department list
     * @return drop-down tree structure list
     */
    public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts);

    /**
     * Query department tree information based on role ID
     *
     * @param roleId role ID
     * @return selected department list
     */
    public List<Integer> selectDeptListByRoleId(Long roleId);

    /**
     * Query information based on department ID
     *
     * @param deptId department ID
     * @return department information
     */
    public SysDept selectDeptById(Long deptId);

    /**
     * Query all sub-departments based on ID (normal state)
     *
     * @param deptId department ID
     * @return Number of sub-departments
     */
    public int selectNormalChildrenDeptById(Long deptId);

    /**
     * Whether there are department sub-nodes
     *
     * @param deptId department ID
     * @return result
     */
    public boolean hasChildByDeptId(Long deptId);

    /**
     * Query whether there are users in the department
     *
     * @param deptId department ID
     * @return result true exists false does not exist
     */
    public boolean checkDeptExistUser(Long deptId);

    /**
     * Check whether the department name is unique
     *
     * @param dept department information
     * @return result
     */
    public String checkDeptNameUnique(SysDept dept);

    /**
     * Added save department information
     *
     * @param dept department information
     * @return result
     */
    public int insertDept(SysDept dept);

    /**
     * Modify and save department information
     *
     * @param dept department information
     * @return result
     */
    public int updateDept(SysDept dept);

    /**
     * Delete department management information
     *
     * @param deptId department ID
     * @return result
     */
    public int deleteDeptById(Long deptId);
}