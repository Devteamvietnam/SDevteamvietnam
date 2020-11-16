package com.devteamvietnam.system.mapper;

import java.util.List;

import com.devteamvietnam.system.domain.SysConfig;

/**
 * Parameter configuration data layer
 *
 * @author ivan
 */
public interface SysConfigMapper
{
    /**
     * Query parameter configuration information
     *
     * @param config parameter configuration information
     * @return parameter configuration information
     */
    public SysConfig selectConfig(SysConfig config);

    /**
     * Query parameter configuration list
     *
     * @param config parameter configuration information
     * @return parameter configuration collection
     */
    public List<SysConfig> selectConfigList(SysConfig config);

    /**
     * Query parameter configuration information according to the key name
     *
     * @param configKey parameter key name
     * @return parameter configuration information
     */
    public SysConfig checkConfigKeyUnique(String configKey);

    /**
     * New parameter configuration
     *
     * @param config parameter configuration information
     * @return result
     */
    public int insertConfig(SysConfig config);

    /**
     * Modify parameter configuration
     *
     * @param config parameter configuration information
     * @return result
     */
    public int updateConfig(SysConfig config);

    /**
     * Delete parameter configuration
     *
     * @param configId parameter ID
     * @return result
     */
    public int deleteConfigById(Long configId);

    /**
     * Batch delete parameter information
     *
     * @param configIds ID of the parameter to be deleted
     * @return result
     */
    public int deleteConfigByIds(Long[] configIds);
}
