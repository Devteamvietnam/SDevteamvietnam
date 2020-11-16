package com.devteamvietnam.generator.mapper;

import java.util.List;

import com.devteamvietnam.generator.domain.GenTable;

/**
 * Business data layer

 */
public interface GenTableMapper
{
    /**
     * Query business list
     *
     * @param genTable business information
     * @return business collection
     */
    public List<GenTable> selectGenTableList(GenTable genTable);

    /**
     * Query database list
     *
     * @param genTable business information
     * @return database table collection
     */
    public List<GenTable> selectDbTableList(GenTable genTable);

    /**
     * Query database list
     *
     * @param tableNames table name group
     * @return database table collection
     */
    public List<GenTable> selectDbTableListByNames(String[] tableNames);

    /**
     * Query form ID business information
     *
     * @param id business ID
     * @return business information
     */
    public GenTable selectGenTableById(Long id);

    /**
     * Query table name business information
     *
     * @param tableName table name
     * @return business information
     */
    public GenTable selectGenTableByName(String tableName);

    /**
     * New business
     *
     * @param genTable business information
     * @return result
     */
    public int insertGenTable(GenTable genTable);

    /**
     * Modify business
     *
     * @param genTable business information
     * @return result
     */
    public int updateGenTable(GenTable genTable);

    /**
     * Delete business in bulk
     *
     * @param ids ID of the data to be deleted
     * @return result
     */
    public int deleteGenTableByIds(Long[] ids);
}
