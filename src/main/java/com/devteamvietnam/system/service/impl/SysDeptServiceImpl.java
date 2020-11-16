package com.devteamvietnam.system.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devteamvietnam.common.annotation.DataScope;
import com.devteamvietnam.common.constant.UserConstants;
import com.devteamvietnam.common.core.domain.TreeSelect;
import com.devteamvietnam.common.core.domain.entity.SysDept;
import com.devteamvietnam.common.core.domain.entity.SysRole;
import com.devteamvietnam.common.exception.CustomException;
import com.devteamvietnam.common.utils.StringUtils;
import com.devteamvietnam.system.mapper.SysDeptMapper;
import com.devteamvietnam.system.mapper.SysRoleMapper;
import com.devteamvietnam.system.service.ISysDeptService;

/**
 * Department management service realization
 *
 * @author ivan
 */
@Service
public class SysDeptServiceImpl implements ISysDeptService
{
    @Autowired
    private SysDeptMapper deptMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    /**
     * Query department management data
     *
     * @param dept department information
     * @return department information collection
     */
    @Override
    @DataScope(deptAlias = "d")
    public List<SysDept> selectDeptList(SysDept dept)
    {
        return deptMapper.selectDeptList(dept);
    }


    /**
     * Build the tree structure required by the front end
     *
     * @param depts department list
     * @return tree structure list
     */
    @Override
    public List<SysDept> buildDeptTree(List<SysDept> depts)
    {
        List<SysDept> returnList = new ArrayList<SysDept>();
        List<Long> tempList = new ArrayList<Long>();
        for (SysDept dept: depts)
        {
            tempList.add(dept.getDeptId());
        }
        for (Iterator<SysDept> iterator = depts.iterator(); iterator.hasNext();)
        {
            SysDept dept = (SysDept) iterator.next();
            // If it is a top-level node, traverse all child nodes of the parent node
            if (!tempList.contains(dept.getParentId()))
            {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = depts;
        }
        return returnList;
    }

    /**
     * Build the drop-down tree structure required by the front end
     *
     * @param depts department list
     * @return drop-down tree structure list
     */
    @Override
    public List<TreeSelect> buildDeptTreeSelect(List<SysDept> depts)
    {
        List<SysDept> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * Query department tree information based on role ID
     *
     * @param roleId role ID
     * @return selected department list
     */
    @Override
    public List<Integer> selectDeptListByRoleId(Long roleId)
    {
        SysRole role = roleMapper.selectRoleById(roleId);
        return deptMapper.selectDeptListByRoleId(roleId, role.isDeptCheckStrictly());
    }

    /**
     * Query information based on department ID
     *
     * @param deptId department ID
     * @return department information
     */
    @Override
    public SysDept selectDeptById(Long deptId)
    {
        return deptMapper.selectDeptById(deptId);
    }

    /**
     * Query all sub-departments based on ID (normal state)
     *
     * @param deptId department ID
     * @return Number of sub-departments
     */
    @Override
    public int selectNormalChildrenDeptById(Long deptId)
    {
        return deptMapper.selectNormalChildrenDeptById(deptId);
    }
    /**
     * Whether there are child nodes
     *
     * @param deptId department ID
     * @return result
     */
    @Override
    public boolean hasChildByDeptId(Long deptId)
    {
        int result = deptMapper.hasChildByDeptId(deptId);
        return result> 0? true: false;
    }

    /**
     * Query whether there are users in the department
     *
     * @param deptId department ID
     * @return result true exists false does not exist
     */
    @Override
    public boolean checkDeptExistUser(Long deptId)
    {
        int result = deptMapper.checkDeptExistUser(deptId);
        return result> 0? true: false;
    }

    /**
     * Check whether the department name is unique
     *
     * @param dept department information
     * @return result
     */
    @Override
    public String checkDeptNameUnique(SysDept dept)
    {
        Long deptId = StringUtils.isNull(dept.getDeptId())? -1L: dept.getDeptId();
        SysDept info = deptMapper.checkDeptNameUnique(dept.getDeptName(), dept.getParentId());
        if (StringUtils.isNotNull(info) && info.getDeptId().longValue() != deptId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * Added save department information
     *
     * @param dept department information
     * @return result
     */
    @Override
    public int insertDept(SysDept dept)
    {
        SysDept info = deptMapper.selectDeptById(dept.getParentId());
        // If the parent node is not in the normal state, no new child nodes are allowed
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus()))
        {
            throw new CustomException("Department is disabled, adding is not allowed");
        }
        dept.setAncestors(info.getAncestors() + "," + dept.getParentId());
        return deptMapper.insertDept(dept);
    }

    /**
     * Modify and save department information
     *
     * @param dept department information
     * @return result
     */
    @Override
    public int updateDept(SysDept dept)
    {
        SysDept newParentDept = deptMapper.selectDeptById(dept.getParentId());
        SysDept oldDept = deptMapper.selectDeptById(dept.getDeptId());
        if (StringUtils.isNotNull(newParentDept) && StringUtils.isNotNull(oldDept))
        {
            String newAncestors = newParentDept.getAncestors() + "," + newParentDept.getDeptId();
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors);
        }
        int result = deptMapper.updateDept(dept);
        if (UserConstants.DEPT_NORMAL.equals(dept.getStatus()))
        {
            // If the department is enabled, all superior departments of the department are enabled
            updateParentDeptStatus(dept);
        }
        return result;
    }

    /**
     * Modify the status of the parent department of the department
     *
     * @param dept current department
     */
    private void updateParentDeptStatus(SysDept dept)
    {
        String updateBy = dept.getUpdateBy();
        dept = deptMapper.selectDeptById(dept.getDeptId());
        dept.setUpdateBy(updateBy);
        deptMapper.updateDeptStatus(dept);
    }

    /**
     * Modify the child element relationship
     *
     * @param deptId Department ID being modified
     * @param newAncestors new parent ID collection
     * @param oldAncestors old parent ID collection
     */
    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors)
    {
        List<SysDept> children = deptMapper.selectChildrenDeptById(deptId);
        for (SysDept child: children)
        {
            child.setAncestors(child.getAncestors().replace(oldAncestors, newAncestors));
        }
        if (children.size()> 0)
        {
            deptMapper.updateDeptChildren(children);
        }
    }

    /**
     * Delete department management information
     *
     * @param deptId department ID
     * @return result
     */
    @Override
    public int deleteDeptById(Long deptId)
    {
        return deptMapper.deleteDeptById(deptId);
    }

    /**
     * Recursive list
     */
    private void recursionFn(List<SysDept> list, SysDept t)
    {
        // Get the list of child nodes
        List<SysDept> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDept tChild: childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * Get the list of child nodes
     */
    private List<SysDept> getChildList(List<SysDept> list, SysDept t)
    {
        List<SysDept> tlist = new ArrayList<SysDept>();
        Iterator<SysDept> it = list.iterator();
        while (it.hasNext())
        {
            SysDept n = (SysDept) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getDeptId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * Determine whether there are child nodes
     */
    private boolean hasChild(List<SysDept> list, SysDept t)
    {
        return getChildList(list, t).size()> 0? true: false;
    }
}
