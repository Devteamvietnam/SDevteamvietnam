package com.devteamvietnam.quartz.service;

import java.util.List;

import com.devteamvietnam.quartz.domain.SysJobLog;

/**
 * Scheduled task scheduling log information service layer
 */
public interface ISysJobLogService
{
    /**
     * Obtain the scheduled task of the quartz scheduler log
     *
     * @param jobLog scheduling log information
     * @return scheduling task log collection
     */
    public List<SysJobLog> selectJobLogList(SysJobLog jobLog);

    /**
     * Query scheduling information by scheduling task log ID
     *
     * @param jobLogId scheduling task log ID
     * @return Schedule task log object information
     */
    public SysJobLog selectJobLogById(Long jobLogId);

    /**
     * Added task log
     *
     * @param jobLog scheduling log information
     */
    public void addJobLog(SysJobLog jobLog);

    /**
     * Batch delete scheduling log information
     *
     * @param logIds The log ID to be deleted
     * @return result
     */
    public int deleteJobLogByIds(Long[] logIds);

    /**
     * Delete task log
     *
     * @param jobId scheduling log ID
     * @return result
     */
    public int deleteJobLogById(Long jobId);

    /**
     * Clear task log
     */
    public void cleanJobLog();
}
