package com.devteamvietnam.common.utils.uuid;

/**
 * ID generator tools
 *
 * @author Ivan
 */
public class IdUtils
{
    /**
     * Get random UUID
     *
     * @return random UUID
     */
    public static String randomUUID()
    {
        return UUID.randomUUID().toString();
    }

    /**
     * Simplified UUID with the horizontal line removed
     *
     * @return Simplified UUID, remove the horizontal line
     */
    public static String simpleUUID()
    {
        return UUID.randomUUID().toString(true);
    }

    /**
     * Obtain a random UUID and use the better-performing ThreadLocalRandom to generate a UUID
     *
     * @return random UUID
     */
    public static String fastUUID()
    {
        return UUID.fastUUID().toString();
    }

    /**
     * Simplified UUID, remove the horizontal line, use better performance ThreadLocalRandom to generate UUID
     *
     * @return Simplified UUID, remove the horizontal line
     */
    public static String fastSimpleUUID()
    {
        return UUID.fastUUID().toString(true);
    }
}
