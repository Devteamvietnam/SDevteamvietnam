package com.devteamvietnam.system.service;

import java.util.List;
import java.util.Set;

import com.devteamvietnam.common.core.domain.TreeSelect;
import com.devteamvietnam.common.core.domain.entity.SysMenu;
import com.devteamvietnam.system.domain.vo.RouterVo;

/**
 * Menu business layer
 *
 * @author ruoyi
 */
public interface ISysMenuService
{
    /**
     * According to user query system menu list
     *
     * @param userId user ID
     * @return menu list
     */
    public List<SysMenu> selectMenuList(Long userId);

    /**
     * According to user query system menu list
     *
     * @param menu menu information
     * @param userId user ID
     * @return menu list
     */
    public List<SysMenu> selectMenuList(SysMenu menu, Long userId);

    /**
     * Query permissions based on user ID
     *
     * @param userId user ID
     * @return permission list
     */
    public Set<String> selectMenuPermsByUserId(Long userId);

    /**
     * Query menu tree information based on user ID
     *
     * @param userId user ID
     * @return menu list
     */
    public List<SysMenu> selectMenuTreeByUserId(Long userId);

    /**
     * Query menu tree information based on role ID
     *
     * @param roleId role ID
     * @return selected menu list
     */
    public List<Integer> selectMenuListByRoleId(Long roleId);

    /**
     * Build the menu needed for front-end routing
     *
     * @param menus menu list
     * @return route list
     */
    public List<RouterVo> buildMenus(List<SysMenu> menus);

    /**
     * Build the tree structure required by the front end
     *
     * @param menus menu list
     * @return tree structure list
     */
    public List<SysMenu> buildMenuTree(List<SysMenu> menus);

    /**
     * Build the drop-down tree structure required by the front end
     *
     * @param menus menu list
     * @return drop-down tree structure list
     */
    public List<TreeSelect> buildMenuTreeSelect(List<SysMenu> menus);

    /**
     * Query information based on menu ID
     *
     * @param menuId menu ID
     * @return menu information
     */
    public SysMenu selectMenuById(Long menuId);

    /**
     * Is there a menu sub-node
     *
     * @param menuId menu ID
     * @return result true exists false does not exist
     */
    public boolean hasChildByMenuId(Long menuId);

    /**
     * Query whether there is a role in the menu
     *
     * @param menuId menu ID
     * @return result true exists false does not exist
     */
    public boolean checkMenuExistRole(Long menuId);

    /**
     * Added save menu information
     *
     * @param menu menu information
     * @return result
     */
    public int insertMenu(SysMenu menu);

    /**
     * Modify and save menu information
     *
     * @param menu menu information
     * @return result
     */
    public int updateMenu(SysMenu menu);

    /**
     * Delete menu management information
     *
     * @param menuId menu ID
     * @return result
     */
    public int deleteMenuById(Long menuId);

    /**
     * Verify that the menu name is unique
     *
     * @param menu menu information
     * @return result
     */
    public String checkMenuNameUnique(SysMenu menu);
}
