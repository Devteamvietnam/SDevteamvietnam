package com.devteamvietnam.admin.controller.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.devteamvietnam.common.config.DevteamConfig;
import com.devteamvietnam.common.constant.Constants;
import com.devteamvietnam.common.core.domain.AjaxResult;
import com.devteamvietnam.common.utils.StringUtils;
import com.devteamvietnam.common.utils.file.FileUploadUtils;
import com.devteamvietnam.common.utils.file.FileUtils;
import com.devteamvietnam.framework.config.ServerConfig;

/**
 * General request processing
 *
 */
@RestController
public class CommonController
{
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ServerConfig serverConfig;

    /**
     * Universal download request
     *
     * @param fileName file name
     * @param delete whether to delete
     */
    @GetMapping("common/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request)
    {
        try
        {
            if (!FileUtils.isValidFilename(fileName))
            {
                throw new Exception(StringUtils.format("File name ({}) is illegal and download is not allowed. ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = DevteamConfig.getDownloadPath() + fileName;

            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, realFileName));
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete)
            {
                FileUtils.deleteFile(filePath);
            }
        }
        catch (Exception e)
        {
            log.error("Failed to download file", e);
        }
    }

    /**
     * Universal upload request
     */
    @PostMapping("/common/upload")
    public AjaxResult uploadFile(MultipartFile file) throws Exception
    {
        try
        {
            // Upload file path
            String filePath = DevteamConfig.getUploadPath();
            // Upload and return the new file name
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("fileName", fileName);
            ajax.put("url", url);
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * General download of local resources
     */
    @GetMapping("/common/download/resource")
    public void resourceDownload(String name, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        // local resource path
        String localPath = DevteamConfig.getProfile();
        // Database resource address
        String downloadPath = localPath + StringUtils.substringAfter(name, Constants.RESOURCE_PREFIX);
        // download name
        String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, downloadName));
        FileUtils.writeBytes(downloadPath, response.getOutputStream());
    }
}
