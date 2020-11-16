package com.devteamvietnam.system.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devteamvietnam.common.constant.UserConstants;
import com.devteamvietnam.common.core.domain.entity.SysDictData;
import com.devteamvietnam.common.core.domain.entity.SysDictType;
import com.devteamvietnam.common.exception.CustomException;
import com.devteamvietnam.common.utils.DictUtils;
import com.devteamvietnam.common.utils.StringUtils;
import com.devteamvietnam.system.mapper.SysDictDataMapper;
import com.devteamvietnam.system.mapper.SysDictTypeMapper;
import com.devteamvietnam.system.service.ISysDictTypeService;

/**
 * Dictionary business layer processing
 *
 * @author ivan
 */
@Service
public class SysDictTypeServiceImpl implements ISysDictTypeService
{
    @Autowired
    private SysDictTypeMapper dictTypeMapper;

    @Autowired
    private SysDictDataMapper dictDataMapper;

    /**
     * When the project starts, initialize the dictionary to the cache
     */
    @PostConstruct
    public void init()
    {
        List<SysDictType> dictTypeList = dictTypeMapper.selectDictTypeAll();
        for (SysDictType dictType: dictTypeList)
        {
            List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType(dictType.getDictType());
            DictUtils.setDictCache(dictType.getDictType(), dictDatas);
        }
    }

    /**
     * Paging query dictionary type according to conditions
     *
     * @param dictType dictionary type information
     * @return dictionary type collection information
     */
    @Override
    public List<SysDictType> selectDictTypeList(SysDictType dictType)
    {
        return dictTypeMapper.selectDictTypeList(dictType);
    }

    /**
     * According to all dictionary types
     *
     * @return dictionary type collection information
     */
    @Override
    public List<SysDictType> selectDictTypeAll()
    {
        return dictTypeMapper.selectDictTypeAll();
    }

    /**
     * Query dictionary data according to dictionary type
     *
     * @param dictType dictionary type
     * @return dictionary data collection information
     */
    @Override
    public List<SysDictData> selectDictDataByType(String dictType)
    {
        List<SysDictData> dictDatas = DictUtils.getDictCache(dictType);
        if (StringUtils.isNotEmpty(dictDatas))
        {
            return dictDatas;
        }
        dictDatas = dictDataMapper.selectDictDataByType(dictType);
        if (StringUtils.isNotEmpty(dictDatas))
        {
            DictUtils.setDictCache(dictType, dictDatas);
            return dictDatas;
        }
        return null;
    }

    /**
     * Query information based on dictionary type ID
     *
     * @param dictId dictionary type ID
     * @return dictionary type
     */
    @Override
    public SysDictType selectDictTypeById(Long dictId)
    {
        return dictTypeMapper.selectDictTypeById(dictId);
    }

    /**
     * Query information according to dictionary type
     *
     * @param dictType dictionary type
     * @return dictionary type
     */
    @Override
    public SysDictType selectDictTypeByType(String dictType)
    {
        return dictTypeMapper.selectDictTypeByType(dictType);
    }

    /**
     * Batch delete dictionary type information
     *
     * @param dictIds The ID of the dictionary that needs to be deleted
     * @return result
     */
    @Override
    public int deleteDictTypeByIds(Long[] dictIds)
    {
        for (Long dictId: dictIds)
        {
            SysDictType dictType = selectDictTypeById(dictId);
            if (dictDataMapper.countDictDataByType(dictType.getDictType())> 0)
            {
                throw new CustomException(String.format("%1$s has been allocated and cannot be deleted", dictType.getDictName()));
            }
        }
        int count = dictTypeMapper.deleteDictTypeByIds(dictIds);
        if (count> 0)
        {
            DictUtils.clearDictCache();
        }
        return count;
    }

    /**
     * Clear cache data
     */
    @Override
    public void clearCache()
    {
        DictUtils.clearDictCache();
    }

    /**
     * Added saving dictionary type information
     *
     * @param dictType dictionary type information
     * @return result
     */
    @Override
    public int insertDictType(SysDictType dictType)
    {
        int row = dictTypeMapper.insertDictType(dictType);
        if (row> 0)
        {
            DictUtils.clearDictCache();
        }
        return row;
    }

    /**
     * Modify and save dictionary type information
     *
     * @param dictType dictionary type information
     * @return result
     */
    @Override
    @Transactional
    public int updateDictType(SysDictType dictType)
    {
        SysDictType oldDict = dictTypeMapper.selectDictTypeById(dictType.getDictId());
        dictDataMapper.updateDictDataType(oldDict.getDictType(), dictType.getDictType());
        int row = dictTypeMapper.updateDictType(dictType);
        if (row> 0)
        {
            DictUtils.clearDictCache();
        }
        return row;
    }

    /**
     * Check whether the dictionary type is unique
     *
     * @param dict dictionary type
     * @return result
     */
    @Override
    public String checkDictTypeUnique(SysDictType dict)
    {
        Long dictId = StringUtils.isNull(dict.getDictId())? -1L: dict.getDictId();
        SysDictType dictType = dictTypeMapper.checkDictTypeUnique(dict.getDictType());
        if (StringUtils.isNotNull(dictType) && dictType.getDictId().longValue() != dictId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }
}