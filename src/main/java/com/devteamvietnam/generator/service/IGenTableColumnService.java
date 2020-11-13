package com.devteamvietnam.generator.service;

import java.util.List;

import com.devteamvietnam.generator.domain.GenTableColumn;

/**
 * Business field Service layer
 */
public interface IGenTableColumnService
{
    /**
     * Query the list of business fields
     *
     * @param tableId business field number
     * @return business field collection
     */
    public List<GenTableColumn> selectGenTableColumnListByTableId(Long tableId);

    /**
     * Added business fields
     *
     * @param genTableColumn business field information
     * @return result
     */
    public int insertGenTableColumn(GenTableColumn genTableColumn);

    /**
     * Modify business fields
     *
     * @param genTableColumn business field information
     * @return result
     */
    public int updateGenTableColumn(GenTableColumn genTableColumn);

    /**
     * Delete business field information
     *
     * @param ids ID of the data to be deleted
     * @return result
     */
    public int deleteGenTableColumnByIds(String ids);
}
