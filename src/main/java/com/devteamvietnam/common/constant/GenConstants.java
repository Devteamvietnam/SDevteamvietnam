package com.devteamvietnam.common.constant;

/**
 * Code generation general constants
 *
 * @author Ivan
 */
public class GenConstants
{
    /** Single table (addition, deletion, modification, check) */
    public static final String TPL_CRUD = "crud";

    /** Tree table (addition, deletion, modification, query) */
    public static final String TPL_TREE = "tree";

    /** Tree encoding field */
    public static final String TREE_CODE = "treeCode";

    /** Tree parent encoding field */
    public static final String TREE_PARENT_CODE = "treeParentCode";

    /** Tree name field */
    public static final String TREE_NAME = "treeName";

    /** Upper menu ID field */
    public static final String PARENT_MENU_ID = "parentMenuId";

    /** Previous menu name field */
    public static final String PARENT_MENU_NAME = "parentMenuName";

    /** Database string type */
    public static final String[] COLUMNTYPE_STR = {"char", "varchar", "nvarchar", "varchar2", "tinytext", "text",
            "mediumtext", "longtext" };

    /** Database time type */
    public static final String[] COLUMNTYPE_TIME = {"datetime", "time", "date", "timestamp" };

    /** Database number type */
    public static final String[] COLUMNTYPE_NUMBER = {"tinyint", "smallint", "mediumint", "int", "number", "integer",
            "bit", "bigint", "float", "float", "double", "decimal" };

    /** The page does not need to edit fields */
    public static final String[] COLUMNNAME_NOT_EDIT = {"id", "create_by", "create_time", "del_flag" };

    /** List fields that do not need to be displayed on the page */
    public static final String[] COLUMNNAME_NOT_LIST = {"id", "create_by", "create_time", "del_flag", "update_by",
            "update_time" };

    /** The page does not require query fields */
    public static final String[] COLUMNNAME_NOT_QUERY = {"id", "create_by", "create_time", "del_flag", "update_by",
            "update_time", "remark" };

    /** Entity base class field */
    public static final String[] BASE_ENTITY = {"createBy", "createTime", "updateBy", "updateTime", "remark" };

    /** Tree base class field */
    public static final String[] TREE_ENTITY = {"parentName", "parentId", "orderNum", "ancestors", "children" };

    /** Text box */
    public static final String HTML_INPUT = "input";

    /** Text field */
    public static final String HTML_TEXTAREA = "textarea";

    /** drop-down box */
    public static final String HTML_SELECT = "select";

    /** Single box */
    public static final String HTML_RADIO = "radio";

    /** checkbox */
    public static final String HTML_CHECKBOX = "checkbox";

    /** Date control */
    public static final String HTML_DATETIME = "datetime";

    /** Rich text control */
    public static final String HTML_EDITOR = "editor";

    /** String type */
    public static final String TYPE_STRING = "String";

    /** Integer */
    public static final String TYPE_INTEGER = "Integer";

    /** Long integer */
    public static final String TYPE_LONG = "Long";

    /** Floating point */
    public static final String TYPE_DOUBLE = "Double";

    /** High-precision calculation type */
    public static final String TYPE_BIGDECIMAL = "BigDecimal";

    /** Time type */
    public static final String TYPE_DATE = "Date";

    /** Fuzzy query */
    public static final String QUERY_LIKE = "LIKE";

    /** Required */
    public static final String REQUIRE = "1";
}
