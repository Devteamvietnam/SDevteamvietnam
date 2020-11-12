package com.devteamvietnam.common.utils;

/**
 * Process and record log files
 *
 * @author Ivan
 */
public class LogUtils
{
    public static String getBlock(Object msg)
    {
        if (msg == null)
        {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }
}

