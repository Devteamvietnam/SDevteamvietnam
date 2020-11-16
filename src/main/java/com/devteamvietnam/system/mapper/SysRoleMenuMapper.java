package com.devteamvietnam.system.mapper;

import java.util.List;

import com.devteamvietnam.system.domain.SysRoleMenu;

/**
 * Role and menu association table data layer
*
* @author ivan
*/
public interface SysRoleMenuMapper
{
   /**
    * Query the number of menus used
    *
    * @param menuId menu ID
    * @return result
    */
   public int checkMenuExistRole(Long menuId);

   /**
    * Delete role and menu association by role ID
    *
    * @param roleId role ID
    * @return result
    */
   public int deleteRoleMenuByRoleId(Long roleId);

   /**
    * Add role menu information in batches
    *
    * @param roleMenuList role menu list
    * @return result
    */
   public int batchRoleMenu(List<SysRoleMenu> roleMenuList);
} 

