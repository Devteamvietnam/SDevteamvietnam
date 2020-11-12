package com.devteamvietnam.common.core.domain.entity;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.devteamvietnam.common.core.domain.BaseEntity;

/**
 * Menu permission table sys_menu
 *
 * @author Ivan
 */
public class SysMenu extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Menu ID */
    private Long menuId;

    /** Menu name */
    private String menuName;

    /** Parent menu name */
    private String parentName;

    /** Parent menu ID */
    private Long parentId;

    /** display order */
    private String orderNum;

    /** Routing address */
    private String path;

    /** Component path */
    private String component;

    /** Is it an external link (0 is 1 no) */
    private String isFrame;

    /** Whether to cache (0 cache 1 no cache) */
    private String isCache;

    /** Type (M directory C menu F button) */
    private String menuType;

    /** Display status (0 display 1 hide) */
    private String visible;
    
    /** Menu status (0 display 1 hide) */
    private String status;

    /** Permission string */
    private String perms;

    /** Menu icon */
    private String icon;

    /** Submenu */
    private List<SysMenu> children = new ArrayList<SysMenu>();

    public Long getMenuId()
    {
        return menuId;
    }

    public void setMenuId(Long menuId)
    {
        this.menuId = menuId;
    }

    @NotBlank(message = "Menu name cannot be empty")
    @Size(min = 0, max = 50, message = "The length of the menu name cannot exceed 50 characters")
    public String getMenuName()
    {
        return menuName;
    }

    public void setMenuName(String menuName)
    {
        this.menuName = menuName;
    }

    public String getParentName()
    {
        return parentName;
    }

    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    @NotBlank(message = "The display order cannot be empty")
    public String getOrderNum()
    {
        return orderNum;
    }

    public void setOrderNum(String orderNum)
    {
        this.orderNum = orderNum;
    }

    @Size(min = 0, max = 200, message = "The routing address cannot exceed 200 characters")
    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    @Size(min = 0, max = 200, message = "The component path cannot exceed 255 characters")
    public String getComponent()
    {
        return component;
    }

    public void setComponent(String component)
    {
        this.component = component;
    }

    public String getIsFrame()
    {
        return isFrame;
    }

    public void setIsFrame(String isFrame)
    {
        this.isFrame = isFrame;
    }

    public String getIsCache()
    {
        return isCache;
    }

    public void setIsCache(String isCache)
    {
        this.isCache = isCache;
    }

    @NotBlank(message = "The menu type cannot be empty")
    public String getMenuType()
    {
        return menuType;
    }

    public void setMenuType(String menuType)
    {
        this.menuType = menuType;
    }

    public String getVisible()
    {
        return visible;
    }

    public void setVisible(String visible)
    {
        this.visible = visible;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Size(min = 0, max = 100, message = "The length of the authorization identifier cannot exceed 100 characters")
    public String getPerms()
    {
        return perms;
    }

    public void setPerms(String perms)
    {
        this.perms = perms;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public List<SysMenu> getChildren()
    {
        return children;
    }

    public void setChildren(List<SysMenu> children)
    {
        this.children = children;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("menuId", getMenuId())
            .append("menuName", getMenuName())
            .append("parentId", getParentId())
            .append("orderNum", getOrderNum())
            .append("path", getPath())
            .append("component", getComponent())
            .append("isFrame", getIsFrame())
            .append("IsCache", getIsCache())
            .append("menuType", getMenuType())
            .append("visible", getVisible())
            .append("status ", getStatus())
            .append("perms", getPerms())
            .append("icon", getIcon())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
