package com.devteamvietnam.generator.util;

import java.util.Properties;

import org.apache.velocity.app.Velocity;

import com.devteamvietnam.common.constant.Constants;

/**
 * VelocityEngine factory
 */
public class VelocityInitializer
{
    /**
     * Initialize the vm method
     */
    public static void initVelocity()
    {
        Properties p = new Properties();
        try
        {
            // Load the vm file in the classpath directory
            p.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            // Define character set
            p.setProperty(Velocity.ENCODING_DEFAULT, Constants.UTF8);
            p.setProperty(Velocity.OUTPUT_ENCODING, Constants.UTF8);
            // Initialize the Velocity engine, specify the configuration Properties
            Velocity.init(p);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
