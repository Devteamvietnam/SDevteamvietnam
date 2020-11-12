package com.devteamvietnam.common.exception.file;

/**
 * File name size limit exception class
 *
 * @author Ivan
 */
public class FileSizeLimitExceededException extends FileException
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -5157338627200047077L;

	public FileSizeLimitExceededException(long defaultMaxSize)
    {
        super("upload.exceed.maxSize", new Object[] {defaultMaxSize });
    }
}
