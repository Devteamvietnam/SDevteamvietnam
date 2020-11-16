package com.devteamvietnam.framework.interceptor;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.alibaba.fastjson.JSONObject;
import com.devteamvietnam.common.annotation.RepeatSubmit;
import com.devteamvietnam.common.core.domain.AjaxResult;
import com.devteamvietnam.common.utils.ServletUtils;

/**
 * Prevent repeated submission interceptor
 *
 * 
 */
@Component
public abstract class RepeatSubmitInterceptor extends HandlerInterceptorAdapter
{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        if (handler instanceof HandlerMethod)
        {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            if (annotation != null)
            {
                if (this.isRepeatSubmit(request))
                {
                    AjaxResult ajaxResult = AjaxResult.error("Repeat submission is not allowed, please try again later");
                    ServletUtils.renderString(response, JSONObject.toJSONString(ajaxResult));
                    return false;
                }
            }
            return true;
        }
        else
        {
            return super.preHandle(request, response, handler);
        }
    }

    /**
     * Verify whether duplicate submissions are implemented by subclasses to implement specific anti-duplication rules
     *
     * @param request
     * @return
     * @throws Exception
     */
    public abstract boolean isRepeatSubmit(HttpServletRequest request);
}