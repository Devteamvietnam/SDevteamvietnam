package com.devteamvietnam.common.core.domain.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.devteamvietnam.common.annotation.Excel;
import com.devteamvietnam.common.annotation.Excel.ColumnType;
import com.devteamvietnam.common.core.domain.BaseEntity;

/**
 * Role table sys_role
 *
 * @author Ivan
 */
public class SysRole extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Role ID */
    @Excel(name = "role serial number", cellType = ColumnType.NUMERIC)
    private Long roleId;

    /** Role Name */
    @Excel(name = "role name")
    private String roleName;

    /** Role Permissions */
    @Excel(name = "role permissions")
    private String roleKey;

    /** Role sorting */
    @Excel(name = "Role Sorting")
    private String roleSort;

    /** Data scope (1: all data permissions; 2: custom data permissions; 3: data permissions for this department; 4: data permissions for this department and below) */
    @Excel(name = "Data Range", readConverterExp = "1=All data permissions, 2=Custom data permissions, 3=Data permissions in this department, 4=Data permissions in this department and below")
    private String dataScope;

    /** Whether the menu tree selection items are displayed in association (0: parent and child are not displayed in association with each other 1: parent and child are displayed in association with each other) */
    private boolean menuCheckStrictly;

    /** Whether the department tree selection items are displayed in association (0: parent and child are not displayed in association with each other 1: parent and child are displayed in association with each other) */
    private boolean deptCheckStrictly;

    /** Role status (0 normal and 1 disabled) */
    @Excel(name = "role status", readConverterExp = "0=normal, 1=disabled")
    private String status;

    /** Delete flag (0 means existence 2 means deletion) */
    private String delFlag;

    /** Does the user have this role ID does not exist by default */
    private boolean flag = false;

    /** Menu group */
    private Long[] menuIds;

    /** Department group (data authority) */
    private Long[] deptIds;

    public SysRole()
    {

    }

    public SysRole(Long roleId)
    {
        this.roleId = roleId;
    }

    public Long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public boolean isAdmin()
    {
        return isAdmin(this.roleId);
    }

    public static boolean isAdmin(Long roleId)
    {
        return roleId != null && 1L == roleId;
    }

    @NotBlank(message = "The role name cannot be empty")
    @Size(min = 0, max = 30, message = "The length of the role name cannot exceed 30 characters")
    public String getRoleName()
    {
        return roleName;
    }

    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    @NotBlank(message = "Permission character cannot be empty")
    @Size(min = 0, max = 100, message = "Permission character length cannot exceed 100 characters")
    public String getRoleKey()
    {
        return roleKey;
    }

    public void setRoleKey(String roleKey)
    {
        this.roleKey = roleKey;
    }

    @NotBlank(message = "The display order cannot be empty")
    public String getRoleSort()
    {
        return roleSort;
    }

    public void setRoleSort(String roleSort)
    {
        this.roleSort = roleSort;
    }

    public String getDataScope()
    {
        return dataScope;
    }

    public void setDataScope(String dataScope)
    {
        this.dataScope = dataScope;
    }

    public boolean isMenuCheckStrictly()
    {
        return menuCheckStrictly;
    }

    public void setMenuCheckStrictly(boolean menuCheckStrictly)
    {
        this.menuCheckStrictly = menuCheckStrictly;
    }

    public boolean isDeptCheckStrictly()
    {
        return deptCheckStrictly;
    }

    public void setDeptCheckStrictly(boolean deptCheckStrictly)
    {
        this.deptCheckStrictly = deptCheckStrictly;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public boolean isFlag()
    {
        return flag;
    }

    public void setFlag(boolean flag)
    {
        this.flag = flag;
    }

    public Long[] getMenuIds()
    {
        return menuIds;
    }

    public void setMenuIds(Long[] menuIds)
    {
        this.menuIds = menuIds;
    }

    public Long[] getDeptIds()
    {
        return deptIds;
    }

    public void setDeptIds(Long[] deptIds)
    {
        this.deptIds = deptIds;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roleId", getRoleId())
            .append("roleName", getRoleName())
            .append("roleKey", getRoleKey())
            .append("roleSort", getRoleSort())
            .append("dataScope", getDataScope())
            .append("menuCheckStrictly", isMenuCheckStrictly())
            .append("deptCheckStrictly", isDeptCheckStrictly())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}