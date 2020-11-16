package com.devteamvietnam.quartz.controller;

import java.util.List;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devteamvietnam.common.annotation.Log;
import com.devteamvietnam.common.core.controller.BaseController;
import com.devteamvietnam.common.core.domain.AjaxResult;
import com.devteamvietnam.common.core.page.TableDataInfo;
import com.devteamvietnam.common.enums.BusinessType;
import com.devteamvietnam.common.exception.job.TaskException;
import com.devteamvietnam.common.utils.SecurityUtils;
import com.devteamvietnam.common.utils.poi.ExcelUtil;
import com.devteamvietnam.quartz.domain.SysJob;
import com.devteamvietnam.quartz.service.ISysJobService;
import com.devteamvietnam.quartz.util.CronUtils;

/**
 * Scheduling task information operation processing
 */
@RestController
@RequestMapping("/monitor/job")
public class SysJobController extends BaseController
{
    @Autowired
    private ISysJobService jobService;

    /**
     * Query the list of scheduled tasks
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysJob sysJob)
    {
        startPage();
        List<SysJob> list = jobService.selectJobList(sysJob);
        return getDataTable(list);
    }

    /**
     * Export timed task list
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:export')")
    @Log(title = "Timing Task", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysJob sysJob)
    {
        List<SysJob> list = jobService.selectJobList(sysJob);
        ExcelUtil<SysJob> util = new ExcelUtil<SysJob>(SysJob.class);
        return util.exportExcel(list, "Timing Task");
    }

    /**
     * Get detailed information about scheduled tasks
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:query')")
    @GetMapping(value = "/{jobId}")
    public AjaxResult getInfo(@PathVariable("jobId") Long jobId)
    {
        return AjaxResult.success(jobService.selectJobById(jobId));
    }

    /**
     * Added timed tasks
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:add')")
    @Log(title = "Timing Task", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysJob sysJob) throws SchedulerException, TaskException
    {
        if (!CronUtils.isValid(sysJob.getCronExpression()))
        {
            return AjaxResult.error("cron expression is incorrect");
        }
        sysJob.setCreateBy(SecurityUtils.getUsername());
        return toAjax(jobService.insertJob(sysJob));
    }

    /**
     * Modify timed tasks
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:edit')")
    @Log(title = "Timing Task", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysJob sysJob) throws SchedulerException, TaskException
    {
        if (!CronUtils.isValid(sysJob.getCronExpression()))
        {
            return AjaxResult.error("cron expression is incorrect");
        }
        sysJob.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(jobService.updateJob(sysJob));
    }

    /**
     * Timed task status modification
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:changeStatus')")
    @Log(title = "Timing Task", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysJob job) throws SchedulerException
    {
        SysJob newJob = jobService.selectJobById(job.getJobId());
        newJob.setStatus(job.getStatus());
        return toAjax(jobService.changeStatus(newJob));
    }

    /**
     * Scheduled tasks are executed immediately
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:changeStatus')")
    @Log(title = "Timing Task", businessType = BusinessType.UPDATE)
    @PutMapping("/run")
    public AjaxResult run(@RequestBody SysJob job) throws SchedulerException
    {
        jobService.run(job);
        return AjaxResult.success();
    }

    /**
     * Delete scheduled tasks
     */
    @PreAuthorize("@ss.hasPermi('monitor:job:remove')")
    @Log(title = "Timing Task", businessType = BusinessType.DELETE)
    @DeleteMapping("/{jobIds}")
    public AjaxResult remove(@PathVariable Long[] jobIds) throws SchedulerException, TaskException
    {
        jobService.deleteJobByIds(jobIds);
        return AjaxResult.success();
    }
}
