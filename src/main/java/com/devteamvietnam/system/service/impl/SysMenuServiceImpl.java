package com.devteamvietnam.system.service.impl;

import org.springframework.stereotype.Service;

import com.devteamvietnam.common.constant.UserConstants;
import com.devteamvietnam.common.core.domain.TreeSelect;
import com.devteamvietnam.common.core.domain.entity.SysMenu;
import com.devteamvietnam.common.core.domain.entity.SysRole;
import com.devteamvietnam.common.core.domain.entity.SysUser;
import com.devteamvietnam.common.utils.SecurityUtils;
import com.devteamvietnam.common.utils.StringUtils;
import com.devteamvietnam.system.domain.vo.MetaVo;
import com.devteamvietnam.system.domain.vo.RouterVo;
import com.devteamvietnam.system.mapper.SysMenuMapper;
import com.devteamvietnam.system.mapper.SysRoleMapper;
import com.devteamvietnam.system.mapper.SysRoleMenuMapper;
import com.devteamvietnam.system.service.ISysMenuService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Menu business layer processing
 *
 * @author ivan
 */
@Service
public class SysMenuServiceImpl implements ISysMenuService
{
    public static final String PREMISSION_STRING = "perms[\"{0}\"]";

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    /**
     * According to user query system menu list
     *
     * @param userId user ID
     * @return menu list
     */
    @Override
    public List<SysMenu> selectMenuList(Long userId)
    {
        return selectMenuList(new SysMenu(), userId);
    }

    /**
     * Query system menu list
     *
     * @param menu menu information
     * @return menu list
     */
    @Override
    public List<SysMenu> selectMenuList(SysMenu menu, Long userId)
    {
        List<SysMenu> menuList = null;
        // The administrator displays all menu information
        if (SysUser.isAdmin(userId))
        {
            menuList = menuMapper.selectMenuList(menu);
        }
        else
        {
            menu.getParams().put("userId", userId);
            menuList = menuMapper.selectMenuListByUserId(menu);
        }
        return menuList;
    }

    /**
     * Query permissions based on user ID
     *
     * @param userId user ID
     * @return permission list
     */
    @Override
    public Set<String> selectMenuPermsByUserId(Long userId)
    {
        List<String> perms = menuMapper.selectMenuPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        for (String perm: perms)
        {
            if (StringUtils.isNotEmpty(perm))
            {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }
        return permsSet;
    }

    /**
     * Query menu based on user ID
     *
     * @param userId user name
     * @return menu list
     */
    @Override
    public List<SysMenu> selectMenuTreeByUserId(Long userId)
    {
        List<SysMenu> menus = null;
        if (SecurityUtils.isAdmin(userId))
        {
            menus = menuMapper.selectMenuTreeAll();
        }
        else
        {
            menus = menuMapper.selectMenuTreeByUserId(userId);
        }
        return getChildPerms(menus, 0);
    }

    /**
     * Query menu tree information based on role ID
     *
     * @param roleId role ID
     * @return selected menu list
     */
    @Override
    public List<Integer> selectMenuListByRoleId(Long roleId)
    {
        SysRole role = roleMapper.selectRoleById(roleId);
        return menuMapper.selectMenuListByRoleId(roleId, role.isMenuCheckStrictly());
    }

    /**
     * Build the menu needed for front-end routing
     *
     * @param menus menu list
     * @return route list
     */
    @Override
    public List<RouterVo> buildMenus(List<SysMenu> menus)
    {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu: menus)
        {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache())));
            List<SysMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && cMenus.size()> 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType()))
            {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            }
            else if (isMeunFrame(menu))
            {
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getPath()));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache())));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }
    /**
     * Build the tree structure required by the front end
     *
     * @param menus menu list
     * @return tree structure list
     */
    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus)
    {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        List<Long> tempList = new ArrayList<Long>();
        for (SysMenu dept: menus)
        {
            tempList.add(dept.getMenuId());
        }
        for (Iterator<SysMenu> iterator = menus.iterator(); iterator.hasNext();)
        {
            SysMenu menu = (SysMenu) iterator.next();
            // If it is a top-level node, traverse all child nodes of the parent node
            if (!tempList.contains(menu.getParentId()))
            {
                recursionFn(menus, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = menus;
        }
        return returnList;
    }

    /**
     * Build the drop-down tree structure required by the front end
     *
     * @param menus menu list
     * @return drop-down tree structure list
     */
    @Override
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus)
    {
        List<SysMenu> menuTrees = buildMenuTree(menus);
        return menuTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * Query information based on menu ID
     *
     * @param menuId menu ID
     * @return menu information
     */
    @Override
    public SysMenu selectMenuById(Long menuId)
    {
        return menuMapper.selectMenuById(menuId);
    }

    /**
     * Is there a menu sub-node
     *
     * @param menuId menu ID
     * @return result
     */
    @Override
    public boolean hasChildByMenuId(Long menuId)
    {
        int result = menuMapper.hasChildByMenuId(menuId);
        return result> 0? true: false;
    }

    /**
     * Query the number of menus used
     *
     * @param menuId menu ID
     * @return result
     */
    @Override
    public boolean checkMenuExistRole(Long menuId)
    {
        int result = roleMenuMapper.checkMenuExistRole(menuId);
        return result> 0? true: false;
    }

    /**
     * Added save menu information
     *
     * @param menu menu information
     * @return result
     */
    @Override
    public int insertMenu(SysMenu menu)
    {
        return menuMapper.insertMenu(menu);
    }

    /**
     * Modify and save menu information
     *
     * @param menu menu information
     * @return result
     */
    @Override
    public int updateMenu(SysMenu menu)
    {
        return menuMapper.updateMenu(menu);
    }

    /**
     * Delete menu management information
     *
     * @param menuId menu ID
     * @return result
     */
    @Override
    public int deleteMenuById(Long menuId)
    {
        return menuMapper.deleteMenuById(menuId);
    }

    /**
     * Verify that the menu name is unique
     *
     * @param menu menu information
     * @return result
     */
    @Override
    public String checkMenuNameUnique(SysMenu menu)
    {
        Long menuId = StringUtils.isNull(menu.getMenuId())? -1L: menu.getMenuId();
        SysMenu info = menuMapper.checkMenuNameUnique(menu.getMenuName(), menu.getParentId());
        if (StringUtils.isNotNull(info) && info.getMenuId().longValue() != menuId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
    
    /**
     * Get route name
     *
     * @param menu menu information
     * @return route name
     */
    public String getRouteName(SysMenu menu)
    {
        String routerName = StringUtils.capitalize(menu.getPath());
        // Non-external link and a first-level directory (type is directory)
        if (isMeunFrame(menu))
        {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * Get routing address
     *
     * @param menu menu information
     * @return routing address
     */
    public String getRouterPath(SysMenu menu)
    {
        String routerPath = menu.getPath();
        // Non-external link and a first-level directory (type is directory)
        if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType())
                && UserConstants.NO_FRAME.equals(menu.getIsFrame()))
        {
            routerPath = "/" + menu.getPath();
        }
        // Non-external link and a first-level directory (type is menu)
        else if (isMeunFrame(menu))
        {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * Get component information
     *
     * @param menu menu information
     * @return component information
     */
    public String getComponent(SysMenu menu)
    {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMeunFrame(menu))
        {
            component = menu.getComponent();
        }
        return component;
    }

    /**
     * Whether to jump inside the menu
     *
     * @param menu menu information
     * @return result
     */
    public boolean isMeunFrame(SysMenu menu)
    {
        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsFrame().equals(UserConstants.NO_FRAME);
    }

    /**
     * Get all child nodes according to the ID of the parent node
     *
     * @param list classification table
     * @param parentId The parent node ID passed in
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, int parentId)
    {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext();)
        {
            SysMenu t = (SysMenu) iterator.next();
            // 1. According to a parent node ID passed in, traverse all child nodes of the parent node
            if (t.getParentId() == parentId)
            {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * Recursive list
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t)
    {
        // Get the list of child nodes
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild: childList)
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
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t)
    {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext())
        {
            SysMenu n = (SysMenu) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * Determine whether there are child nodes
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t)
    {
        return getChildList(list, t).size()> 0? true: false;
    }
}