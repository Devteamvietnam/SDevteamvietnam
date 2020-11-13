package com.devteamvietnam.system.domain.vo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Routing configuration information
 *
 * @author ivan
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVo
{
    /**
     * Route name
     */
    private String name;

    /**
     * Routing address
     */
    private String path;

    /**
     * Whether to hide the route, when set to true, the route will not appear in the sidebar
     */
    private boolean hidden;

    /**
     * Redirect address, when noRedirect is set, the route cannot be clicked in the breadcrumb navigation
     */
    private String redirect;

    /**
     * Component address
     */
    private String component;

    /**
     * When you have more than one route declared by children under a route, it will automatically become a nested mode-such as a component page
     */
    private Boolean alwaysShow;

    /**
     * Other elements
     */
    private MetaVo meta;

    /**
     * Sub-route
     */
    private List<RouterVo> children;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public boolean getHidden()
    {
        return hidden;
    }

    public void setHidden(boolean hidden)
    {
        this.hidden = hidden;
    }

    public String getRedirect()
    {
        return redirect;
    }

    public void setRedirect(String redirect)
    {
        this.redirect = redirect;
    }

    public String getComponent()
    {
        return component;
    }

    public void setComponent(String component)
    {
        this.component = component;
    }

    public Boolean getAlwaysShow()
    {
        return alwaysShow;
    }

    public void setAlwaysShow(Boolean alwaysShow)
    {
        this.alwaysShow = alwaysShow;
    }

    public MetaVo getMeta()
    {
        return meta;
    }

    public void setMeta(MetaVo meta)
    {
        this.meta = meta;
    }

    public List<RouterVo> getChildren()
    {
        return children;
    }

    public void setChildren(List<RouterVo> children)
    {
        this.children = children;
    }
}
