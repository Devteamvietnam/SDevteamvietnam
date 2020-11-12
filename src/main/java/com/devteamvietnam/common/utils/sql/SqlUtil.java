package com.devteamvietnam.common.utils.sql;

import com.devteamvietnam.common.exception.BaseException;
import com.devteamvietnam.common.utils.StringUtils;

/**
 * sql operation tools
 *
 * @author Ivan
 */
public class SqlUtil
{
    /**
     * Only supports letters, numbers, underscores, spaces, commas, decimal points (supports multiple field sorting)
     */
    public static String SQL_PATTERN = "[a-zA-Z0-9_\\ \\,\\.]+";

    /**
     * Check characters to prevent injection bypass
     */
    public static String escapeOrderBySql(String value)
    {
        if (StringUtils.isNotEmpty(value) && !isValidOrderBySql(value))
        {
            throw new BaseException("The parameter does not conform to the specification and cannot be queried");
        }
        return value;
    }

    /**
     * Verify that the order by syntax complies with the specification
     */
    public static boolean isValidOrderBySql(String value)
    {
        return value.matches(SQL_PATTERN);
    }
}