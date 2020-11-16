package com.devteamvietnam.framework.interceptor.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;
import com.devteamvietnam.common.constant.Constants;
import com.devteamvietnam.common.core.redis.RedisCache;
import com.devteamvietnam.common.filter.RepeatedlyRequestWrapper;
import com.devteamvietnam.common.utils.StringUtils;
import com.devteamvietnam.common.utils.http.HttpHelper;
import com.devteamvietnam.framework.interceptor.RepeatSubmitInterceptor;

/**
 * Determine whether the request URL and data are the same as the last time,
 * If it is the same as last time, the form is repeated. The effective time is within 10 seconds.
 *
 * 
 */
@Component
public class SameUrlDataInterceptor extends RepeatSubmitInterceptor
{
    public final String REPEAT_PARAMS = "repeatParams";

    public final String REPEAT_TIME = "repeatTime";

    // Token custom identification
    @Value("${token.header}")
    private String header;

    @Autowired
    private RedisCache redisCache;

    /**
     * Interval time, unit: second, default 10 seconds
     *
     * Two requests with the same parameters, if the interval is longer than this parameter, the system will not recognize the data as repeated submissions
     */
    private int intervalTime = 10;

    public void setIntervalTime(int intervalTime)
    {
        this.intervalTime = intervalTime;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isRepeatSubmit(HttpServletRequest request)
    {
        String nowParams = "";
        if (request instanceof RepeatedlyRequestWrapper)
        {
            RepeatedlyRequestWrapper repeatedlyRequest = (RepeatedlyRequestWrapper) request;
            nowParams = HttpHelper.getBodyString(repeatedlyRequest);
        }

        // body parameter is empty, get Parameter data
        if (StringUtils.isEmpty(nowParams))
        {
            nowParams = JSONObject.toJSONString(request.getParameterMap());
        }
        Map<String, Object> nowDataMap = new HashMap<String, Object>();
        nowDataMap.put(REPEAT_PARAMS, nowParams);
        nowDataMap.put(REPEAT_TIME, System.currentTimeMillis());

        // Request address (as the key value for storing cache)
        String url = request.getRequestURI();

        // Unique value (use request address without message header)
        String submitKey = request.getHeader(header);
        if (StringUtils.isEmpty(submitKey))
        {
            submitKey = url;
        }

        // Unique identifier (specify key + message header)
        String cache_repeat_key = Constants.REPEAT_SUBMIT_KEY + submitKey;

        Object sessionObj = redisCache.getCacheObject(cache_repeat_key);
        if (sessionObj != null)
        {
            Map<String, Object> sessionMap = (Map<String, Object>) sessionObj;
            if (sessionMap.containsKey(url))
            {
                Map<String, Object> preDataMap = (Map<String, Object>) sessionMap.get(url);
                if (compareParams(nowDataMap, preDataMap) && compareTime(nowDataMap, preDataMap))
                {
                    return true;
                }
            }
        }
        Map<String, Object> cacheMap = new HashMap<String, Object>();
        cacheMap.put(url, nowDataMap);
        redisCache.setCacheObject(cache_repeat_key, cacheMap, intervalTime, TimeUnit.SECONDS);
        return false;
    }

    /**
     * Determine whether the parameters are the same
     */
    private boolean compareParams(Map<String, Object> nowMap, Map<String, Object> preMap)
    {
        String nowParams = (String) nowMap.get(REPEAT_PARAMS);
        String preParams = (String) preMap.get(REPEAT_PARAMS);
        return nowParams.equals(preParams);
    }

    /**
     * Determine the interval between two times
     */
    private boolean compareTime(Map<String, Object> nowMap, Map<String, Object> preMap)
    {
        long time1 = (Long) nowMap.get(REPEAT_TIME);
        long time2 = (Long) preMap.get(REPEAT_TIME);
        if ((time1-time2) <(this.intervalTime * 1000))
        {
            return true;
        }
        return false;
    }
}
