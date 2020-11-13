package com.devteamvietnam.system.service.impl;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devteamvietnam.common.annotation.DataSource;
import com.devteamvietnam.common.constant.Constants;
import com.devteamvietnam.common.constant.UserConstants;
import com.devteamvietnam.common.core.redis.RedisCache;
import com.devteamvietnam.common.core.text.Convert;
import com.devteamvietnam.common.enums.DataSourceType;
import com.devteamvietnam.common.exception.CustomException;
import com.devteamvietnam.common.utils.StringUtils;
import com.devteamvietnam.system.domain.SysConfig;
import com.devteamvietnam.system.mapper.SysConfigMapper;
import com.devteamvietnam.system.service.ISysConfigService;

/**
 * Parameter configuration service layer implementation
 *
 * @author ivan
 */
@Service
public class SysConfigServiceImpl implements ISysConfigService
{
    @Autowired
    private SysConfigMapper configMapper;

    @Autowired
    private RedisCache redisCache;

    /**
     * When the project starts, initialize the parameters to the cache
     */
    @PostConstruct
    public void init()
    {
        List<SysConfig> configsList = configMapper.selectConfigList(new SysConfig());
        for (SysConfig config: configsList)
        {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
    }

    /**
     * Query parameter configuration information
     *
     * @param configId parameter configuration ID
     * @return parameter configuration information
     */
    @Override
    @DataSource(DataSourceType.MASTER)
    public SysConfig selectConfigById(Long configId)
    {
        SysConfig config = new SysConfig();
        config.setConfigId(configId);
        return configMapper.selectConfig(config);
    }

    /**
     * Query parameter configuration information according to the key name
     *
     * @param configKey parameter key
     * @return parameter key value
     */
    @Override
    public String selectConfigByKey(String configKey)
    {
        String configValue = Convert.toStr(redisCache.getCacheObject(getCacheKey(configKey)));
        if (StringUtils.isNotEmpty(configValue))
        {
            return configValue;
        }
        SysConfig config = new SysConfig();
        config.setConfigKey(configKey);
        SysConfig retConfig = configMapper.selectConfig(config);
        if (StringUtils.isNotNull(retConfig))
        {
            redisCache.setCacheObject(getCacheKey(configKey), retConfig.getConfigValue());
            return retConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * Query parameter configuration list
     *
     * @param config parameter configuration information
     * @return parameter configuration collection
     */
    @Override
    public List<SysConfig> selectConfigList(SysConfig config)
    {
        return configMapper.selectConfigList(config);
    }

    /**
     * New parameter configuration
     *
     * @param config parameter configuration information
     * @return result
     */
    @Override
    public int insertConfig(SysConfig config)
    {
        int row = configMapper.insertConfig(config);
        if (row> 0)
        {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
        return row;
    }

    /**
     * Modify parameter configuration
     *
     * @param config parameter configuration information
     * @return result
     */
    @Override
    public int updateConfig(SysConfig config)
    {
        int row = configMapper.updateConfig(config);
        if (row> 0)
        {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
        }
        return row;
    }

    /**
     * Batch delete parameter information
     *
     * @param configIds ID of the parameter to be deleted
     * @return result
     */
    @Override
    public int deleteConfigByIds(Long[] configIds)
    {
        for (Long configId: configIds)
        {
            SysConfig config = selectConfigById(configId);
            if (StringUtils.equals(UserConstants.YES, config.getConfigType()))
            {
                throw new CustomException(String.format("Built-in parameter [%1$s] cannot be deleted", config.getConfigKey()));
            }
        }
        int count = configMapper.deleteConfigByIds(configIds);
        if (count> 0)
        {
            Collection<String> keys = redisCache.keys(Constants.SYS_CONFIG_KEY + "*");
            redisCache.deleteObject(keys);
        }
        return count;
    }

    /**
     * Clear cache data
     */
    @Override
    public void clearCache()
    {
        Collection<String> keys = redisCache.keys(Constants.SYS_CONFIG_KEY + "*");
        redisCache.deleteObject(keys);
    }

    /**
     * Verify that the parameter key name is unique
     *
     * @param config parameter configuration information
     * @return result
     */
    @Override
    public String checkConfigKeyUnique(SysConfig config)
    {
        Long configId = StringUtils.isNull(config.getConfigId())? -1L: config.getConfigId();
        SysConfig info = configMapper.checkConfigKeyUnique(config.getConfigKey());
        if (StringUtils.isNotNull(info) && info.getConfigId().longValue() != configId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * Set the cache key
     *
     * @param configKey parameter key
     * @return cache key key
     */
    private String getCacheKey(String configKey)
    {
        return Constants.SYS_CONFIG_KEY + configKey;
    }
}
