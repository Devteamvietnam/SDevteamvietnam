package com.devteamvietnam.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.devteamvietnam.common.core.domain.entity.SysDictData;

/**
 * Dictionary table data layer
 *
 * @author ivan
 */
public interface SysDictDataMapper
{
    /**
     * Paging query dictionary data according to conditions
     *
     * @param dictData dictionary data information
     * @return dictionary data collection information
     */
    public List<SysDictData> selectDictDataList(SysDictData dictData);

    /**
     * Query dictionary data according to dictionary type
     *
     * @param dictType dictionary type
     * @return dictionary data collection information
     */
    public List<SysDictData> selectDictDataByType(String dictType);

    /**
     * Query dictionary data information according to dictionary type and dictionary key value
     *
     * @param dictType dictionary type
     * @param dictValue dictionary key value
     * @return dictionary tag
     */
    public String selectDictLabel(@Param("dictType") String dictType, @Param("dictValue") String dictValue);

    /**
     * Query information based on dictionary data ID
     *
     * @param dictCode dictionary data ID
     * @return dictionary data
     */
    public SysDictData selectDictDataById(Long dictCode);

    /**
     * Query dictionary data
     *
     * @param dictType dictionary type
     * @return dictionary data
     */
    public int countDictDataByType(String dictType);

    /**
     * Delete dictionary data information by dictionary ID
     *
     * @param dictCode dictionary data ID
     * @return result
     */
    public int deleteDictDataById(Long dictCode);

    /**
     * Batch delete dictionary data information
     *
     * @param dictCodes The ID of the dictionary data to be deleted
     * @return result
     */
    public int deleteDictDataByIds(Long[] dictCodes);

    /**
     * Added dictionary data information
     *
     * @param dictData dictionary data information
     * @return result
     */
    public int insertDictData(SysDictData dictData);

    /**
     * Modify dictionary data information
     *
     * @param dictData dictionary data information
     * @return result
     */
    public int updateDictData(SysDictData dictData);

    /**
     * Synchronously modify the dictionary type
     *
     * @param oldDictType old dictionary type
     * @param newDictType New and old dictionary type
     * @return result
     */
    public int updateDictDataType(@Param("oldDictType") String oldDictType, @Param("newDictType") String newDictType);
}
