package com.devteamvietnam.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devteamvietnam.system.domain.SysLogininfor;
import com.devteamvietnam.system.mapper.SysLogininforMapper;
import com.devteamvietnam.system.service.ISysLogininforService;

/**
* System access log information service layer processing
*
* @author ivan
*/
@Service
public class SysLogininforServiceImpl implements ISysLogininforService
{

   @Autowired
   private SysLogininforMapper logininforMapper;

   /**
    * Added system login log
    *
    * @param logininfor access log object
    */
   @Override
   public void insertLogininfor(SysLogininfor logininfor)
   {
       logininforMapper.insertLogininfor(logininfor);
   }

   /**
    * Query system login log collection
    *
    * @param logininfor access log object
    * @return login record collection
    */
   @Override
   public List<SysLogininfor> selectLogininforList(SysLogininfor logininfor)
   {
       return logininforMapper.selectLogininforList(logininfor);
   }

   /**
    * Batch delete system login logs
    *
    * @param infoIds ID of the login log to be deleted
    * @return
    */
   @Override
   public int deleteLogininforByIds(Long[] infoIds)
   {
       return logininforMapper.deleteLogininforByIds(infoIds);
   }

   /**
    * Clear system login log
    */
   @Override
   public void cleanLogininfor()
   {
       logininforMapper.cleanLogininfor();
   }
}
