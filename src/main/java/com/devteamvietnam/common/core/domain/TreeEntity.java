package com.devteamvietnam.common.core.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Tree base class
 *
 * @author Ivan
 */
public class TreeEntity extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Parent menu name */
    private String parentName;

    /** Parent menu ID */
    private Long parentId;

    /** display order */
    private Integer orderNum;

    /** List of ancestors */
    private String ancestors;

    /** sub-department */
    private List<?> children = new ArrayList<>();

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

    public Integer getOrderNum()
    {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum)
    {
        this.orderNum = orderNum;
    }

    public String getAncestors()
    {
        return ancestors;
    }

    public void setAncestors(String ancestors)
    {
        this.ancestors = ancestors;
    }

    public List<?> getChildren()
    {
        return children;
    }

    public void setChildren(List<?> children)
    {
        this.children = children;
    }
}
