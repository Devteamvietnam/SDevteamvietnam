package com.devteamvietnam.system.service;

import java.util.List;

import com.devteamvietnam.common.core.domain.entity.SysDictData;
import com.devteamvietnam.common.core.domain.entity.SysDictType;

/**
 * Dictionary business layer
 *
 * @author ivan
 */
public interface ISysDictTypeService
{
    /**
     * Paging query dictionary type according to conditions
     *
     * @param dictType dictionary type information
     * @return dictionary type collection information
     */
    public List<SysDictType> selectDictTypeList(SysDictType dictType);

    /**
     * According to all dictionary types
     *
     * @return dictionary type collection information
     */
    public List<SysDictType> selectDictTypeAll();

    /**
     * Query dictionary data according to dictionary type
     *
     * @param dictType dictionary type
     * @return dictionary data collection information
     */
    public List<SysDictData> selectDictDataByType(String dictType);

    /**
     * Query information based on dictionary type ID
     *
     * @param dictId dictionary type ID
     * @return dictionary type
     */
    public SysDictType selectDictTypeById(Long dictId);

    /**
     * Query information according to dictionary type
     *
     * @param dictType dictionary type
     * @return dictionary type
     */
    public SysDictType selectDictTypeByType(String dictType);

    /**
     * Batch delete dictionary information
     *
     * @param dictIds The ID of the dictionary that needs to be deleted
     * @return result
     */
    public int deleteDictTypeByIds(Long[] dictIds);

    /**
     * Clear cache data
     */
    public void clearCache();

    /**
     * Added saving dictionary type information
     *
     * @param dictType dictionary type information
     * @return result
     */
    public int insertDictType(SysDictType dictType);

    /**
     * Modify and save dictionary type information
     *
     * @param dictType dictionary type information
     * @return result
     */
    public int updateDictType(SysDictType dictType);

    /**
     * Check whether the dictionary type is unique
     *
     * @param dictType dictionary type
     * @return result
     */
    public String checkDictTypeUnique(SysDictType dictType);
}
