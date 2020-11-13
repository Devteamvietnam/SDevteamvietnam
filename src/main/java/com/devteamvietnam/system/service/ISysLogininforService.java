package com.devteamvietnam.system.service;

import java.util.List;

import com.devteamvietnam.system.domain.SysLogininfor;

/**
 * System access log information service layer
 *
 * @author ivan
 */
public interface ISysLogininforService
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
     * @return
     */
    public int deleteLogininforByIds(Long[] infoIds);

    /**
     * Clear system login log
     */
    public void cleanLogininfor();
}
