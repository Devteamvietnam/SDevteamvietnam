package com.devteamvietnam.quartz.util;

import java.text.ParseException;
import java.util.Date;
import org.quartz.CronExpression;
/**
 * cron expression tool class

 *
 */
public class CronUtils
{
    /**
     * Returns a boolean value representing the validity of a given cron expression
     *
     * @param cronExpression Cron expression
     * @return boolean expression is valid
     */
    public static boolean isValid(String cronExpression)
    {
        return CronExpression.isValidExpression(cronExpression);
    }

    /**
     * Return a string value, indicating that the message is invalid Cron expression gives validity
     *
     * @param cronExpression Cron expression
     * @return String returns the expression error description when it is invalid, and returns null if it is valid
     */
    public static String getInvalidMessage(String cronExpression)
    {
        try
        {
            new CronExpression(cronExpression);
            return null;
        }
        catch (ParseException pe)
        {
            return pe.getMessage();
        }
    }

    /**
     * Return the next execution time according to the given cron expression
     *
     * @param cronExpression Cron expression
     * @return Date Next Cron expression execution time
     */
    public static Date getNextExecution(String cronExpression)
    {
        try
        {
            CronExpression cron = new CronExpression(cronExpression);
            return cron.getNextValidTimeAfter(new Date(System.currentTimeMillis()));
        }
        catch (ParseException e)
        {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}