package com.devteamvietnam.system.domain;

import java.util.Date;

import com.devteamvietnam.common.annotation.Excel;
import com.devteamvietnam.common.annotation.Excel.ColumnType;
import com.devteamvietnam.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * System access record table sys_logininfor
 *
 * @author ivan
 */
public class SysLogininfor extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @Excel(name = "serial number", cellType = ColumnType.NUMERIC)
    private Long infoId;

    /** user account */
    @Excel(name = "User Account")
    private String userName;

    /** Login status 0 success 1 failure */
    @Excel(name = "Login status", readConverterExp = "0=success, 1=failure")
    private String status;

    /** Login IP address */
    @Excel(name = "Login address")
    private String ipaddr;

    /** Login location */
    @Excel(name = "Login Location")
    private String loginLocation;

    /** Browser type */
    @Excel(name = "Browser")
    private String browser;

    /** operating system */
    @Excel(name = "Operating System")
    private String os;

    /** Prompt message */
    @Excel(name = "Prompt Message")
    private String msg;

    /** interview time */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "Access time", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date loginTime;

    public Long getInfoId()
    {
        return infoId;
    }

    public void setInfoId(Long infoId)
    {
        this.infoId = infoId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getIpaddr()
    {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr)
    {
        this.ipaddr = ipaddr;
    }

    public String getLoginLocation()
    {
        return loginLocation;
    }

    public void setLoginLocation(String loginLocation)
    {
        this.loginLocation = loginLocation;
    }

    public String getBrowser()
    {
        return browser;
    }

    public void setBrowser(String browser)
    {
        this.browser = browser;
    }

    public String getOs()
    {
        return os;
    }

    public void setOs(String os)
    {
        this.os = os;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public Date getLoginTime()
    {
        return loginTime;
    }

    public void setLoginTime(Date loginTime)
    {
        this.loginTime = loginTime;
    }
}