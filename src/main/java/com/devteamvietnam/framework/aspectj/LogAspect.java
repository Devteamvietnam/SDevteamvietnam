package com.devteamvietnam.framework.aspectj;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import com.alibaba.fastjson.JSON;
import com.devteamvietnam.common.annotation.Log;
import com.devteamvietnam.common.core.domain.model.LoginUser;
import com.devteamvietnam.common.enums.BusinessStatus;
import com.devteamvietnam.common.enums.HttpMethod;
import com.devteamvietnam.common.utils.ServletUtils;
import com.devteamvietnam.common.utils.StringUtils;
import com.devteamvietnam.common.utils.ip.IpUtils;
import com.devteamvietnam.common.utils.spring.SpringUtils;
import com.devteamvietnam.framework.manager.AsyncManager;
import com.devteamvietnam.framework.manager.factory.AsyncFactory;
import com.devteamvietnam.framework.web.service.TokenService;
import com.devteamvietnam.system.domain.SysOperLog;

/**
 * Operation log record processing
 *
 * @author ruoyi
 */
@Aspect
@Component
public class LogAspect
{
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    // Configure weaving point
    @Pointcut("@annotation(com.devteamvietnam.common.annotation.Log)")
    public void logPointCut()
    {
    }

    /**
     * Execute after processing the request
     *
     * @param joinPoint cut point
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult)
    {
        handleLog(joinPoint, null, jsonResult);
    }

    /**
     * Intercept abnormal operations
     *
     * @param joinPoint cut point
     * @param e exception
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e)
    {
        handleLog(joinPoint, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult)
    {
        try
        {
            // get comment
            Log controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null)
            {
                return;
            }

            // Get the current user
            LoginUser loginUser = SpringUtils.getBean(TokenService.class).getLoginUser(ServletUtils.getRequest());

            // *========Database log=========*//
            SysOperLog operLog = new SysOperLog();
            operLog.setStatus(BusinessStatus.SUCCESS.ordinal());
            // requested address
            String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
            operLog.setOperIp(ip);
            // return parameters
            operLog.setJsonResult(JSON.toJSONString(jsonResult));

            operLog.setOperUrl(ServletUtils.getRequest().getRequestURI());
            if (loginUser != null)
            {
                operLog.setOperName(loginUser.getUsername());
            }

            if (e != null)
            {
                operLog.setStatus(BusinessStatus.FAIL.ordinal());
                operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // Set method name
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");
            // Set the request method
            operLog.setRequestMethod(ServletUtils.getRequest().getMethod());
            // Process the parameters on the set annotation
            getControllerMethodDescription(joinPoint, controllerLog, operLog);
            // Save the database
            AsyncManager.me().execute(AsyncFactory.recordOper(operLog));
        }
        catch (Exception exp)
        {
            // Record local exception log
            log.error("==Pre-notification exception==");
            log.error("Exception Information: {}", exp.getMessage());
            exp.printStackTrace();
        }
    }
    /**
     * Get the description of the method in the annotation for the Controller layer annotation
     *
     * @param log log
     * @param operLog operation log
     * @throws Exception
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, SysOperLog operLog) throws Exception
    {
        // set action action
        operLog.setBusinessType(log.businessType().ordinal());
        // set title
        operLog.setTitle(log.title());
        // Set operator category
        operLog.setOperatorType(log.operatorType().ordinal());
        // Do you need to save the request, parameters and values
        if (log.isSaveRequestData())
        {
            // Get parameter information and pass it to the database.
            setRequestValue(joinPoint, operLog);
        }
    }

    /**
     * Get the requested parameters and put them in the log
     *
     * @param operLog operation log
     * @throws Exception
     */
    private void setRequestValue(JoinPoint joinPoint, SysOperLog operLog) throws Exception
    {
        String requestMethod = operLog.getRequestMethod();
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod))
        {
            String params = argsArrayToString(joinPoint.getArgs());
            operLog.setOperParam(StringUtils.substring(params, 0, 2000));
        }
        else
        {
            Map<?, ?> paramsMap = (Map<?, ?>) ServletUtils.getRequest().getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            operLog.setOperParam(StringUtils.substring(paramsMap.toString(), 0, 2000));
        }
    }

    /**
     * Whether there is a comment, get it if it exists
     */
    private Log getAnnotationLog(JoinPoint joinPoint) throws Exception
    {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null)
        {
            return method.getAnnotation(Log.class);
        }
        return null;
    }

    /**
     * Parameter assembly
     */
    private String argsArrayToString(Object[] paramsArray)
    {
        String params = "";
        if (paramsArray != null && paramsArray.length> 0)
        {
            for (int i = 0; i <paramsArray.length; i++)
            {
                if (!isFilterObject(paramsArray[i]))
                {
                    Object jsonObj = JSON.toJSON(paramsArray[i]);
                    params += jsonObj.toString() + "";
                }
            }
        }
        return params.trim();
    }

    /**
     * Determine whether the object needs to be filtered.
     *
     * @param o Object information.
     * @return If it is an object that needs to be filtered, it returns true; otherwise, it returns false.
     */
    public boolean isFilterObject(final Object o)
    {
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse;
    }
}