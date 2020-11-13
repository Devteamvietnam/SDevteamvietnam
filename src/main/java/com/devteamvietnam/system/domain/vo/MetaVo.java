package com.devteamvietnam.system.domain.vo;

/**
 * Route display information
 *
 * @author Ivan
 */
public class MetaVo
{
    /**
     * Set the name of the route displayed in the sidebar and breadcrumbs
     */
    private String title;

    /**
     * Set the icon of the route, corresponding to the path src/assets/icons/svg
     */
    private String icon;

    /**
     * Set to true, it will not be cached by <keep-alive>
     */
    private boolean noCache;

    public MetaVo()
    {
    }

    public MetaVo(String title, String icon)
    {
        this.title = title;
        this.icon = icon;
    }

    public MetaVo(String title, String icon, boolean noCache)
    {
        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
    }

    public boolean isNoCache()
    {
        return noCache;
    }

    public void setNoCache(boolean noCache)
    {
        this.noCache = noCache;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }
}