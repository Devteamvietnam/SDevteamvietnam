package com.devteamvietnam.common.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.devteamvietnam.common.utils.spring.SpringUtils;

/**
 * Obtain i18n resource files
 *
 * @author Ivan
 */
public class MessageUtils
{
    /**
     * Obtain the message according to the message key and parameters and delegate to spring messageSource
     *
     * @param code message key
     * @param args parameters
     * @return Get the internationalized translation value
     */
    public static String message(String code, Object... args)
    {
        MessageSource messageSource = SpringUtils.getBean(MessageSource.class);
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
