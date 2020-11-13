package com.devteamvietnam.framework.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javax.annotation.PreDestroy;
/**
 * Ensure that the background thread can be closed when the application exits
 *
 * 
 */
@Component
public class ShutdownManager
{
    private static final Logger logger = LoggerFactory.getLogger("sys-user");

    @PreDestroy
    public void destroy()
    {
        shutdownAsyncManager();
    }

    /**
     * Stop executing tasks asynchronously
     */
    private void shutdownAsyncManager()
    {
        try
        {
            logger.info("====Close background task task thread pool====");
            AsyncManager.me().shutdown();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
    }
}