package com.devteamvietnam.common.utils;

import java.util.Collection;
import java.util.List;

import com.devteamvietnam.common.utils.spring.SpringUtils;

/**
 * Dictionary tools
 *
 * @author Ivan
 */
public class DictUtils
{
    /**
     * Separator
     */
    public static final String SEPARATOR = ",";

    /**
     * Set dictionary cache
     *
     * @param key parameter key
     * @param dictDatas dictionary data list
     */
    public static void setDictCache(String key, List<SysDictData> dictDatas)
    {
        SpringUtils.getBean(RedisCache.class).setCacheObject(getCacheKey(key), dictDatas);
    }

    /**
     * Get dictionary cache
     *
     * @param key parameter key
     * @return dictDatas dictionary data list
     */
    public static List<SysDictData> getDictCache(String key)
    {
        Object cacheObj = SpringUtils.getBean(RedisCache.class).getCacheObject(getCacheKey(key));
        if (StringUtils.isNotNull(cacheObj))
        {
            List<SysDictData> dictDatas = StringUtils.cast(cacheObj);
            return dictDatas;
        }
        return null;
    }

    /**
     * Get dictionary tags according to dictionary type and dictionary value
     *
     * @param dictType dictionary type
     * @param dictValue dictionary value
     * @return dictionary tag
     */
    public static String getDictLabel(String dictType, String dictValue)
    {
        return getDictLabel(dictType, dictValue, SEPARATOR);
    }

    /**
     * Get dictionary value according to dictionary type and dictionary tag
     *
     * @param dictType dictionary type
     * @param dictLabel dictionary label
     * @return dictionary value
     */
    public static String getDictValue(String dictType, String dictLabel)
    {
        return getDictValue(dictType, dictLabel, SEPARATOR);
    }

    /**
     * Get dictionary tags according to dictionary type and dictionary value
     *
     * @param dictType dictionary type
     * @param dictValue dictionary value
     * @param separator separator
     * @return dictionary tag
     */
    public static String getDictLabel(String dictType, String dictValue, String separator)
    {
        StringBuilder propertyString = new StringBuilder();
        List<SysDictData> datas = getDictCache(dictType);

        if (StringUtils.containsAny(separator, dictValue) && StringUtils.isNotEmpty(datas))
        {
            for (SysDictData dict: datas)
            {
                for (String value: dictValue.split(separator))
                {
                    if (value.equals(dict.getDictValue()))
                    {
                        propertyString.append(dict.getDictLabel() + separator);
                        break;
                    }
                }
            }
        }
        else
        {
            for (SysDictData dict: datas)
            {
                if (dictValue.equals(dict.getDictValue()))
                {
                    return dict.getDictLabel();
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * Get dictionary value according to dictionary type and dictionary tag
     *
     * @param dictType dictionary type
     * @param dictLabel dictionary label
     * @param separator separator
     * @return dictionary value
     */
    public static String getDictValue(String dictType, String dictLabel, String separator)
    {
        StringBuilder propertyString = new StringBuilder();
        List<SysDictData> datas = getDictCache(dictType);

        if (StringUtils.containsAny(separator, dictLabel) && StringUtils.isNotEmpty(datas))
        {
            for (SysDictData dict: datas)
            {
                for (String label: dictLabel.split(separator))
                {
                    if (label.equals(dict.getDictLabel()))
                    {
                        propertyString.append(dict.getDictValue() + separator);
                        break;
                    }
                }
            }
        }
        else
        {
            for (SysDictData dict: datas)
            {
                if (dictLabel.equals(dict.getDictLabel()))
                {
                    return dict.getDictValue();
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * Clear dictionary cache
     */
    public static void clearDictCache()
    {
        Collection<String> keys = SpringUtils.getBean(RedisCache.class).keys(Constants.SYS_DICT_KEY + "*");
        SpringUtils.getBean(RedisCache.class).deleteObject(keys);
    }

    /**
     * Set the cache key
     *
     * @param configKey parameter key
     * @return cache key key
     */
    public static String getCacheKey(String configKey)
    {
        return Constants.SYS_DICT_KEY + configKey;
    }
}
