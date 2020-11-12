package com.devteamvietnam.common.constant;

/**
 * Common constants for task scheduling
 *
 * @author Ivan
 */
public class ScheduleConstants
{
    public static final String TASK_CLASS_NAME = "TASK_CLASS_NAME";

    /** Execution target key */
    public static final String TASK_PROPERTIES = "TASK_PROPERTIES";

    /** Default */
    public static final String MISFIRE_DEFAULT = "0";

    /** Trigger execution immediately */
    public static final String MISFIRE_IGNORE_MISFIRES = "1";

    /** Trigger one execution */
    public static final String MISFIRE_FIRE_AND_PROCEED = "2";

    /** Execute immediately without triggering */
    public static final String MISFIRE_DO_NOTHING = "3";

    public enum Status
    {
        /**
         * Normal
         */
        NORMAL("0"),
        /**
         * time out
         */
        PAUSE("1");

        private String value;

        private Status(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }
}