package com.devteamvietnam.system.mapper;

import com.devteamvietnam.common.core.domain.entity.SysDept;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * Department management data layer
 *
 * @author ivan
 */
public interface SysDeptMapper
{
    /**
     * Query department management data
     *
     * @param dept department information
     * @return department information collection
     */
    public List<SysDept> selectDeptList(SysDept dept);

    /**
     * Query department tree information based on role ID
     *
     * @param roleId role ID
     * @param deptCheckStrictly whether the department tree selection item is displayed in association
     * @return selected department list
     */
    public List<Integer> selectDeptListByRoleId(@Param("roleId") Long roleId, @Param("deptCheckStrictly") boolean deptCheckStrictly);

    /**
     * Query information based on department ID
     *
     * @param deptId department ID
     * @return department information
     */
    public SysDept selectDeptById(Long deptId);

    /**
     * Query all sub-departments based on ID
     *
     * @param deptId department ID
     * @return Department list
     */
    public List<SysDept> selectChildrenDeptById(Long deptId);

    /**
     * Query all sub-departments based on ID (normal state)
     *
     * @param deptId department ID
     * @return Number of sub-departments
     */
    public int selectNormalChildrenDeptById(Long deptId);

    /**
     * Whether there are child nodes
     *
     * @param deptId department ID
     * @return result
     */
    public int hasChildByDeptId(Long deptId);

    /**
     * Query whether there are users in the department
     *
     * @param deptId department ID
     * @return result
     */
    public int checkDeptExistUser(Long deptId);

    /**
     * Check whether the department name is unique
     *
     * @param deptName department name
     * @param parentId parent department ID
     * @return result
     */
    public SysDept checkDeptNameUnique(@Param("deptName") String deptName, @Param("parentId") Long parentId);

    /**
     * Add department information
     *
     * @param dept department information
     * @return result
     */
    public int insertDept(SysDept dept);

    /**
     * Modify department information
     *
     * @param dept department information
     * @return result
     */
    public int updateDept(SysDept dept);

    /**
     * Modify the status of the parent department of the department
     *
     * @param dept department
     */
    public void updateDeptStatus(SysDept dept);

    /**
     * Modify the child element relationship
     *
     * @param depts child element
     * @return result
     */
    public int updateDeptChildren(@Param("depts") List<SysDept> depts);

    /**
     * Delete department management information
     *
     * @param deptId department ID
     * @return result
     */
    public int deleteDeptById(Long deptId);
}
