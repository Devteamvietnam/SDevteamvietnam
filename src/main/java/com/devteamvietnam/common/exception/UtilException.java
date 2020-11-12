package com.devteamvietnam.common.exception;

/**
 * Tool abnormal
 *
 * @author Ivan
 */
public class UtilException extends RuntimeException
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -7057608040105636365L;

	public UtilException(Throwable e)
    {
        super(e.getMessage(), e);
    }

    public UtilException(String message)
    {
        super(message);
    }

    public UtilException(String message, Throwable throwable)
    {
        super(message, throwable);
    }
}
