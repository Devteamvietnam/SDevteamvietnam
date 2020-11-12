package com.devteamvietnam.common.exception.file;

import com.devteamvietnam.common.exception.BaseException;

/**
 * File information abnormal
 *
 * @author Ivan
 */
public class FileException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args)
    {
        super("file", code, args, null);
    }

}
