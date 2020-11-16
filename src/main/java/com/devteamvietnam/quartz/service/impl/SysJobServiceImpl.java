package com.devteamvietnam.quartz.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devteamvietnam.common.constant.ScheduleConstants;
import com.devteamvietnam.common.exception.job.TaskException;
import com.devteamvietnam.quartz.domain.SysJob;
import com.devteamvietnam.quartz.mapper.SysJobMapper;
import com.devteamvietnam.quartz.service.ISysJobService;
import com.devteamvietnam.quartz.util.CronUtils;
import com.devteamvietnam.quartz.util.ScheduleUtils;

/**
 * Timing task scheduling information service layer

*/
@Service
public class SysJobServiceImpl implements ISysJobService
{
   @Autowired
   private Scheduler scheduler;

   @Autowired
   private SysJobMapper jobMapper;

   /**
    * When the project is started, the timer is initialized mainly to prevent the manual modification of the database from not being synchronized to the timing task processing (Note: You cannot manually modify the database ID and task group name, otherwise it will cause dirty data)
    */
   @PostConstruct
   public void init() throws SchedulerException, TaskException
   {
       scheduler.clear();
       List<SysJob> jobList = jobMapper.selectJobAll();
       for (SysJob job: jobList)
       {
           ScheduleUtils.createScheduleJob(scheduler, job);
       }
   }

   /**
    * Get the scheduled task list of the quartz scheduler
    *
    * @param job scheduling information
    * @return
    */
   @Override
   public List<SysJob> selectJobList(SysJob job)
   {
       return jobMapper.selectJobList(job);
   }

   /**
    * Query scheduling information by scheduling task ID
    *
    * @param jobId scheduling task ID
    * @return scheduling task object information
    */
   @Override
   public SysJob selectJobById(Long jobId)
   {
       return jobMapper.selectJobById(jobId);
   }

   /**
    * Pause task
    *
    * @param job scheduling information
    */
   @Override
   @Transactional
   public int pauseJob(SysJob job) throws SchedulerException
   {
       Long jobId = job.getJobId();
       String jobGroup = job.getJobGroup();
       job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
       int rows = jobMapper.updateJob(job);
       if (rows> 0)
       {
           scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
       }
       return rows;
   }

   /**
    * Recovery task
    *
    * @param job scheduling information
    */
   @Override
   @Transactional
   public int resumeJob(SysJob job) throws SchedulerException
   {
       Long jobId = job.getJobId();
       String jobGroup = job.getJobGroup();
       job.setStatus(ScheduleConstants.Status.NORMAL.getValue());
       int rows = jobMapper.updateJob(job);
       if (rows> 0)
       {
           scheduler.resumeJob(ScheduleUtils.getJobKey(jobId, jobGroup));
       }
       return rows;
   }

   /**
    * After deleting the task, the corresponding trigger will also be deleted
    *
    * @param job scheduling information
    */
   @Override
   @Transactional
   public int deleteJob(SysJob job) throws SchedulerException
   {
       Long jobId = job.getJobId();
       String jobGroup = job.getJobGroup();
       int rows = jobMapper.deleteJobById(jobId);
       if (rows> 0)
       {
           scheduler.deleteJob(ScheduleUtils.getJobKey(jobId, jobGroup));
       }
       return rows;
   }

   /**
    * Batch delete scheduling information
    *
    * @param jobIds ID of the job to be deleted
    * @return result
    */
   @Override
   @Transactional
   public void deleteJobByIds(Long[] jobIds) throws SchedulerException
   {
       for (Long jobId: jobIds)
       {
           SysJob job = jobMapper.selectJobById(jobId);
           deleteJob(job);
       }
   } 

   /**
    * Task scheduling status modification
    *
    * @param job scheduling information
    */
   @Override
   @Transactional
   public int changeStatus(SysJob job) throws SchedulerException
   {
       int rows = 0;
       String status = job.getStatus();
       if (ScheduleConstants.Status.NORMAL.getValue().equals(status))
       {
           rows = resumeJob(job);
       }
       else if (ScheduleConstants.Status.PAUSE.getValue().equals(status))
       {
           rows = pauseJob(job);
       }
       return rows;
   }

   /**
    * Run the task immediately
    *
    * @param job scheduling information
    */
   @Override
   @Transactional
   public void run(SysJob job) throws SchedulerException
   {
       Long jobId = job.getJobId();
       String jobGroup = job.getJobGroup();
       SysJob properties = selectJobById(job.getJobId());
       // parameters
       JobDataMap dataMap = new JobDataMap();
       dataMap.put(ScheduleConstants.TASK_PROPERTIES, properties);
       scheduler.triggerJob(ScheduleUtils.getJobKey(jobId, jobGroup), dataMap);
   }

   /**
    * New task
    *
    * @param job scheduling information scheduling information
    */
   @Override
   @Transactional
   public int insertJob(SysJob job) throws SchedulerException, TaskException
   {
       job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
       int rows = jobMapper.insertJob(job);
       if (rows> 0)
       {
           ScheduleUtils.createScheduleJob(scheduler, job);
       }
       return rows;
   }

   /**
    * Update the time expression of the task
    *
    * @param job scheduling information
    */
   @Override
   @Transactional
   public int updateJob(SysJob job) throws SchedulerException, TaskException
   {
       SysJob properties = selectJobById(job.getJobId());
       int rows = jobMapper.updateJob(job);
       if (rows> 0)
       {
           updateSchedulerJob(job, properties.getJobGroup());
       }
       return rows;
   }

   /**
    * Update task
    *
    * @param job task object
    * @param jobGroup task group name
    */
   public void updateSchedulerJob(SysJob job, String jobGroup) throws SchedulerException, TaskException
   {
       Long jobId = job.getJobId();
       // Determine if it exists
       JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
       if (scheduler.checkExists(jobKey))
       {
           // To prevent data problems during creation, remove first, and then perform the creation operation
           scheduler.deleteJob(jobKey);
       }
       ScheduleUtils.createScheduleJob(scheduler, job);
   }

   /**
    * Verify that the cron expression is valid
    *
    * @param cronExpression expression
    * @return result
    */
   @Override
   public boolean checkCronExpressionIsValid(String cronExpression)
   {
       return CronUtils.isValid(cronExpression);
   }
}
