package com.devteamvietnam.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.devteamvietnam.common.core.domain.entity.SysMenu;

/**
 * Menu table data layer
 *
 * @author ivan
 */
public interface SysMenuMapper
{
    /**
     * Query system menu list
     *
     * @param menu menu information
     * @return menu list
     */
    public List<SysMenu> selectMenuList(SysMenu menu);

    /**
     * According to all user permissions
     *
     * @return permission list
     */
    public List<String> selectMenuPerms();

    /**
     * According to user query system menu list
     *
     * @param menu menu information
     * @return menu list
     */
    public List<SysMenu> selectMenuListByUserId(SysMenu menu);

    /**
     * Query permissions based on user ID
     *
     * @param userId user ID
     * @return permission list
     */
    public List<String> selectMenuPermsByUserId(Long userId);

    /**
     * Query menu based on user ID
     *
     * @return menu list
     */
    public List<SysMenu> selectMenuTreeAll();

    /**
     * Query menu based on user ID
     *
     * @param userId user ID
     * @return menu list
     */
    public List<SysMenu> selectMenuTreeByUserId(Long userId);

    /**
     * Query menu tree information based on role ID
     *
     * @param roleId role ID
     * @param menuCheckStrictly whether the menu tree selection item is displayed in association
     * @return selected menu list
     */
    public List<Integer> selectMenuListByRoleId(@Param("roleId") Long roleId, @Param("menuCheckStrictly") boolean menuCheckStrictly);

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
     * @return result
     */
    public int hasChildByMenuId(Long menuId);

    /**
     * Added menu information
     *
     * @param menu menu information
     * @return result
     */
    public int insertMenu(SysMenu menu);

    /**
     * Modify menu information
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
     * @param menuName menu name
     * @param parentId parent menu ID
     * @return result
     */
    public SysMenu checkMenuNameUnique(@Param("menuName") String menuName, @Param("parentId") Long parentId);
}
