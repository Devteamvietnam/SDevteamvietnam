package com.devteamvietnam.common.core.domain;

import java.util.HashMap;

import com.devteamvietnam.common.constant.HttpStatus;
import com.devteamvietnam.common.utils.StringUtils;

/**
 * Operation message reminder
 *
 * @author Ivan
 */
public class AjaxResult extends HashMap<String, Object>
{
    private static final long serialVersionUID = 1L;

    /** status code */
    public static final String CODE_TAG = "code";

    /** Return content */
    public static final String MSG_TAG = "msg";

    /** Data object */
    public static final String DATA_TAG = "data";

    /**
     * Initialize a newly created AjaxResult object so that it represents an empty message.
     */
    public AjaxResult()
    {
    }

    /**
     * Initialize a newly created AjaxResult object
     *
     * @param code status code
     * @param msg return content
     */
    public AjaxResult(int code, String msg)
    {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * Initialize a newly created AjaxResult object
     *
     * @param code status code
     * @param msg return content
     * @param data data object
     */
    public AjaxResult(int code, String msg, Object data)
    {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (StringUtils.isNotNull(data))
        {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * Return success message
     *
     * @return success message
     */
    public static AjaxResult success()
    {
        return AjaxResult.success("The operation was successful");
    }

    /**
     * Return successful data
     *
     * @return success message
     */
    public static AjaxResult success(Object data)
    {
        return AjaxResult.success("operation successful", data);
    }

    /**
     * Return success message
     *
     * @param msg return content
     * @return success message
     */
    public static AjaxResult success(String msg)
    {
        return AjaxResult.success(msg, null);
    }

    /**
     * Return success message
     *
     * @param msg return content
     * @param data data object
     * @return success message
     */
    public static AjaxResult success(String msg, Object data)
    {
        return new AjaxResult(HttpStatus.SUCCESS, msg, data);
    }

    /**
     * Return error message
     *
     * @return
     */
    public static AjaxResult error()
    {
        return AjaxResult.error("The operation failed");
    }

    /**
     * Return error message
     *
     * @param msg return content
     * @return warning message
     */
    public static AjaxResult error(String msg)
    {
        return AjaxResult.error(msg, null);
    }

    /**
     * Return error message
     *
     * @param msg return content
     * @param data data object
     * @return warning message
     */
    public static AjaxResult error(String msg, Object data)
    {
        return new AjaxResult(HttpStatus.ERROR, msg, data);
    }

    /**
     * Return error message
     *
     * @param code status code
     * @param msg return content
     * @return warning message
     */
    public static AjaxResult error(int code, String msg)
    {
        return new AjaxResult(code, msg, null);
    }
}