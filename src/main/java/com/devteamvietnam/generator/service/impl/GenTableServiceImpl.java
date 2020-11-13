package com.devteamvietnam.generator.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.devteamvietnam.common.constant.Constants;
import com.devteamvietnam.common.constant.GenConstants;
import com.devteamvietnam.common.core.text.CharsetKit;
import com.devteamvietnam.common.exception.CustomException;
import com.devteamvietnam.common.utils.SecurityUtils;
import com.devteamvietnam.common.utils.StringUtils;
import com.devteamvietnam.common.utils.file.FileUtils;
import com.devteamvietnam.generator.domain.GenTable;
import com.devteamvietnam.generator.domain.GenTableColumn;
import com.devteamvietnam.generator.mapper.GenTableColumnMapper;
import com.devteamvietnam.generator.mapper.GenTableMapper;
import com.devteamvietnam.generator.service.IGenTableService;
import com.devteamvietnam.generator.util.GenUtils;
import com.devteamvietnam.generator.util.VelocityInitializer;
import com.devteamvietnam.generator.util.VelocityUtils;
/**
 * Business service layer implementation
 *
 */
@Service
public class GenTableServiceImpl implements IGenTableService
{
    private static final Logger log = LoggerFactory.getLogger(GenTableServiceImpl.class);

    @Autowired
    private GenTableMapper genTableMapper;

    @Autowired
    private GenTableColumnMapper genTableColumnMapper;

    /**
     * Query business information
     *
     * @param id business ID
     * @return business information
     */
    @Override
    public GenTable selectGenTableById(Long id)
    {
        GenTable genTable = genTableMapper.selectGenTableById(id);
        setTableFromOptions(genTable);
        return genTable;
    }

    /**
     * Query business list
     *
     * @param genTable business information
     * @return business collection
     */
    @Override
    public List<GenTable> selectGenTableList(GenTable genTable)
    {
        return genTableMapper.selectGenTableList(genTable);
    }

    /**
     * Query database list
     *
     * @param genTable business information
     * @return database table collection
     */
    @Override
    public List<GenTable> selectDbTableList(GenTable genTable)
    {
        return genTableMapper.selectDbTableList(genTable);
    }

    /**
     * Query database list
     *
     * @param tableNames table name group
     * @return database table collection
     */
    @Override
    public List<GenTable> selectDbTableListByNames(String[] tableNames)
    {
        return genTableMapper.selectDbTableListByNames(tableNames);
    }

    /**
     * Modify business
     *
     * @param genTable business information
     * @return result
     */
    @Override
    @Transactional
    public void updateGenTable(GenTable genTable)
    {
        String options = JSON.toJSONString(genTable.getParams());
        genTable.setOptions(options);
        int row = genTableMapper.updateGenTable(genTable);
        if (row> 0)
        {
            for (GenTableColumn cenTableColumn: genTable.getColumns())
            {
                genTableColumnMapper.updateGenTableColumn(cenTableColumn);
            }
        }
    }

    /**
     * Delete business objects
     *
     * @param tableIds ID of the data to be deleted
     * @return result
     */
    @Override
    @Transactional
    public void deleteGenTableByIds(Long[] tableIds)
    {
        genTableMapper.deleteGenTableByIds(tableIds);
        genTableColumnMapper.deleteGenTableColumnByIds(tableIds);
    }

    /**
     * Import table structure
     *
     * @param tableList import table list
     */
    @Override
    @Transactional
    public void importGenTable(List<GenTable> tableList)
    {
        String operName = SecurityUtils.getUsername();
        try
        {
            for (GenTable table: tableList)
            {
                String tableName = table.getTableName();
                GenUtils.initTable(table, operName);
                int row = genTableMapper.insertGenTable(table);
                if (row> 0)
                {
                    // Save column information
                    List<GenTableColumn> genTableColumns = genTableColumnMapper.selectDbTableColumnsByName(tableName);
                    for (GenTableColumn column: genTableColumns)
                    {
                        GenUtils.initColumnField(column, table);
                        genTableColumnMapper.insertGenTableColumn(column);
                    }
                }
            }
        }
        catch (Exception e)
        {
            throw new CustomException("Import failed:" + e.getMessage());
        }
    }
    /**
     * Preview code
     *
     * @param tableId table number
     * @return preview data list
     */
    @Override
    public Map<String, String> previewCode(Long tableId)
    {
        Map<String, String> dataMap = new LinkedHashMap<>();
        // Query table information
        GenTable table = genTableMapper.selectGenTableById(tableId);
        // Query column information
        List<GenTableColumn> columns = table.getColumns();
        setPkColumn(table, columns);
        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // Get a list of templates
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
        for (String template: templates)
        {
            // Render template
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, Constants.UTF8);
            tpl.merge(context, sw);
            dataMap.put(template, sw.toString());
        }
        return dataMap;
    }

    /**
     * Generate code (download method)
     *
     * @param tableName table name
     * @return data
     */
    @Override
    public byte[] downloadCode(String tableName)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        generatorCode(tableName, zip);
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * Generate code (custom path)
     *
     * @param tableName table name
     */
    @Override
    public void generatorCode(String tableName)
    {
        // Query table information
        GenTable table = genTableMapper.selectGenTableByName(tableName);
        // Query column information
        List<GenTableColumn> columns = table.getColumns();
        setPkColumn(table, columns);

        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // Get a list of templates
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
        for (String template: templates)
        {
            if (!StringUtils.containsAny(template, "sql.vm", "api.js.vm", "index.vue.vm", "index-tree.vue.vm"))
            {
                // Render template
                StringWriter sw = new StringWriter();
                Template tpl = Velocity.getTemplate(template, Constants.UTF8);
                tpl.merge(context, sw);
                try
                {
                    String path = getGenPath(table, template);
                    FileUtils.writeStringToFile(new File(path), sw.toString(), CharsetKit.UTF_8);
                }
                catch (IOException e)
                {
                    throw new CustomException("Failed to render template, table name:" + table.getTableName());
                }
            }
        }
    }

    /**
     * Synchronize the database
     *
     * @param tableName table name
     */
    @Override
    @Transactional
    public void synchDb(String tableName)
    {
        GenTable table = genTableMapper.selectGenTableByName(tableName);
        List<GenTableColumn> tableColumns = table.getColumns();
        List<String> tableColumnNames = tableColumns.stream().map(GenTableColumn::getColumnName).collect(Collectors.toList());

        List<GenTableColumn> dbTableColumns = genTableColumnMapper.selectDbTableColumnsByName(tableName);
        List<String> dbTableColumnNames = dbTableColumns.stream().map(GenTableColumn::getColumnName).collect(Collectors.toList());

        dbTableColumns.forEach(column -> {
            if (!tableColumnNames.contains(column.getColumnName()))
            {
                GenUtils.initColumnField(column, table);
                genTableColumnMapper.insertGenTableColumn(column);
            }
        });

        List<GenTableColumn> delColumns = tableColumns.stream().filter(column -> !dbTableColumnNames.contains(column.getColumnName())).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(delColumns))
        {
            genTableColumnMapper.deleteGenTableColumns(delColumns);
        }
    }

    /**
     * Generate codes in batches (download method)
     *
     * @param tableNames table array
     * @return data
     */
    @Override
    public byte[] downloadCode(String[] tableNames)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName: tableNames)
        {
            generatorCode(tableName, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }
    /**
     * Query table information and generate code
     */
    private void generatorCode(String tableName, ZipOutputStream zip)
    {
        // Query table information
        GenTable table = genTableMapper.selectGenTableByName(tableName);
        // Query column information
        List<GenTableColumn> columns = table.getColumns();
        setPkColumn(table, columns);

        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // Get a list of templates
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
        for (String template: templates)
        {
            // Render template
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, Constants.UTF8);
            tpl.merge(context, sw);
            try
            {
                // add to zip
                zip.putNextEntry(new ZipEntry(VelocityUtils.getFileName(template, table)));
                IOUtils.write(sw.toString(), zip, Constants.UTF8);
                IOUtils.closeQuietly(sw);
zip.flush();
                zip.closeEntry();
            }
            catch (IOException e)
            {
                log.error("Failed to render template, table name:" + table.getTableName(), e);
            }
        }
    }

    /**
     * Modify and save parameter verification
     *
     * @param genTable business information
     */
    @Override
    public void validateEdit(GenTable genTable)
    {
        if (GenConstants.TPL_TREE.equals(genTable.getTplCategory()))
        {
            String options = JSON.toJSONString(genTable.getParams());
            JSONObject paramsObj = JSONObject.parseObject(options);
            if (StringUtils.isEmpty(paramsObj.getString(GenConstants.TREE_CODE)))
            {
                throw new CustomException("The tree code field cannot be empty");
            }
            else if (StringUtils.isEmpty(paramsObj.getString(GenConstants.TREE_PARENT_CODE)))
            {
                throw new CustomException("The parent code field of the tree cannot be empty");
            }
            else if (StringUtils.isEmpty(paramsObj.getString(GenConstants.TREE_NAME)))
            {
                throw new CustomException("The tree name field cannot be empty");
            }
        }
    }

    /**
     * Set the primary key column information
     *
     * @param table business table information
     * @param columns business field list
     */
    public void setPkColumn(GenTable table, List<GenTableColumn> columns)
    {
        for (GenTableColumn column: columns)
        {
            if (column.isPk())
            {
                table.setPkColumn(column);
                break;
            }
        }
        if (StringUtils.isNull(table.getPkColumn()))
        {
            table.setPkColumn(columns.get(0));
        }
    }

    /**
     * Set the code to generate other option values
     *
     * @param genTable generated object after setting
     */
    public void setTableFromOptions(GenTable genTable)
    {
        JSONObject paramsObj = JSONObject.parseObject(genTable.getOptions());
        if (StringUtils.isNotNull(paramsObj))
        {
            String treeCode = paramsObj.getString(GenConstants.TREE_CODE);
            String treeParentCode = paramsObj.getString(GenConstants.TREE_PARENT_CODE);
            String treeName = paramsObj.getString(GenConstants.TREE_NAME);
            String parentMenuId = paramsObj.getString(GenConstants.PARENT_MENU_ID);
            String parentMenuName = paramsObj.getString(GenConstants.PARENT_MENU_NAME);
            
            genTable.setTreeCode(treeCode);
            genTable.setTreeParentCode(treeParentCode);
            genTable.setTreeName(treeName);
            genTable.setParentMenuId(parentMenuId);
            genTable.setParentMenuName(parentMenuName);
        }
    }

    /**
     * Get the code generation address
     *
     * @param table business table information
     * @param template template file path
     * @return generates address
     */
    public static String getGenPath(GenTable table, String template)
    {
        String genPath = table.getGenPath();
        if (StringUtils.equals(genPath, "/"))
        {
            return System.getProperty("user.dir") + File.separator + "src" + File.separator + VelocityUtils.getFileName(template, table);
        }
        return genPath + File.separator + VelocityUtils.getFileName(template, table);
    }
}
