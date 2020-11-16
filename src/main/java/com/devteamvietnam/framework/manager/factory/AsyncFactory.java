package com.devteamvietnam.framework.manager.factory;

import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devteamvietnam.common.constant.Constants;
import com.devteamvietnam.common.utils.LogUtils;
import com.devteamvietnam.common.utils.ServletUtils;
import com.devteamvietnam.common.utils.ip.AddressUtils;
import com.devteamvietnam.common.utils.ip.IpUtils;
import com.devteamvietnam.common.utils.spring.SpringUtils;
import com.devteamvietnam.system.domain.SysLogininfor;
import com.devteamvietnam.system.domain.SysOperLog;
import com.devteamvietnam.system.service.ISysLogininforService;
import com.devteamvietnam.system.service.ISysOperLogService;

import eu.bitwalker.useragentutils.UserAgent;

/**
 * Asynchronous factory (for generating tasks)
 *
 * 
 */
public class AsyncFactory
{
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * Record login information
     *
     * @param username username
     * @param status
     * @param message
     * @param args list
     * @return task task
     */
    public static TimerTask recordLogininfor(final String username, final String status, final String message,
            final Object... args)
    {
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        return new TimerTask()
        {
            @Override
            public void run()
            {
                String address = AddressUtils.getRealAddressByIP(ip);
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(address);
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                // print information to log
                sys_user_logger.info(s.toString(), args);
                // Get the client operating system
                String os = userAgent.getOperatingSystem().getName();
                // Get the client browser
                String browser = userAgent.getBrowser().getName();
                // package object
                SysLogininfor logininfor = new SysLogininfor();
                logininfor.setUserName(username);
                logininfor.setIpaddr(ip);
                logininfor.setLoginLocation(address);
                logininfor.setBrowser(browser);
                logininfor.setOs(os);
                logininfor.setMsg(message);
                // log status
                if (Constants.LOGIN_SUCCESS.equals(status) || Constants.LOGOUT.equals(status))
                {
                    logininfor.setStatus(Constants.SUCCESS);
                }
                else if (Constants.LOGIN_FAIL.equals(status))
                {
                    logininfor.setStatus(Constants.FAIL);
                }
                // insert data
                SpringUtils.getBean(ISysLogininforService.class).insertLogininfor(logininfor);
            }
        };
    }

    /**
     * Operation log record
     *
     * @param operLog operation log information
     * @return task task
     */
    public static TimerTask recordOper(final SysOperLog operLog)
    {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                // Remote query operation location
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringUtils.getBean(ISysOperLogService.class).insertOperlog(operLog);
            }
        };
    }
}
