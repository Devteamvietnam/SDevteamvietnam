package com.devteamvietnam.generator.service;

import java.util.List;
import java.util.Map;

import com.devteamvietnam.generator.domain.GenTable;

/**
 * Business service layer

 */
public interface IGenTableService
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
     * Query business information
     *
     * @param id business ID
     * @return business information
     */
    public GenTable selectGenTableById(Long id);

    /**
     * Modify business
     *
     * @param genTable business information
     * @return result
     */
    public void updateGenTable(GenTable genTable);

    /**
     * Delete business information
     *
     * @param tableIds The ID of the table data to be deleted
     * @return result
     */
    public void deleteGenTableByIds(Long[] tableIds);

    /**
     * Import table structure
     *
     * @param tableList import table list
     */
    public void importGenTable(List<GenTable> tableList);

    /**
     * Preview code
     *
     * @param tableId table number
     * @return preview data list
     */
    public Map<String, String> previewCode(Long tableId);

    /**
     * Generate code (download method)
     *
     * @param tableName table name
     * @return data
     */
    public byte[] downloadCode(String tableName);

    /**
     * Generate code (custom path)
     *
     * @param tableName table name
     * @return data
     */
    public void generatorCode(String tableName);

    /**
     * Synchronize the database
     *
     * @param tableName table name
     */
    public void synchDb(String tableName);

    /**
     * Generate codes in batches (download method)
     *
     * @param tableNames table array
     * @return data
     */
    public byte[] downloadCode(String[] tableNames);

    /**
     * Modify and save parameter verification
     *
     * @param genTable business information
     */
    public void validateEdit(GenTable genTable);
}
