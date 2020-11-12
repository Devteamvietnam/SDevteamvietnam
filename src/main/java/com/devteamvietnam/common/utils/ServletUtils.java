package com.devteamvietnam.common.utils;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.devteamvietnam.common.core.text.Convert;

/**
 * Client tools
 *
 * @author ruoyi
 */
public class ServletUtils
{
    /**
     * Get String parameters
     */
    public static String getParameter(String name)
    {
        return getRequest().getParameter(name);
    }

    /**
     * Get String parameters
     */
    public static String getParameter(String name, String defaultValue)
    {
        return Convert.toStr(getRequest().getParameter(name), defaultValue);
    }

    /**
     * Get Integer parameters
     */
    public static Integer getParameterToInt(String name)
    {
        return Convert.toInt(getRequest().getParameter(name));
    }

    /**
     * Get Integer parameters
     */
    public static Integer getParameterToInt(String name, Integer defaultValue)
    {
        return Convert.toInt(getRequest().getParameter(name), defaultValue);
    }

    /**
     * Get request
     */
    public static HttpServletRequest getRequest()
    {
        return getRequestAttributes().getRequest();
    }

    /**
     * Get response
     */
    public static HttpServletResponse getResponse()
    {
        return getRequestAttributes().getResponse();
    }

    /**
     * Get session
     */
    public static HttpSession getSession()
    {
        return getRequest().getSession();
    }

    public static ServletRequestAttributes getRequestAttributes()
    {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    /**
     * Render the string to the client
     *
     * @param response rendering object
     * @param string The string to be rendered
     * @return null
     */
    public static String renderString(HttpServletResponse response, String string)
    {
        try
        {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Is it an Ajax asynchronous request
     *
     * @param request
     */
    public static boolean isAjaxRequest(HttpServletRequest request)
    {
        String accept = request.getHeader("accept");
        if (accept != null && accept.indexOf("application/json") != -1)
        {
            return true;
        }

        String xRequestedWith = request.getHeader("X-Requested-With");
        if (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1)
        {
            return true;
        }

        String uri = request.getRequestURI();
        if (StringUtils.inStringIgnoreCase(uri, ".json", ".xml"))
        {
            return true;
        }

        String ajax = request.getParameter("__ajax");
        if (StringUtils.inStringIgnoreCase(ajax, "json", "xml"))
        {
            return true;
        }
        return false;
    }
}
