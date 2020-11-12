package com.devteamvietnam.common.utils.file;


import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import com.devteamvietnam.common.config.DevteamConfig;
import com.devteamvietnam.common.constant.Constants;
import com.devteamvietnam.common.exception.file.FileNameLengthLimitExceededException;
import com.devteamvietnam.common.exception.file.FileSizeLimitExceededException;
import com.devteamvietnam.common.exception.file.InvalidExtensionException;
import com.devteamvietnam.common.utils.DateUtils;
import com.devteamvietnam.common.utils.StringUtils;
import com.devteamvietnam.common.utils.uuid.IdUtils;

/**
 * File upload tools
 *
 * @author ruoyi
 */
public class FileUploadUtils
{
    /**
     * Default size 50M
     */
    public static final long DEFAULT_MAX_SIZE = 50 * 1024 * 1024;

    /**
     * The default maximum length of the file name is 100
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;

    /**
     * Default upload address
     */
    private static String defaultBaseDir = DevteamConfig.getProfile();

    public static void setDefaultBaseDir(String defaultBaseDir)
    {
        FileUploadUtils.defaultBaseDir = defaultBaseDir;
    }

    public static String getDefaultBaseDir()
    {
        return defaultBaseDir;
    }

    /**
     * File upload with default configuration
     *
     * @param file uploaded file
     * @return file name
     * @throws Exception
     */
    public static final String upload(MultipartFile file) throws IOException
    {
        try
        {
            return upload(getDefaultBaseDir(), file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        }
        catch (Exception e)
        {
            throw new IOException(e.getMessage(), e);
        }
    }

    /**
     * Upload according to file path
     *
     * @param baseDir relative application base directory
     * @param file uploaded file
     * @return file name
     * @throws IOException
     */
    public static final String upload(String baseDir, MultipartFile file) throws IOException
    {
        try
        {
            return upload(baseDir, file, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        }
        catch (Exception e)
        {
            throw new IOException(e.getMessage(), e);
        }
    }
    
    /**
     * File Upload
     *
     * @param baseDir relative application base directory
     * @param file uploaded file
     * @param allowedExtension upload file type
     * @return returns the name of the uploaded file
     * @throws FileSizeLimitExceededException if the maximum size is exceeded
     * @throws FileNameLengthLimitExceededException File name is too long
     * @throws IOException such as when there is an error in reading and writing files
     * @throws InvalidExtensionException File verification exception
     */
    public static final String upload(String baseDir, MultipartFile file, String[] allowedExtension)
            throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException,
            InvalidExtensionException
    {
        int fileNamelength = file.getOriginalFilename().length();
        if (fileNamelength> FileUploadUtils.DEFAULT_FILE_NAME_LENGTH)
        {
            throw new FileNameLengthLimitExceededException(FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
        }

        assertAllowed(file, allowedExtension);

        String fileName = extractFilename(file);

        File desc = getAbsoluteFile(baseDir, fileName);
        file.transferTo(desc);
        String pathFileName = getPathFileName(baseDir, fileName);
        return pathFileName;
    }

    /**
     * Encoding file name
     */
    public static final String extractFilename(MultipartFile file)
    {
        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
        fileName = DateUtils.datePath() + "/" + IdUtils.fastUUID() + "." + extension;
        return fileName;
    }

    private static final File getAbsoluteFile(String uploadDir, String fileName) throws IOException
    {
        File desc = new File(uploadDir + File.separator + fileName);

        if (!desc.getParentFile().exists())
        {
            desc.getParentFile().mkdirs();
        }
        if (!desc.exists())
        {
            desc.createNewFile();
        }
        return desc;
    }

    private static final String getPathFileName(String uploadDir, String fileName) throws IOException
    {
        int dirLastIndex = DevteamConfig.getProfile().length() + 1;
        String currentDir = StringUtils.substring(uploadDir, dirLastIndex);
        String pathFileName = Constants.RESOURCE_PREFIX + "/" + currentDir + "/" + fileName;
        return pathFileName;
    }
    
    /**
     * File size verification
     *
     * @param file uploaded file
     * @return
     * @throws FileSizeLimitExceededException if the maximum size is exceeded
     * @throws InvalidExtensionException
     */
    public static final void assertAllowed(MultipartFile file, String[] allowedExtension)
            throws FileSizeLimitExceededException, InvalidExtensionException
    {
        long size = file.getSize();
        if (DEFAULT_MAX_SIZE != -1 && size> DEFAULT_MAX_SIZE)
        {
            throw new FileSizeLimitExceededException(DEFAULT_MAX_SIZE / 1024 / 1024);
        }

        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
        if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension))
        {
            if (allowedExtension == MimeTypeUtils.IMAGE_EXTENSION)
            {
                throw new InvalidExtensionException.InvalidImageExtensionException(allowedExtension, extension,
                        fileName);
            }
            else if (allowedExtension == MimeTypeUtils.FLASH_EXTENSION)
            {
                throw new InvalidExtensionException.InvalidFlashExtensionException(allowedExtension, extension,
                        fileName);
            }
            else if (allowedExtension == MimeTypeUtils.MEDIA_EXTENSION)
            {
                throw new InvalidExtensionException.InvalidMediaExtensionException(allowedExtension, extension,
                        fileName);
            }
            else
            {
                throw new InvalidExtensionException(allowedExtension, extension, fileName);
            }
        }

    }

    /**
     * Determine whether the MIME type is an allowed MIME type
     *
     * @param extension
     * @param allowedExtension
     * @return
     */
    public static final boolean isAllowedExtension(String extension, String[] allowedExtension)
    {
        for (String str: allowedExtension)
        {
            if (str.equalsIgnoreCase(extension))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the suffix of the file name
     *
     * @param file form file
     * @return suffix
     */
    public static final String getExtension(MultipartFile file)
    {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension))
        {
            extension = MimeTypeUtils.getExtension(file.getContentType());
        }
        return extension;
    }
}
