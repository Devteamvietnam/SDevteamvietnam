package com.devteamvietnam.common.utils.bean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Bean tools
 *
 * @author Ivan
 */
public class BeanUtils extends org.springframework.beans.BeanUtils
{
    /** The subscript at the beginning of the attribute name in the Bean method name */
    private static final int BEAN_METHOD_PROP_INDEX = 3;

    /** * Regular expression matching the getter method */
    private static final Pattern GET_PATTERN = Pattern.compile("get(\\p{javaUpperCase}\\w*)");

    /** * Regular expression matching setter method */
    private static final Pattern SET_PATTERN = Pattern.compile("set(\\p{javaUpperCase}\\w*)");

    /**
     * Bean attribute copy tool method.
     *
     * @param dest target object
     * @param src source object
     */
    public static void copyBeanProp(Object dest, Object src)
    {
        try
        {
            copyProperties(src, dest);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Get the setter method of the object.
     *
     * @param obj object
     * List of setter methods for @return objects
     */
    public static List<Method> getSetterMethods(Object obj)
    {
        // List of setter methods
        List<Method> setterMethods = new ArrayList<Method>();

        // Get all methods
        Method[] methods = obj.getClass().getMethods();

        // Find the setter method

        for (Method method: methods)
        {
            Matcher m = SET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 1))
            {
                setterMethods.add(method);
            }
        }
        // Return to the list of setter methods
        return setterMethods;
    }

    /**
     * Get the getter method of the object.
     *
     * @param obj object
     * @return object getter method list
     */

    public static List<Method> getGetterMethods(Object obj)
    {
        // List of getter methods
        List<Method> getterMethods = new ArrayList<Method>();
        // Get all methods
        Method[] methods = obj.getClass().getMethods();
        // Find the getter method
        for (Method method: methods)
        {
            Matcher m = GET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 0))
            {
                getterMethods.add(method);
            }
        }
        // Return the list of getter methods
        return getterMethods;
    }

    /**
     * Check whether the attribute names in the Bean method name are equal. <br>
     * As getName() and setName() are the same attribute names, getName() and setAge() attribute names are different.
     *
     * @param m1 method name 1
     * @param m2 method name 2
     * @return returns true as the attribute name, otherwise it returns false
     */

    public static boolean isMethodPropEquals(String m1, String m2)
    {
        return m1.substring(BEAN_METHOD_PROP_INDEX).equals(m2.substring(BEAN_METHOD_PROP_INDEX));
    }
}
