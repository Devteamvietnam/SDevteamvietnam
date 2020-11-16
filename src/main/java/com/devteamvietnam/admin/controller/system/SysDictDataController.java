package com.devteamvietnam.admin.controller.system;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
import com.devteamvietnam.common.core.domain.entity.SysDictData;
import com.devteamvietnam.common.core.page.TableDataInfo;
import com.devteamvietnam.common.enums.BusinessType;
import com.devteamvietnam.common.utils.SecurityUtils;
import com.devteamvietnam.common.utils.poi.ExcelUtil;
import com.devteamvietnam.system.service.ISysDictDataService;
import com.devteamvietnam.system.service.ISysDictTypeService;

/**
 * Data dictionary information
 */
@RestController
@RequestMapping("/system/dict/data")
public class SysDictDataController extends BaseController
{
    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private ISysDictTypeService dictTypeService;

    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysDictData dictData)
    {
        startPage();
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return getDataTable(list);
    }

    @Log(title = "Dictionary Data", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:dict:export')")
    @GetMapping("/export")
    public AjaxResult export(SysDictData dictData)
    {
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
        return util.exportExcel(list, "Dictionary data");
    }

    /**
     * Query dictionary data details
     */
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{dictCode}")
    public AjaxResult getInfo(@PathVariable Long dictCode)
    {
        return AjaxResult.success(dictDataService.selectDictDataById(dictCode));
    }

    /**
     * Query dictionary data information according to dictionary type
     */
    @GetMapping(value = "/type/{dictType}")
    public AjaxResult dictType(@PathVariable String dictType)
    {
        return AjaxResult.success(dictTypeService.selectDictDataByType(dictType));
    }

    /**
     * New dictionary type
     */
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "Dictionary Data", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDictData dict)
    {
        dict.setCreateBy(SecurityUtils.getUsername());
        return toAjax(dictDataService.insertDictData(dict));
    }

    /**
     * Modify the saved dictionary type
     */
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "Dictionary Data", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDictData dict)
    {
        dict.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(dictDataService.updateDictData(dict));
    }

    /**
     * Delete dictionary type
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "Dictionary Type", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictCodes}")
    public AjaxResult remove(@PathVariable Long[] dictCodes)
    {
        return toAjax(dictDataService.deleteDictDataByIds(dictCodes));
    }
}
