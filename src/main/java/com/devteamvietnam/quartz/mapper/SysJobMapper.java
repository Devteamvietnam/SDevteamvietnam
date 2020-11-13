package com.devteamvietnam.quartz.mapper;

import java.util.List;

import com.devteamvietnam.quartz.domain.SysJob;

/**
 * Scheduling task information data layer
 */
public interface SysJobMapper
{
    /**
     * Query collection of scheduled task logs
     *
     * @param job scheduling information
     * @return Operation log collection
     */
    public List<SysJob> selectJobList(SysJob job);

    /**
     * Query all scheduled tasks
     *
     * @return Schedule task list
     */
    public List<SysJob> selectJobAll();

    /**
     * Query scheduling task information by scheduling ID
     *
     * @param jobId scheduling ID
     * @return role object information
     */
    public SysJob selectJobById(Long jobId);

    /**
     * Delete scheduling task information by scheduling ID
     *
     * @param jobId scheduling ID
     * @return result
     */
    public int deleteJobById(Long jobId);

    /**
     * Batch delete scheduled task information
     *
     * @param ids ID of the data to be deleted
     * @return result
     */
    public int deleteJobByIds(Long[] ids);

    /**
     * Modify scheduling task information
     *
     * @param job scheduling task information
     * @return result
     */
    public int updateJob(SysJob job);

    /**
     * Added scheduling task information
     *
     * @param job scheduling task information
     * @return result
     */
    public int insertJob(SysJob job);
}
