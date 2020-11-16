package com.devteamvietnam.admin.controller.system;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
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
import com.devteamvietnam.common.constant.UserConstants;
import com.devteamvietnam.common.core.controller.BaseController;
import com.devteamvietnam.common.core.domain.AjaxResult;
import com.devteamvietnam.common.core.domain.entity.SysDept;
import com.devteamvietnam.common.enums.BusinessType;
import com.devteamvietnam.common.utils.SecurityUtils;
import com.devteamvietnam.common.utils.StringUtils;
import com.devteamvietnam.system.service.ISysDeptService;

/**
 * Department Information

 */
@RestController
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController
{
    @Autowired
    private ISysDeptService deptService;

    /**
     * Get department list
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    public AjaxResult list(SysDept dept)
    {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return AjaxResult.success(depts);
    }

    /**
     * Query department list (exclude nodes)
     */
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list/exclude/{deptId}")
    public AjaxResult excludeChild(@PathVariable(value = "deptId", required = false) Long deptId)
    {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        Iterator<SysDept> it = depts.iterator();
        while (it.hasNext())
        {
            SysDept d = (SysDept) it.next();
            if (d.getDeptId().intValue() == deptId
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + ""))
            {
                it.remove();
            }
        }
        return AjaxResult.success(depts);
    }

    /**
     * Get detailed information based on department number
     */
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    public AjaxResult getInfo(@PathVariable Long deptId)
    {
        return AjaxResult.success(deptService.selectDeptById(deptId));
    }

    /**
     * Get the department drop-down list
     */
    @GetMapping("/treeselect")
    public AjaxResult treeselect(SysDept dept)
    {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return AjaxResult.success(deptService.buildDeptTreeSelect(depts));
    }

    /**
     * Load the corresponding role department list tree
     */
    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
    public AjaxResult roleDeptTreeselect(@PathVariable("roleId") Long roleId)
    {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", deptService.selectDeptListByRoleId(roleId));
        ajax.put("depts", deptService.buildDeptTreeSelect(depts));
        return ajax;
    }

    /**
     * Add department
     */
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @Log(title = "Department Management", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDept dept)
    {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept)))
        {
            return AjaxResult.error("New department'" + dept.getDeptName() + "'Failed, department name already exists");
        }
        dept.setCreateBy(SecurityUtils.getUsername());
        return toAjax(deptService.insertDept(dept));
    }

    /**
     * Modify department
     */
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    @Log(title = "Department Management", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDept dept)
    {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept)))
        {
            return AjaxResult.error("Modify department'" + dept.getDeptName() + "'Failed, department name already exists");
        }
        else if (dept.getParentId().equals(dept.getDeptId()))
        {
            return AjaxResult.error("Modify department'" + dept.getDeptName() + "'Failed, the superior department cannot be oneself");
        }
        else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus())
                && deptService.selectNormalChildrenDeptById(dept.getDeptId())> 0)
        {
            return AjaxResult.error("This department contains sub-departments that are not deactivated!");
        }
        dept.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(deptService.updateDept(dept));
    }

    /**
     * Delete department
     */
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
    @Log(title = "Department Management", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public AjaxResult remove(@PathVariable Long deptId)
    {
        if (deptService.hasChildByDeptId(deptId))
        {
            return AjaxResult.error("There are subordinate departments and it is not allowed to delete");
        }
        if (deptService.checkDeptExistUser(deptId))
        {
            return AjaxResult.error("There are users in the department and they are not allowed to be deleted");
        }
        return toAjax(deptService.deleteDeptById(deptId));
    }
}
