package com.devteamvietnam.common.exception;

import com.devteamvietnam.common.utils.MessageUtils;
import com.devteamvietnam.common.utils.StringUtils;

/**
 * Basic exception
 *
 * @author Ivan
 */
public class BaseException extends RuntimeException
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -8624496260708982892L;

	/**
     * Owned module
     */
    private String module;

    /**
     * error code
     */
    private String code;

    /**
     * The parameter corresponding to the error code
     */
    private Object[] args;

    /**
     * wrong information
     */
    private String defaultMessage;

    public BaseException(String module, String code, Object[] args, String defaultMessage)
    {
        this.module = module;
        this.code = code;
        this.args = args;
        this.defaultMessage = defaultMessage;
    }

    public BaseException(String module, String code, Object[] args)
    {
        this(module, code, args, null);
    }

    public BaseException(String module, String defaultMessage)
    {
        this(module, null, null, defaultMessage);
    }

    public BaseException(String code, Object[] args)
    {
        this(null, code, args, null);
    }

    public BaseException(String defaultMessage)
    {
        this(null, null, null, defaultMessage);
    }

    @Override
    public String getMessage()
    {
        String message = null;
        if (!StringUtils.isEmpty(code))
        {
            message = MessageUtils.message(code, args);
        }
        if (message == null)
        {
            message = defaultMessage;
        }
        return message;
    }

    public String getModule()
    {
        return module;
    }

    public String getCode()
    {
        return code;
    }

    public Object[] getArgs()
    {
        return args;
    }

    public String getDefaultMessage()
    {
        return defaultMessage;
    }
}
