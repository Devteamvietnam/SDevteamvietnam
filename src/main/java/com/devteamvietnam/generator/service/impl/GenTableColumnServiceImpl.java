package com.devteamvietnam.generator.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devteamvietnam.common.core.text.Convert;
import com.devteamvietnam.generator.domain.GenTableColumn;
import com.devteamvietnam.generator.mapper.GenTableColumnMapper;
import com.devteamvietnam.generator.service.IGenTableColumnService; 

/**
 * Business field service layer implementation
 *
 * @author ruoyi
 */
@Service
public class GenTableColumnServiceImpl implements IGenTableColumnService
{
@Autowired
private GenTableColumnMapper genTableColumnMapper;

/**
     * Query the list of business fields
     *
     * @param tableId business field number
     * @return business field collection
     */
@Override
public List<GenTableColumn> selectGenTableColumnListByTableId(Long tableId)
{
return genTableColumnMapper.selectGenTableColumnListByTableId(tableId);
}

    /**
     * Added business fields
     *
     * @param genTableColumn business field information
     * @return result
     */
@Override
public int insertGenTableColumn(GenTableColumn genTableColumn)
{
return genTableColumnMapper.insertGenTableColumn(genTableColumn);
}

/**
     * Modify business fields
     *
     * @param genTableColumn business field information
     * @return result
     */
@Override
public int updateGenTableColumn(GenTableColumn genTableColumn)
{
return genTableColumnMapper.updateGenTableColumn(genTableColumn);
}

/**
     * Delete business field object
     *
     * @param ids ID of the data to be deleted
     * @return result
     */
@Override
public int deleteGenTableColumnByIds(String ids)
{
return genTableColumnMapper.deleteGenTableColumnByIds(Convert.toLongArray(ids));
}
}