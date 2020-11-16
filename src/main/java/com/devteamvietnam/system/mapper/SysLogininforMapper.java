package com.devteamvietnam.system.mapper;

import java.util.List;

import com.devteamvietnam.system.domain.SysLogininfor;

/**
* System access log information data layer
*
* @author ivan
*/
public interface SysLogininforMapper
{
   /**
    * Added system login log
    *
    * @param logininfor access log object
    */
   public void insertLogininfor(SysLogininfor logininfor);

   /**
    * Query system login log collection
    *
    * @param logininfor access log object
    * @return login record collection
    */
   public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor);

   /**
    * Batch delete system login logs
    *
    * @param infoIds ID of the login log to be deleted
    * @return result
    */
   public int deleteLogininforByIds(Long[] infoIds);

   /**
    * Clear system login log
    *
    * @return result
    */
   public int cleanLogininfor();
}
