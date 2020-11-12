package com.devteamvietnam.common.core.controller;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.devteamvietnam.common.constant.HttpStatus;
import com.devteamvietnam.common.core.domain.AjaxResult;
import com.devteamvietnam.common.core.page.PageDomain;
import com.devteamvietnam.common.core.page.TableDataInfo;
import com.devteamvietnam.common.core.page.TableSupport;
import com.devteamvietnam.common.utils.DateUtils;
import com.devteamvietnam.common.utils.StringUtils;
import com.devteamvietnam.common.utils.sql.SqlUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * Web layer general data processing
 *
 * @author Ivan
 */
public class BaseController
{
    protected final Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * The date format string passed from the front desk is automatically converted to Date type
     */
    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        // Date type conversion
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport()
        {
            @Override
            public void setAsText(String text)
            {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * Set request paging data
     */
    protected void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     * Paging data in response to requests
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("Query successful");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * Response returns results
     *
     * @param rows affect the number of rows
     * @return operation result
     */
    protected AjaxResult toAjax(int rows)
    {
        return rows> 0? AjaxResult.success(): AjaxResult.error();
    }

    /**
     * Page jump
     */
    public String redirect(String url)
    {
        return StringUtils.format("redirect:{}", url);
    }
}
