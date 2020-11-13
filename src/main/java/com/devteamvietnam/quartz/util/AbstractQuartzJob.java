package com.devteamvietnam.quartz.util;

import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devteamvietnam.common.constant.Constants;
import com.devteamvietnam.common.constant.ScheduleConstants;
import com.devteamvietnam.common.utils.ExceptionUtil;
import com.devteamvietnam.common.utils.StringUtils;
import com.devteamvietnam.common.utils.bean.BeanUtils;
import com.devteamvietnam.common.utils.spring.SpringUtils;
import com.devteamvietnam.quartz.domain.SysJob;
import com.devteamvietnam.quartz.domain.SysJobLog;
import com.devteamvietnam.quartz.service.ISysJobLogService;
/**
 * Abstract quartz call

 */
public abstract class AbstractQuartzJob implements Job
{
    private static final Logger log = LoggerFactory.getLogger(AbstractQuartzJob.class);

    /**
     * Thread local variables
     */
    private static ThreadLocal<Date> threadLocal = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException
    {
        SysJob sysJob = new SysJob();
        BeanUtils.copyBeanProp(sysJob, context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES));
        try
        {
            before(context, sysJob);
            if (sysJob != null)
            {
                doExecute(context, sysJob);
            }
            after(context, sysJob, null);
        }
        catch (Exception e)
        {
            log.error("Task execution exception -:", e);
            after(context, sysJob, e);
        }
    }

    /**
     * Before execution
     *
     * @param context work execution context object
     * @param sysJob system scheduled task
     */
    protected void before(JobExecutionContext context, SysJob sysJob)
    {
        threadLocal.set(new Date());
    }

    /**
     * After execution
     *
     * @param context work execution context object
     * @param sysJob system scheduled task
     */
    protected void after(JobExecutionContext context, SysJob sysJob, Exception e)
    {
        Date startTime = threadLocal.get();
        threadLocal.remove();

        final SysJobLog sysJobLog = new SysJobLog();
        sysJobLog.setJobName(sysJob.getJobName());
        sysJobLog.setJobGroup(sysJob.getJobGroup());
        sysJobLog.setInvokeTarget(sysJob.getInvokeTarget());
        sysJobLog.setStartTime(startTime);
        sysJobLog.setStopTime(new Date());
        long runMs = sysJobLog.getStopTime().getTime()-sysJobLog.getStartTime().getTime();
        sysJobLog.setJobMessage(sysJobLog.getJobName() + "Total time:" + runMs + "milliseconds");
        if (e != null)
        {
            sysJobLog.setStatus(Constants.FAIL);
            String errorMsg = StringUtils.substring(ExceptionUtil.getExceptionMessage(e), 0, 2000);
            sysJobLog.setExceptionInfo(errorMsg);
        }
        else
        {
            sysJobLog.setStatus(Constants.SUCCESS);
        }

        // Write to the database
        SpringUtils.getBean(ISysJobLogService.class).addJobLog(sysJobLog);
    }

    /**
     * Execution method, overloaded by subclasses
     *
     * @param context work execution context object
     * @param sysJob system scheduled task
     * @throws Exception exception during execution
     */
    protected abstract void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception;
}
