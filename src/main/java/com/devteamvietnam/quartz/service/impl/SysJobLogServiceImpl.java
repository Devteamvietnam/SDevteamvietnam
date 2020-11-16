package com.devteamvietnam.quartz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devteamvietnam.quartz.domain.SysJobLog;
import com.devteamvietnam.quartz.mapper.SysJobLogMapper;
import com.devteamvietnam.quartz.service.ISysJobLogService;

/**
 * Scheduled task scheduling log information service layer

 */

@Service
public class SysJobLogServiceImpl implements ISysJobLogService
{
    @Autowired
    private SysJobLogMapper jobLogMapper;

    /**
     * Obtain the scheduled task of the quartz scheduler log
     *
     * @param jobLog scheduling log information
     * @return scheduling task log collection
     */
    @Override
    public List<SysJobLog> selectJobLogList(SysJobLog jobLog)
    {
        return jobLogMapper.selectJobLogList(jobLog);
    }

    /**
     * Query scheduling information by scheduling task log ID
     *
     * @param jobLogId scheduling task log ID
     * @return Schedule task log object information
     */
    @Override
    public SysJobLog selectJobLogById(Long jobLogId)
    {
        return jobLogMapper.selectJobLogById(jobLogId);
    }

    /**
     * Added task log
     *
     * @param jobLog scheduling log information
     */
    @Override
    public void addJobLog(SysJobLog jobLog)
    {
        jobLogMapper.insertJobLog(jobLog);
    }

    /**
     * Batch delete scheduling log information
     *
     * @param logIds ID of the data to be deleted
     * @return result
     */
    @Override
    public int deleteJobLogByIds(Long[] logIds)
    {
        return jobLogMapper.deleteJobLogByIds(logIds);
    }

    /**
     * Delete task log
     *
     * @param jobId scheduling log ID
     */
    @Override
    public int deleteJobLogById(Long jobId)
    {
        return jobLogMapper.deleteJobLogById(jobId);
    }

    /**
     * Clear task log
     */
    @Override
    public void cleanJobLog()
    {
        jobLogMapper.cleanJobLog();
    }
}
