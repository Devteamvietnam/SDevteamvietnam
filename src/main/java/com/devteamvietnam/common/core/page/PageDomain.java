package com.devteamvietnam.common.core.page;

import com.devteamvietnam.common.utils.StringUtils;

/**
 * Paging data
 *
 * @author ruoyi
 */
public class PageDomain
{
    /** Current record start index */
    private Integer pageNum;

    /** The number of records displayed per page */
    private Integer pageSize;

    /** Sort column */
    private String orderByColumn;

    /** Sorting direction desc or asc */
    private String isAsc = "asc";

    public String getOrderBy()
    {
        if (StringUtils.isEmpty(orderByColumn))
        {
            return "";
        }
        return StringUtils.toUnderScoreCase(orderByColumn) + "" + isAsc;
    }

    public Integer getPageNum()
    {
        return pageNum;
    }

    public void setPageNum(Integer pageNum)
    {
        this.pageNum = pageNum;
    }

    public Integer getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }

    public String getOrderByColumn()
    {
        return orderByColumn;
    }

    public void setOrderByColumn(String orderByColumn)
    {
        this.orderByColumn = orderByColumn;
    }

    public String getIsAsc()
    {
        return isAsc;
    }

    public void setIsAsc(String isAsc)
    {
        this.isAsc = isAsc;
    }
}
