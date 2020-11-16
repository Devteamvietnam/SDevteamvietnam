package com.devteamvietnam.quartz.domain;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.devteamvietnam.common.annotation.Excel;
import com.devteamvietnam.common.annotation.Excel.ColumnType;
import com.devteamvietnam.common.constant.ScheduleConstants;
import com.devteamvietnam.common.core.domain.BaseEntity;
import com.devteamvietnam.common.utils.StringUtils;
import com.devteamvietnam.quartz.util.CronUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Timing task scheduling table sys_job
 */
public class SysJob extends BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** Task ID */
    @Excel(name = "task number", cellType = ColumnType.NUMERIC)
    private Long jobId;

    /** mission name */
    @Excel(name = "task name")
    private String jobName;

    /** Task group name */
    @Excel(name = "task group name")
    private String jobGroup;

    /** Call the target string */
    @Excel(name = "call target string")
    private String invokeTarget;

    /** cron execution expression */
    @Excel(name = "execute expression")
    private String cronExpression;

    /** cron planning strategy */
    @Excel(name = "Planning Strategy", readConverterExp = "0=default, 1=trigger execution immediately, 2=trigger once execution, 3=not trigger immediate execution")
    private String misfirePolicy = ScheduleConstants.MISFIRE_DEFAULT;

    /** Whether to execute concurrently (0 allowed 1 prohibited) */
    @Excel(name = "Concurrent execution", readConverterExp = "0=allow, 1=prohibit")
    private String concurrent;

    /** Task status (0 normal 1 pause) */
    @Excel(name = "task status", readConverterExp = "0=normal, 1=suspended")
    private String status;

    public Long getJobId()
    {
        return jobId;
    }

    public void setJobId(Long jobId)
    {
        this.jobId = jobId;
    }

    @NotBlank(message = "Task name cannot be empty")
    @Size(min = 0, max = 64, message = "Task name cannot exceed 64 characters")
    public String getJobName()
    {
        return jobName;
    }

    public void setJobName(String jobName)
    {
        this.jobName = jobName;
    }

    public String getJobGroup()
    {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup)
    {
        this.jobGroup = jobGroup;
    }

    @NotBlank(message = "Call target string cannot be empty")
    @Size(min = 0, max = 500, message = "The length of the target string cannot exceed 500 characters")
    public String getInvokeTarget()
    {
        return invokeTarget;
    }

    public void setInvokeTarget(String invokeTarget)
    {
        this.invokeTarget = invokeTarget;
    }

    @NotBlank(message = "Cron execution expression cannot be empty")
    @Size(min = 0, max = 255, message = "Cron execution expression cannot exceed 255 characters")
    public String getCronExpression()
    {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression)
    {
        this.cronExpression = cronExpression;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getNextValidTime()
    {
        if (StringUtils.isNotEmpty(cronExpression))
        {
            return CronUtils.getNextExecution(cronExpression);
        }
        return null;
    }

    public String getMisfirePolicy()
    {
        return misfirePolicy;
    }

    public void setMisfirePolicy(String misfirePolicy)
    {
        this.misfirePolicy = misfirePolicy;
    }

    public String getConcurrent()
    {
        return concurrent;
    }

    public void setConcurrent(String concurrent)
    {
        this.concurrent = concurrent;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("jobId", getJobId())
            .append("jobName", getJobName())
            .append("jobGroup", getJobGroup())
            .append("cronExpression", getCronExpression())
            .append("nextValidTime", getNextValidTime())
            .append("misfirePolicy", getMisfirePolicy())
            .append("concurrent", getConcurrent())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}