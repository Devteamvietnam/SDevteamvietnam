package com.devteamvietnam.quartz.util;

import org.quartz.JobExecutionContext;

import com.devteamvietnam.quartz.domain.SysJob;

public class QuartzJobExecution extends AbstractQuartzJob
{
    @Override
    protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception
    {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
