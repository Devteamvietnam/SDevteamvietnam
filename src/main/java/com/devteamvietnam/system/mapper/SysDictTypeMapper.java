package com.devteamvietnam.system.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.devteamvietnam.common.core.domain.entity.SysDictType;

/**
 * Dictionary table data layer
 *
 * @author ruoyi
 */
@Mapper
public interface SysDictTypeMapper
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
     * Delete dictionary information by dictionary ID
     *
     * @param dictId dictionary ID
     * @return result
     */
    public int deleteDictTypeById(Long dictId);

    /**
     * Batch delete dictionary type information
     *
     * @param dictIds The ID of the dictionary that needs to be deleted
     * @return result
     */
    public int deleteDictTypeByIds(Long[] dictIds);

    /**
     * Added dictionary type information
     *
     * @param dictType dictionary type information
     * @return result
     */
    public int insertDictType(SysDictType dictType);

    /**
     * Modify dictionary type information
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
    public SysDictType checkDictTypeUnique(String dictType);
}
