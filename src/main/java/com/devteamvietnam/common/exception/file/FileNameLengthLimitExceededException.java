package com.devteamvietnam.common.exception.file;

/**
 * File name overlength limit exception class
 *
 * @author Ivan
 */
public class FileNameLengthLimitExceededException extends FileException
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -4623681384961374424L;

	public FileNameLengthLimitExceededException(int defaultFileNameLength)
    {
        super("upload.filename.exceed.length", new Object[] {defaultFileNameLength });
    }
}
