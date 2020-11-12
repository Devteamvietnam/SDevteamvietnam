package com.devteamvietnam.common.utils;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.devteamvietnam.common.constant.HttpStatus;
import com.devteamvietnam.common.exception.CustomException;

/**
 * Security service tools
 *
 * @author Ivan
 */
public class SecurityUtils
{
    /**
     * Get user account
     **/
    public static String getUsername()
    {
        try
        {
            return getLoginUser().getUsername();
        }
        catch (Exception e)
        {
            throw new CustomException("Get user account exception", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Get users
     **/
    public static LoginUser getLoginUser()
    {
        try
        {
            return (LoginUser) getAuthentication().getPrincipal();
        }
        catch (Exception e)
        {
            throw new CustomException("Get user information exception", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Obtain Authentication
     */
    public static Authentication getAuthentication()
    {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Generate BCryptPasswordEncoder password
     *
     * @param password password
     * @return encrypted string
     */
    public static String encryptPassword(String password)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * Determine whether the password is the same
     *
     * @param rawPassword real password
     * @param encodedPassword encrypted characters
     * @return result
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword)
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * Are you an administrator
     *
     * @param userId user ID
     * @return result
     */
    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }
}

