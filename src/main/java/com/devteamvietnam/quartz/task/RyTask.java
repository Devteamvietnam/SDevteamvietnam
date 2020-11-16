package com.devteamvietnam.quartz.task;

import org.springframework.stereotype.Component;

import com.devteamvietnam.common.utils.StringUtils;

/**
 * Timing task scheduling test
 */
@Component("ryTask")
public class RyTask
{
    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i)
    {
        System.out.println(StringUtils.format("Execute multi-parameter method: string type {}, boolean type {}, long integer type {}, floating point type {}, integer {}", s, b, l, d, i));
    }

    public void ryParams(String params)
    {
        System.out.println("Execute method with parameters:" + params);
    }

    public void ryNoParams()
    {
        System.out.println("Execute method without parameters");
    }
}
