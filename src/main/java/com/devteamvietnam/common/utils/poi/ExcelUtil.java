package com.devteamvietnam.common.utils.poi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.devteamvietnam.common.annotation.Excel;
import com.devteamvietnam.common.annotation.Excel.ColumnType;
import com.devteamvietnam.common.annotation.Excel.Type;
import com.devteamvietnam.common.annotation.Excels;
import com.devteamvietnam.common.config.DevteamConfig;
import com.devteamvietnam.common.core.domain.AjaxResult;
import com.devteamvietnam.common.core.text.Convert;
import com.devteamvietnam.common.exception.CustomException;
import com.devteamvietnam.common.utils.DateUtils;
import com.devteamvietnam.common.utils.DictUtils;
import com.devteamvietnam.common.utils.StringUtils;
import com.devteamvietnam.common.utils.reflect.ReflectUtils;

/**
 * Excel related processing
 *
 * @author Ivan
 */
public class ExcelUtil<T>
{
    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * Maximum number of rows in Excel sheet, 65536 by default
     */
    public static final int sheetSize = 65536;

    /**
     * Worksheet name
     */
    private String sheetName;

    /**
     * Export type (EXPORT: export data; IMPORT: import template)
     */
    private Type type;

    /**
     * Workbook objects
     */
    private Workbook wb;

    /**
     * Worksheet object
     */
    private Sheet sheet;

    /**
     * Style list
     */
    private Map<String, CellStyle> styles;

    /**
     * Import and export data list
     */
    private List<T> list;

    /**
     * Annotation list
     */
    private List<Object[]> fields;

    /**
     * Statistics list
     */
    private Map<Integer, Double> statistics = new HashMap<Integer, Double>();

    /**
     * Number format
     */
    private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("######0.00");

    /**
     * Entity object
     */
    public Class<T> clazz;

    public ExcelUtil(Class<T> clazz)
    {
        this.clazz = clazz;
    }

    public void init(List<T> list, String sheetName, Type type)
    {
        if (list == null)
        {
            list = new ArrayList<T>();
        }
        this.list = list;
        this.sheetName = sheetName;
        this.type = type;
        createExcelField();
        createWorkbook();
    }
    /**
     * For the excel form, the default first index name is converted to list
     *
     * @param is input stream
     * @return collection after conversion
     */
    public List<T> importExcel(InputStream is) throws Exception
    {
        return importExcel(StringUtils.EMPTY, is);
    }

    /**
     * The specified table index name for excel form is converted into list
     *
     * @param sheetName table index name
     * @param is input stream
     * @return collection after conversion
     */
    public List<T> importExcel(String sheetName, InputStream is) throws Exception
    {
        this.type = Type.IMPORT;
        this.wb = WorkbookFactory.create(is);
        List<T> list = new ArrayList<T>();
        Sheet sheet = null;
        if (StringUtils.isNotEmpty(sheetName))
        {
            // If you specify the sheet name, take the content in the specified sheet.
            sheet = wb.getSheet(sheetName);
        }
        else
        {
            // If the passed sheet name does not exist, it will point to the first sheet by default.
            sheet = wb.getSheetAt(0);
        }

        if (sheet == null)
        {
            throw new IOException("File sheet does not exist");
        }

        int rows = sheet.getPhysicalNumberOfRows();

        if (rows> 0)
        {
            // Define a map to store the serial number and field of the excel column.
            Map<String, Integer> cellMap = new HashMap<String, Integer>();
            // Get the header
            Row heard = sheet.getRow(0);
            for (int i = 0; i <heard.getPhysicalNumberOfCells(); i++)
            {
                Cell cell = heard.getCell(i);
                if (StringUtils.isNotNull(cell))
                {
                    String value = this.getCellValue(heard, i).toString();
                    cellMap.put(value, i);
                }
                else
                {
                    cellMap.put(null, i);
                }
            }
         // Process when there is data to get all the fields of the class.
            Field[] allFields = clazz.getDeclaredFields();
            // Define a map to store the serial number and field of the column.
            Map<Integer, Field> fieldsMap = new HashMap<Integer, Field>();
            for (int col = 0; col <allFields.length; col++)
            {
                Field field = allFields[col];
                Excel attr = field.getAnnotation(Excel.class);
                if (attr != null && (attr.type() == Type.ALL || attr.type() == type))
                {
                    // Set the private field properties of the class to be accessible.
                    field.setAccessible(true);
                    Integer column = cellMap.get(attr.name());
                    if (column != null)
                    {
                        fieldsMap.put(column, field);
                    }
                }
            }
            for (int i = 1; i <rows; i++)
            {
                // Start to fetch data from the second row, the default first row is the header.
                Row row = sheet.getRow(i);
                T entity = null;
                for (Map.Entry<Integer, Field> entry: fieldsMap.entrySet())
                {
                    Object val = this.getCellValue(row, entry.getKey());

                    // If there is no instance, create a new one.
                    entity = (entity == null? clazz.newInstance(): entity);
                    // Get the field of the corresponding column from the map.
                    Field field = fieldsMap.get(entry.getKey());
                    // Get the type and set the value according to the object type.
                    Class<?> fieldType = field.getType();
                    if (String.class == fieldType)
                    {
                        String s = Convert.toStr(val);
                        if (StringUtils.endsWith(s, ".0"))
                        {
                            val = StringUtils.substringBefore(s, ".0");
                        }
                        else
                        {
                            val = Convert.toStr(val);
                        }
                    }
                    else if ((Integer.TYPE == fieldType || Integer.class == fieldType) && StringUtils.isNumeric(Convert.toStr(val)))
                    {
                        val = Convert.toInt(val);
                    }
                    else if (Long.TYPE == fieldType || Long.class == fieldType)
                    {
                        val = Convert.toLong(val);
                    }
                    else if (Double.TYPE == fieldType || Double.class == fieldType)
                    {
                        val = Convert.toDouble(val);
                    }
                    else if (Float.TYPE == fieldType || Float.class == fieldType)
                    {
                        val = Convert.toFloat(val);
                    }
                    else if (BigDecimal.class == fieldType)
                    {
                        val = Convert.toBigDecimal(val);
                    }
                    else if (Date.class == fieldType)
                    {
                        if (val instanceof String)
                        {
                            val = DateUtils.parseDate(val);
                        }
                        else if (val instanceof Double)
                        {
                            val = DateUtil.getJavaDate((Double) val);
                        }
                    }
                    if (StringUtils.isNotNull(fieldType))
                    {
                        Excel attr = field.getAnnotation(Excel.class);
                        String propertyName = field.getName();
                        if (StringUtils.isNotEmpty(attr.targetAttr()))
                        {
                            propertyName = field.getName() + "." + attr.targetAttr();
                        }
                        else if (StringUtils.isNotEmpty(attr.readConverterExp()))
                        {
                            val = reverseByExp(Convert.toStr(val), attr.readConverterExp(), attr.separator());
                        }
                        else if (StringUtils.isNotEmpty(attr.dictType()))
                        {
                            val = reverseDictByExp(Convert.toStr(val), attr.dictType(), attr.separator());
                        }
                        ReflectUtils.invokeSetter(entity, propertyName, val);
                    }
                }
                list.add(entity);
            }
        }
        return list;
    }
    
    /**
     * Import the data in the list data source to the excel form
     *
     * @param list export data collection
     * @param sheetName The name of the worksheet
     * @return result
     */
    public AjaxResult exportExcel(List<T> list, String sheetName)
    {
        this.init(list, sheetName, Type.EXPORT);
        return exportExcel();
    }

    /**
     * Import the data in the list data source to the excel form
     *
     * @param sheetName The name of the worksheet
     * @return result
     */
    public AjaxResult importTemplateExcel(String sheetName)
    {
        this.init(null, sheetName, Type.IMPORT);
        return exportExcel();
    }

    /**
     * Import the data in the list data source to the excel form
     *
     * @return result
     */
    public AjaxResult exportExcel()
    {
        OutputStream out = null;
        try
        {
            // How many sheets are taken out.
            double sheetNo = Math.ceil(list.size() / sheetSize);
            for (int index = 0; index <= sheetNo; index++)
            {
                createSheet(sheetNo, index);

                // Generate a line
                Row row = sheet.createRow(0);
                int column = 0;
                // Write the column header name of each field
                for (Object[] os: fields)
                {
                    Excel excel = (Excel) os[1];
                    this.createCell(excel, row, column++);
                }
                if (Type.EXPORT.equals(type))
                {
                    fillExcelData(index, row);
                    addStatisticsRow();
                }
            }
            String filename = encodingFilename(sheetName);
            out = new FileOutputStream(getAbsoluteFile(filename));
            wb.write(out);
            return AjaxResult.success(filename);
        }
        catch (Exception e)
        {
            log.error("Export Excel exception{}", e.getMessage());
            throw new CustomException("Failed to export Excel, please contact the webmaster!");
        }
        finally
        {
            if (wb != null)
            {
                try
                {
                    wb.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
    }
    /**
     * Fill in excel data
     *
     * @param index serial number
     * @param row cell row
     */
    public void fillExcelData(int index, Row row)
    {
        int startNo = index * sheetSize;
        int endNo = Math.min(startNo + sheetSize, list.size());
        for (int i = startNo; i <endNo; i++)
        {
            row = sheet.createRow(i + 1-startNo);
            // Get the exported object.
            T vo = (T) list.get(i);
            int column = 0;
            for (Object[] os: fields)
            {
                Field field = (Field) os[0];
                Excel excel = (Excel) os[1];
                // Set the private properties of the entity class to be accessible
                field.setAccessible(true);
                this.addCell(excel, row, vo, field, column++);
            }
        }
    }

    /**
     * Create table style
     *
     * @param wb workbook object
     * @return style list
     */
    private Map<String, CellStyle> createStyles(Workbook wb)
    {
        // Write each record, each record corresponds to a row in the excel table
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        style.setFont(dataFont);
        styles.put("data", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = wb.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(headerFont);
        styles.put("header", style);
        
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        Font totalFont = wb.createFont();
        totalFont.setFontName("Arial");
        totalFont.setFontHeightInPoints((short) 10);
        style.setFont(totalFont);
        styles.put("total", style);

        return styles;
    }

    /**
     * Create cell
     */
    public Cell createCell(Excel attr, Row row, int column)
    {
        // Create column
        Cell cell = row.createCell(column);
        // write column information
        cell.setCellValue(attr.name());
        setDataValidation(attr, row, column);
        cell.setCellStyle(styles.get("header"));
        return cell;
    }

    /**
     * Set cell information
     *
     * @param value cell value
     * @param attr annotation related
     * @param cell cell information
     */
    public void setCellVo(Object value, Excel attr, Cell cell)
    {
        if (ColumnType.STRING == attr.cellType())
        {
            cell.setCellType(CellType.STRING);
            cell.setCellValue(StringUtils.isNull(value)? attr.defaultValue(): value + attr.suffix());
        }
        else if (ColumnType.NUMERIC == attr.cellType())
        {
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(StringUtils.contains(Convert.toStr(value), ".")? Convert.toDouble(value): Convert.toInt(value));
        }
    }

    /**
     * Create table style
     */
    public void setDataValidation(Excel attr, Row row, int column)
    {
        if (attr.name().indexOf("Note:") >= 0)
        {
            sheet.setColumnWidth(column, 6000);
        }
        else
        {
            // set column width
            sheet.setColumnWidth(column, (int) ((attr.width() + 0.72) * 256));
            row.setHeight((short) (attr.height() * 20));
        }
        // If the prompt information is set, the mouse will be placed on the prompt.
        if (StringUtils.isNotEmpty(attr.prompt()))
        {
            // There are 2-101 column prompts by default.
            setXSSFPrompt(sheet, "", attr.prompt(), 1, 100, column, column);
        }
        // If the combo attribute is set, this column can only be selected but not entered
        if (attr.combo().length> 0)
        {
            // By default, columns 2-101 can only be selected and cannot be entered.
            setXSSFValidation(sheet, attr.combo(), 1, 100, column, column);
        }
    }
    /**
     * Add cell
     */
    public Cell addCell(Excel attr, Row row, T vo, Field field, int column)
    {
        Cell cell = null;
        try
        {
            // set line height
            row.setHeight((short) (attr.height() * 20));
            // Decide whether to export according to the settings in Excel. In some cases, you need to keep it blank, and users are expected to fill in this column.
            if (attr.isExport())
            {
                // Create cell
                cell = row.createCell(column);
                cell.setCellStyle(styles.get("data"));

                // Used to read the attributes in the object
                Object value = getTargetValue(vo, field, attr);
                String dateFormat = attr.dateFormat();
                String readConverterExp = attr.readConverterExp();
                String separator = attr.separator();
                String dictType = attr.dictType();
                if (StringUtils.isNotEmpty(dateFormat) && StringUtils.isNotNull(value))
                {
                    cell.setCellValue(DateUtils.parseDateToStr(dateFormat, (Date) value));
                }
                else if (StringUtils.isNotEmpty(readConverterExp) && StringUtils.isNotNull(value))
                {
                    cell.setCellValue(convertByExp(Convert.toStr(value), readConverterExp, separator));
                }
                else if (StringUtils.isNotEmpty(dictType) && StringUtils.isNotNull(value))
                {
                    cell.setCellValue(convertDictByExp(Convert.toStr(value), dictType, separator));
                }
                else if (value instanceof BigDecimal && -1 != attr.scale())
                {
                    cell.setCellValue((((BigDecimal) value).setScale(attr.scale(), attr.roundingMode())).toString());
                }
                else
                {
                    // Set the column type
                    setCellVo(value, attr, cell);
                }
                addStatisticsData(column, Convert.toStr(value), attr);
            }
        }
        catch (Exception e)
        {
            log.error("Failed to export Excel{}", e);
        }
        return cell;
    }

    /**
     * Set POI XSSFSheet cell prompt
     *
     * @param sheet form
     * @param promptTitle prompt title
     * @param promptContent prompt content
     * @param firstRow start line
     * @param endRow end row
     * @param firstCol start column
     * @param endCol end column
     */
    public void setXSSFPrompt(Sheet sheet, String promptTitle, String promptContent, int firstRow, int endRow,
            int firstCol, int endCol)
    {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = helper.createCustomConstraint("DD1");
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        dataValidation.createPromptBox(promptTitle, promptContent);
        dataValidation.setShowPromptBox(true);
        sheet.addValidationData(dataValidation);
    }

    /**
     * To set some column values, only pre-made data can be entered, and a drop-down box is displayed.
     *
     * @param sheet The sheet to be set.
     * @param textlist the content displayed in the drop-down box
     * @param firstRow start line
     * @param endRow end row
     * @param firstCol start column
     * @param endCol end column
     * @return The set sheet.
     */
    public void setXSSFValidation(Sheet sheet, String[] textlist, int firstRow, int endRow, int firstCol, int endCol)
    {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        // Load the contents of the drop-down list
        DataValidationConstraint constraint = helper.createExplicitListConstraint(textlist);
        // Set the cell on which the data validity is loaded, the four parameters are: start row, end row, start column, end column
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // Data validity object
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        // Deal with Excel compatibility issues
        if (dataValidation instanceof XSSFDataValidation)
        {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        }
        else
        {
            dataValidation.setSuppressDropDownArrow(false);
        }

        sheet.addValidationData(dataValidation);
    }
    /**
     * Analyze the derived value 0=male, 1=female, 2=unknown
     *
     * @param propertyValue parameter value
     * @param converterExp translation annotation
     * @param separator separator
     * @return parsed value
     */
    public static String convertByExp(String propertyValue, String converterExp, String separator)
    {
        StringBuilder propertyString = new StringBuilder();
        String[] convertSource = converterExp.split(",");
        for (String item: convertSource)
        {
            String[] itemArray = item.split("=");
            if (StringUtils.containsAny(separator, propertyValue))
            {
                for (String value: propertyValue.split(separator))
                {
                    if (itemArray[0].equals(value))
                    {
                        propertyString.append(itemArray[1] + separator);
                        break;
                    }
                }
            }
            else
            {
                if (itemArray[0].equals(propertyValue))
                {
                    return itemArray[1];
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * Reverse analytic value male=0, female=1, unknown=2
     *
     * @param propertyValue parameter value
     * @param converterExp translation annotation
     * @param separator separator
     * @return parsed value
     */
    public static String reverseByExp(String propertyValue, String converterExp, String separator)
    {
        StringBuilder propertyString = new StringBuilder();
        String[] convertSource = converterExp.split(",");
        for (String item: convertSource)
        {
            String[] itemArray = item.split("=");
            if (StringUtils.containsAny(separator, propertyValue))
            {
                for (String value: propertyValue.split(separator))
                {
                    if (itemArray[1].equals(value))
                    {
                        propertyString.append(itemArray[0] + separator);
                        break;
                    }
                }
            }
            else
            {
                if (itemArray[1].equals(propertyValue))
                {
                    return itemArray[0];
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * Parse dictionary values
     *
     * @param dictValue dictionary value
     * @param dictType dictionary type
     * @param separator separator
     * @return dictionary tag
     */
    public static String convertDictByExp(String dictValue, String dictType, String separator)
    {
        return DictUtils.getDictLabel(dictType, dictValue, separator);
    }

    /**
     * Reverse parse value dictionary value
     *
     * @param dictLabel dictionary label
     * @param dictType dictionary type
     * @param separator separator
     * @return dictionary value
     */
    public static String reverseDictByExp(String dictLabel, String dictType, String separator)
    {
        return DictUtils.getDictValue(dictType, dictLabel, separator);
    }

    /**
     * Total statistics
     */
    private void addStatisticsData(Integer index, String text, Excel entity)
    {
        if (entity != null && entity.isStatistics())
        {
            Double temp = 0D;
            if (!statistics.containsKey(index))
            {
                statistics.put(index, temp);
            }
            try
            {
                temp = Double.valueOf(text);
            }
            catch (NumberFormatException e)
            {
            }
            statistics.put(index, statistics.get(index) + temp);
        }
    }
    /**
     * Create statistical rows
     */
    public void addStatisticsRow()
    {
        if (statistics.size()> 0)
        {
            Cell cell = null;
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            Set<Integer> keys = statistics.keySet();
            cell = row.createCell(0);
            cell.setCellStyle(styles.get("total"));
            cell.setCellValue("Total");

            for (Integer key: keys)
            {
                cell = row.createCell(key);
                cell.setCellStyle(styles.get("total"));
                cell.setCellValue(DOUBLE_FORMAT.format(statistics.get(key)));
            }
            statistics.clear();
        }
    }

    /**
     * Encoding file name
     */
    public String encodingFilename(String filename)
    {
        filename = UUID.randomUUID().toString() + "_" + filename + ".xlsx";
        return filename;
    }

    /**
     * Get download path
     *
     * @param filename file name
     */
    public String getAbsoluteFile(String filename)
    {
        String downloadPath = DevteamConfig.getDownloadPath() + filename;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists())
        {
            desc.getParentFile().mkdirs();
        }
        return downloadPath;
    }

    /**
     * Get the attribute value in the bean
     *
     * @param vo entity object
     * @param field
     * @param excel annotation
     * @return the final attribute value
     * @throws Exception
     */
    private Object getTargetValue(T vo, Field field, Excel excel) throws Exception
    {
        Object o = field.get(vo);
        if (StringUtils.isNotEmpty(excel.targetAttr()))
        {
            String target = excel.targetAttr();
            if (target.indexOf(".")> -1)
            {
                String[] targets = target.split("[.]");
                for (String name: targets)
                {
                    o = getValue(o, name);
                }
            }
            else
            {
                o = getValue(o, target);
            }
        }
        return o;
    }

    /**
     * Get the value in the form of the get method method of the class attribute
     *
     * @param o
     * @param name
     * @return value
     * @throws Exception
     */
    private Object getValue(Object o, String name) throws Exception
    {
        if (StringUtils.isNotEmpty(name))
        {
            Class<?> clazz = o.getClass();
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            o = field.get(o);
        }
        return o;
    }

    /**
     * Get all defined fields
     */
    private void createExcelField()
    {
        this.fields = new ArrayList<Object[]>();
        List<Field> tempFields = new ArrayList<>();
        tempFields.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
        tempFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        for (Field field: tempFields)
        {
            // Single annotation
            if (field.isAnnotationPresent(Excel.class))
            {
                putToField(field, field.getAnnotation(Excel.class));
            }

            // Multiple annotations
            if (field.isAnnotationPresent(Excels.class))
            {
                Excels attrs = field.getAnnotation(Excels.class);
                Excel[] excels = attrs.value();
                for (Excel excel: excels)
                {
                    putToField(field, excel);
                }
            }
        }
        this.fields = this.fields.stream().sorted(Comparator.comparing(objects -> ((Excel) objects[1]).sort())).collect(Collectors.toList());
    }

    /**
     * Put it in the field collection
     */
    private void putToField(Field field, Excel attr)
    {
        if (attr != null && (attr.type() == Type.ALL || attr.type() == type))
        {
            this.fields.add(new Object[] {field, attr });
        }
    }

    /**
     * Create a workbook
     */
    public void createWorkbook()
    {
        this.wb = new SXSSFWorkbook(500);
    }

    /**
     * Create worksheet
     *
     * @param sheetNo sheet quantity
     * @param index serial number
     */
    public void createSheet(double sheetNo, int index)
    {
        this.sheet = wb.createSheet();
        this.styles = createStyles(wb);
        // Set the name of the worksheet.
        if (sheetNo == 0)
        {
            wb.setSheetName(index, sheetName);
        }
        else
        {
            wb.setSheetName(index, sheetName + index);
        }
    }
    /**
     * Get cell value
     *
     * @param row obtained row
     * @param column Get the cell column number
     * @return cell value
     */
    public Object getCellValue(Row row, int column)
    {
        if (row == null)
        {
            return row;
        }
        Object val = "";
        try
        {
            Cell cell = row.getCell(column);
            if (StringUtils.isNotNull(cell))
            {
                if (cell.getCellTypeEnum() == CellType.NUMERIC || cell.getCellTypeEnum() == CellType.FORMULA)
                {
                    val = cell.getNumericCellValue();
                    if (HSSFDateUtil.isCellDateFormatted(cell))
                    {
                        val = DateUtil.getJavaDate((Double) val); // POI Excel date format conversion
                    }
                    else
                    {
                        if ((Double) val% 1> 0)
                        {
                            val = new BigDecimal(val.toString());
                        }
                        else
                        {
                            val = new DecimalFormat("0").format(val);
                        }
                    }
                }
                else if (cell.getCellTypeEnum() == CellType.STRING)
                {
                    val = cell.getStringCellValue();
                }
                else if (cell.getCellTypeEnum() == CellType.BOOLEAN)
                {
                    val = cell.getBooleanCellValue();
                }
                else if (cell.getCellTypeEnum() == CellType.ERROR)
                {
                    val = cell.getErrorCellValue();
                }

            }
        }
        catch (Exception e)
        {
            return val;
        }
        return val;
    }
}

