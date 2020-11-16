package com.devteamvietnam.generator.domain;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.ArrayUtils;

import com.devteamvietnam.common.constant.GenConstants;
import com.devteamvietnam.common.core.domain.BaseEntity;
import com.devteamvietnam.common.utils.StringUtils;

/**
 * Business table gen_table

 */
public class GenTable extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Numbering */
    private Long tableId;

    /** Table name */
    @NotBlank(message = "The table name cannot be empty")
    private String tableName;

    /** Table description */
    @NotBlank(message = "The table description cannot be empty")
    private String tableComment;

    /** Entity class name (first letter capitalized) */
    @NotBlank(message = "Entity class name cannot be empty")
    private String className;

    /** Template used (crud single table operation tree tree table operation) */
    private String tplCategory;

    /** Generate package path */
    @NotBlank(message = "The generated package path cannot be empty")
    private String packageName;

    /** Generate module name */
    @NotBlank(message = "Generate module name cannot be empty")
    private String moduleName;

    /** Generate business name */
    @NotBlank(message = "The generated business name cannot be empty")
    private String businessName;

    /** Generate function name */
    @NotBlank(message = "Generate function name cannot be empty")
    private String functionName;

    /** Generate author */
    @NotBlank(message = "The author cannot be empty")
    private String functionAuthor;

    /** Code generation method (0zip compressed package 1 custom path) */
    private String genType;

    /** Generate path (do not fill in the default project path) */
    private String genPath;

    /** Primary key information */
    private GenTableColumn pkColumn;

    /** List information */
    @Valid
    private List<GenTableColumn> columns;

    /** Other generation options */
    private String options;

    /** Tree encoding field */
    private String treeCode;

    /** Tree parent encoding field */
    private String treeParentCode;

    /** Tree name field */
    private String treeName;

    /** Upper menu ID field */
    private String parentMenuId;

    /** Previous menu name field */
    private String parentMenuName;

    public Long getTableId()
    {
        return tableId;
    }

    public void setTableId(Long tableId)
    {
        this.tableId = tableId;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getTableComment()
    {
        return tableComment;
    }

    public void setTableComment(String tableComment)
    {
        this.tableComment = tableComment;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public String getTplCategory()
    {
        return tplCategory;
    }

    public void setTplCategory(String tplCategory)
    {
        this.tplCategory = tplCategory;
    }

    public String getPackageName()
    {
        return packageName;
    }

    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }

    public String getModuleName()
    {
        return moduleName;
    }

    public void setModuleName(String moduleName)
    {
        this.moduleName = moduleName;
    }

    public String getBusinessName()
    {
        return businessName;
    }
    public void setBusinessName(String businessName)
    {
        this.businessName = businessName;
    }

    public String getFunctionName()
    {
        return functionName;
    }

    public void setFunctionName(String functionName)
    {
        this.functionName = functionName;
    }

    public String getFunctionAuthor()
    {
        return functionAuthor;
    }

    public void setFunctionAuthor(String functionAuthor)
    {
        this.functionAuthor = functionAuthor;
    }

    public String getGenType()
    {
        return genType;
    }

    public void setGenType(String genType)
    {
        this.genType = genType;
    }

    public String getGenPath()
    {
        return genPath;
    }

    public void setGenPath(String genPath)
    {
        this.genPath = genPath;
    }

    public GenTableColumn getPkColumn()
    {
        return pkColumn;
    }

    public void setPkColumn(GenTableColumn pkColumn)
    {
        this.pkColumn = pkColumn;
    }

    public List<GenTableColumn> getColumns()
    {
        return columns;
    }

    public void setColumns(List<GenTableColumn> columns)
    {
        this.columns = columns;
    }

    public String getOptions()
    {
        return options;
    }

    public void setOptions(String options)
    {
        this.options = options;
    }

    public String getTreeCode()
    {
        return treeCode;
    }

    public void setTreeCode(String treeCode)
    {
        this.treeCode = treeCode;
    }

    public String getTreeParentCode()
    {
        return treeParentCode;
    }

    public void setTreeParentCode(String treeParentCode)
    {
        this.treeParentCode = treeParentCode;
    }

    public String getTreeName()
    {
        return treeName;
    }

    public void setTreeName(String treeName)
    {
        this.treeName = treeName;
    }

    public String getParentMenuId()
    {
        return parentMenuId;
    }

    public void setParentMenuId(String parentMenuId)
    {
        this.parentMenuId = parentMenuId;
    }

    public String getParentMenuName()
    {
        return parentMenuName;
    }

    public void setParentMenuName(String parentMenuName)
    {
        this.parentMenuName = parentMenuName;
    }

    public boolean isTree()
    {
        return isTree(this.tplCategory);
    }

    public static boolean isTree(String tplCategory)
    {
        return tplCategory != null && StringUtils.equals(GenConstants.TPL_TREE, tplCategory);
    }

    public boolean isCrud()
    {
        return isCrud(this.tplCategory);
    }

    public static boolean isCrud(String tplCategory)
    {
        return tplCategory != null && StringUtils.equals(GenConstants.TPL_CRUD, tplCategory);
    }

    public boolean isSuperColumn(String javaField)
    {
        return isSuperColumn(this.tplCategory, javaField);
    }

    public static boolean isSuperColumn(String tplCategory, String javaField)
    {
        if (isTree(tplCategory))
        {
            return StringUtils.equalsAnyIgnoreCase(javaField,
                    ArrayUtils.addAll(GenConstants.TREE_ENTITY, GenConstants.BASE_ENTITY));
        }
        return StringUtils.equalsAnyIgnoreCase(javaField, GenConstants.BASE_ENTITY);
    }
}