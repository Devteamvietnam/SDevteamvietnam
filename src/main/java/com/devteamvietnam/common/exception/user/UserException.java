package com.devteamvietnam.common.exception.user;

import com.devteamvietnam.common.exception.BaseException;

/**
 * Abnormal user information
 *
 * @author Ivan
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}