package com.devteamvietnam.quartz.service;

import java.util.List;

import org.quartz.SchedulerException;

import com.devteamvietnam.common.exception.job.TaskException;
import com.devteamvietnam.quartz.domain.SysJob;

/**
 * Timing task scheduling information information service layer
 */
public interface ISysJobService
{
    /**
     * Get the scheduled tasks of the quartz scheduler
     *
     * @param job scheduling information
     * @return scheduling task collection
     */
    public List<SysJob> selectJobList(SysJob job);

    /**
     * Query scheduling information by scheduling task ID
     *
     * @param jobId scheduling task ID
     * @return scheduling task object information
     */
    public SysJob selectJobById(Long jobId);

    /**
     * Pause task
     *
     * @param job scheduling information
     * @return result
     */
    public int pauseJob(SysJob job) throws SchedulerException;

    /**
     * Recovery task
     *
     * @param job scheduling information
     * @return result
     */
    public int resumeJob(SysJob job) throws SchedulerException;

    /**
     * After deleting the task, the corresponding trigger will also be deleted
     *
     * @param job scheduling information
     * @return result
     */
    public int deleteJob(SysJob job) throws SchedulerException;

    /**
     * Batch delete scheduling information
     *
     * @param jobIds ID of the job to be deleted
     * @return result
     */
    public void deleteJobByIds(Long[] jobIds) throws SchedulerException;

    /**
     * Task scheduling status modification
     *
     * @param job scheduling information
     * @return result
     */
    public int changeStatus(SysJob job) throws SchedulerException;

    /**
     * Run the task immediately
     *
     * @param job scheduling information
     * @return result
     */
    public void run(SysJob job) throws SchedulerException;

    /**
     * New task
     *
     * @param job scheduling information
     * @return result
     */
    public int insertJob(SysJob job) throws SchedulerException, TaskException;

    /**
     * Update task
     *
     * @param job scheduling information
     * @return result
     */
    public int updateJob(SysJob job) throws SchedulerException, TaskException;

    /**
     * Verify that the cron expression is valid
     *
     * @param cronExpression expression
     * @return result
     */
    public boolean checkCronExpressionIsValid(String cronExpression);
}
