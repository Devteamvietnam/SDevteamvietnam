package com.devteamvietnam.common.core.domain;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import com.devteamvietnam.common.core.domain.entity.SysDept;
import com.devteamvietnam.common.core.domain.entity.SysMenu;
import com.fasterxml.jackson.annotation.JsonInclude;
/**
 * Treeselect tree structure entity class
 *
 * @author Ivan
 */
public class TreeSelect implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** Node ID */
    private Long id;

    /** Node name */
    private String label;

    /** Child node */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelect> children;

    public TreeSelect()
    {

    }

    public TreeSelect(SysDept dept)
    {
        this.id = dept.getDeptId();
        this.label = dept.getDeptName();
        this.children = dept.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    public TreeSelect(SysMenu menu)
    {
        this.id = menu.getMenuId();
        this.label = menu.getMenuName();
        this.children = menu.getChildren().stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public List<TreeSelect> getChildren()
    {
        return children;
    }

    public void setChildren(List<TreeSelect> children)
    {
        this.children = children;
    }
}