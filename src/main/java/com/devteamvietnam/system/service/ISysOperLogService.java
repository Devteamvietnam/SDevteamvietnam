package com.devteamvietnam.system.service;

import java.util.List;

import com.devteamvietnam.system.domain.SysOperLog;

/**
 * Operation log service layer
 *
 * @author ivan
 */
public interface ISysOperLogService
{
    /**
     * Added operation log
     *
     * @param operLog operation log object
     */
    public void insertOperlog(SysOperLog operLog);

    /**
     * Query system operation log collection
     *
     * @param operLog operation log object
     * @return Operation log collection
     */
    public List<SysOperLog> selectOperLogList(SysOperLog operLog);

    /**
     * Batch delete system operation logs
     *
     * @param operIds The ID of the operation log to be deleted
     * @return result
     */
    public int deleteOperLogByIds(Long[] operIds);

    /**
     * Query operation log details
     *
     * @param operId operation ID
     * @return Operation log object
     */
    public SysOperLog selectOperLogById(Long operId);

    /**
     * Clear operation log
     */
    public void cleanOperLog();
}