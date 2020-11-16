package com.devteamvietnam.quartz.util;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

import com.devteamvietnam.common.constant.ScheduleConstants;
import com.devteamvietnam.common.exception.job.TaskException;
import com.devteamvietnam.common.exception.job.TaskException.Code;
import com.devteamvietnam.quartz.domain.SysJob;
/**
 * Timing task tools
 *
 */
public class ScheduleUtils
{
    /**
     * Get the quartz task class
     *
     * @param sysJob execution plan
     * @return Specific execution task class
     */
    private static Class<? extends Job> getQuartzJobClass(SysJob sysJob)
    {
        boolean isConcurrent = "0".equals(sysJob.getConcurrent());
        return isConcurrent? QuartzJobExecution.class: QuartzDisallowConcurrentExecution.class;
    }

    /**
     * Build task trigger object
     */
    public static TriggerKey getTriggerKey(Long jobId, String jobGroup)
    {
        return TriggerKey.triggerKey(ScheduleConstants.TASK_CLASS_NAME + jobId, jobGroup);
    }

    /**
     * Build task key object
     */
    public static JobKey getJobKey(Long jobId, String jobGroup)
    {
        return JobKey.jobKey(ScheduleConstants.TASK_CLASS_NAME + jobId, jobGroup);
    }

    /**
     * Create timed tasks
     */
    public static void createScheduleJob(Scheduler scheduler, SysJob job) throws SchedulerException, TaskException
    {
        Class<? extends Job> jobClass = getQuartzJobClass(job);
        // Build job information
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(getJobKey(jobId, jobGroup)).build();

        // Expression scheduling builder
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
        cronScheduleBuilder = handleCronScheduleMisfirePolicy(job, cronScheduleBuilder);

        // Construct a new trigger according to the new cronExpression expression
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(jobId, jobGroup))
                .withSchedule(cronScheduleBuilder).build();

        // Put in the parameters, the method can be obtained at runtime
        jobDetail.getJobDataMap().put(ScheduleConstants.TASK_PROPERTIES, job);

        // Determine if it exists
        if (scheduler.checkExists(getJobKey(jobId, jobGroup)))
        {
            // To prevent data problems during creation, remove first, and then perform the creation operation
            scheduler.deleteJob(getJobKey(jobId, jobGroup));
        }

        scheduler.scheduleJob(jobDetail, trigger);

        // Pause the task
        if (job.getStatus().equals(ScheduleConstants.Status.PAUSE.getValue()))
        {
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
    }

    /**
     * Set the timing task strategy
     */
    public static CronScheduleBuilder handleCronScheduleMisfirePolicy(SysJob job, CronScheduleBuilder cb)
            throws TaskException
    {
        switch (job.getMisfirePolicy())
        {
            case ScheduleConstants.MISFIRE_DEFAULT:
                return cb;
            case ScheduleConstants.MISFIRE_IGNORE_MISFIRES:
                return cb.withMisfireHandlingInstructionIgnoreMisfires();
            case ScheduleConstants.MISFIRE_FIRE_AND_PROCEED:
                return cb.withMisfireHandlingInstructionFireAndProceed();
            case ScheduleConstants.MISFIRE_DO_NOTHING:
                return cb.withMisfireHandlingInstructionDoNothing();
            default:
                throw new TaskException("The task misfire policy'" + job.getMisfirePolicy()
                        + "'cannot be used in cron schedule tasks", Code.CONFIG_ERROR);
        }
    }
}
