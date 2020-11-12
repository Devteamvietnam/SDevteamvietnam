package com.devteamvietnam.common.core.page;

import com.devteamvietnam.common.utils.ServletUtils;

/**
 * Form data processing
 *
 * @author Ivan
 */
public class TableSupport
{
    /**
     * Current record start index
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * Display the number of records per page
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * Sort column
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * Sorting direction "desc" or "asc".
     */
    public static final String IS_ASC = "isAsc";

    /**
     * Encapsulate paging objects
     */
    public static PageDomain getPageDomain()
    {
        PageDomain pageDomain = new PageDomain();
        pageDomain.setPageNum(ServletUtils.getParameterToInt(PAGE_NUM));
        pageDomain.setPageSize(ServletUtils.getParameterToInt(PAGE_SIZE));
        pageDomain.setOrderByColumn(ServletUtils.getParameter(ORDER_BY_COLUMN));
        pageDomain.setIsAsc(ServletUtils.getParameter(IS_ASC));
        return pageDomain;
    }

    public static PageDomain buildPageRequest()
    {
        return getPageDomain();
    }
}
