package org.maccha.base.util;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.maccha.base.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class ExcelUtils
{
  private static Logger log = LoggerFactory.getLogger(ExcelUtils.class);
  private static int EXCEL2003 = 2007;
  private static int EXCEL2010 = 2010;

  public static List<Map> parseExcel2Map(String excelFile)
    throws Exception
  {
    List list = new ArrayList();
    ImportExcel ei = new ImportExcel(excelFile, 0);
    String[] arryColHead = new String[ei.getLastCellNum()];
    Row rowHead = ei.getRow(0);
    for (int j = 0; j < ei.getLastCellNum(); j++) {
      arryColHead[j] = ImportExcel.access$000(ei, rowHead, j);
    }
    for (int i = ei.getDataRowNum(); i <= ei.getLastDataRowNum(); i++) {
      Row row = ei.getRow(i);
      if (row != null) {
        Map rowMap = new LinkedHashMap();
        for (int j = 0; j < arryColHead.length; j++) {
          String val = ei.getCellValue(row, j);
          if ((arryColHead[j] != null) && (arryColHead[j].trim().length() != 0))
            rowMap.put(arryColHead[j].replaceAll(" ", ""), val);
        }
        list.add(rowMap);
      }
    }
    return list;
  }

  public static List parseExcel2Obj(String excelFile, Class cls, String[] arrRefFieldNames)
    throws Exception
  {
    return parseExcel2Obj(new File(excelFile), cls, arrRefFieldNames);
  }

  public static <E> List<E> parseExcel2Obj(File excelFile, Class cls, String[] arrRefFieldNames)
    throws Exception
  {
    ImportExcel ei = new ImportExcel(excelFile, 1);
    return ei.parseExcel2Obj(cls, arrRefFieldNames);
  }

  public static <E> List<E> parseExcel2Obj(MultipartFile multipartFile, Class cls, String[] arrRefFieldNames)
    throws Exception
  {
    List dataList = Lists.newArrayList();
    try {
      ImportExcel ei = new ImportExcel(multipartFile, 1, 1);
      return ei.parseExcel2Obj(cls, arrRefFieldNames);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return dataList;
  }

  public static void parseList2Excel(List listPo, Map<String, String> excelHeads, HttpServletResponse response, String fileName)
    throws Exception
  {
    IExportExcel _exportExcel = null;
    try {
      _exportExcel = parseList2Excel(listPo, excelHeads, EXCEL2010);
      _exportExcel.write(response, fileName);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      _exportExcel.dispose();
    }
  }

  public static File parseList2Excel(List listPo, Map<String, String> excelHeads, String excelFilePath)
    throws Exception
  {
    IExportExcel _exportExcel = null;
    File _fileExcel = null;
    try {
      _exportExcel = parseList2Excel(listPo, excelHeads, EXCEL2010);
      _fileExcel = _exportExcel.writeFile(excelFilePath);
    } catch (Exception ex) {
      ex.printStackTrace();
      BizException.handleMessageException("export error!", ex.getCause());
    } finally {
      try {
        _exportExcel.dispose(); } catch (Exception ex) {
      }
    }
    return _fileExcel;
  }

  public static File parseList2Excel2003(List listPo, Map<String, String> excelHeads, String excelFilePath)
    throws Exception
  {
    IExportExcel _exportExcel = null;
    File _fileExcel = null;
    try {
      _exportExcel = parseList2Excel(listPo, excelHeads, EXCEL2003);
      _fileExcel = _exportExcel.writeFile(excelFilePath);
    } catch (Exception ex) {
      ex.printStackTrace();
      BizException.handleMessageException("export error!", ex.getCause());
    } finally {
      try {
        _exportExcel.dispose(); } catch (Exception ex) {
      }
    }
    return _fileExcel;
  }

  private static IExportExcel parseList2Excel(List listPo, Map<String, String> excelHeads, int intExcelFormat)
    throws Exception
  {
    List headerList = Lists.newArrayList();
    int colNumber = 0;
    Iterator itrKey = excelHeads.keySet().iterator();
    List listFields = new ArrayList();
    String strKey = null;
    while (itrKey.hasNext()) {
      strKey = (String)itrKey.next();
      listFields.add(strKey);
      headerList.add(excelHeads.get(strKey));
      colNumber++;
    }
    IExportExcel _exportExcel = null;
    if (intExcelFormat == EXCEL2003)
      _exportExcel = new ExportExcel2003("", headerList);
    else {
      _exportExcel = new ExportExcel("", headerList);
    }
    String strPropName = null;
    Object objPropValue = null;
    String strPropValue = null;
    for (int i = 0; i < listPo.size(); i++)
    {
      Object objPoEntity = listPo.get(i);
      Row row = _exportExcel.addRow();
      for (int j = 0; j < listFields.size(); j++) {
        strPropName = (String)listFields.get(j);
        if ((objPoEntity instanceof Map))
          objPropValue = ((Map)objPoEntity).get(strPropName);
        else {
          objPropValue = ClassUtils.get(objPoEntity, strPropName);
        }
        if ((objPropValue != null) && ((objPropValue instanceof java.util.Date))) {
          strPropValue = DateUtils.format((java.util.Date)objPropValue, "yyyy-MM-dd HH:mm:ss");
          _exportExcel.addCell(row, j, strPropValue);
        } else if ((objPropValue != null) && ((objPropValue instanceof java.util.Date))) {
          strPropValue = DateUtils.format((java.util.Date)objPropValue, "yyyy-MM-dd HH:mm:ss");
          _exportExcel.addCell(row, j, strPropValue);
        } else if ((objPropValue != null) && ((objPropValue instanceof Clob))) {
          Clob _obj1 = (Clob)objPropValue;
          strPropValue = (String)TypeConvertorUtils.convert(_obj1, Clob.class, String.class);
          _exportExcel.addCell(row, j, strPropValue);
        } else if ((objPropValue != null) && ((objPropValue instanceof Timestamp))) {
          Object _obj = TypeConvertorUtils.convert(objPropValue, java.util.Date.class);
          strPropValue = DateUtils.format((java.util.Date)_obj, "yyyy-MM-dd HH:mm:ss");
          _exportExcel.addCell(row, j, strPropValue);
        } else if ((objPropValue != null) && ((objPropValue instanceof BigDecimal))) {
          Object _obj = TypeConvertorUtils.convert(objPropValue, Double.class);
          _exportExcel.addCell(row, j, _obj);
        } else {
          _exportExcel.addCell(row, j, objPropValue == null ? "" : objPropValue);
        }
      }
    }
    return _exportExcel;
  }

  public static class ExportExcel
    implements ExcelUtils.IExportExcel
  {
    private static int rowaccess = 100;
    private SXSSFWorkbook wb;
    private SXSSFSheet sheet;
    private Map<String, CellStyle> styles;
    private int rownum;
    FileOutputStream outputStream;

    public ExportExcel(String title, String[] headers)
    {
      initialize(title, Lists.newArrayList(headers));
    }

    public ExportExcel(String title, List<String> headerList)
    {
      initialize(title, headerList);
    }

    private void initialize(String title, List<String> headerList)
    {
      this.wb = new SXSSFWorkbook();

      this.wb.setCompressTempFiles(true);
      this.sheet = ((SXSSFSheet)this.wb.createSheet("Export"));
      this.sheet.setRandomAccessWindowSize(rowaccess);

      if (StringUtils.isNotBlank(title)) {
        Row titleRow = this.sheet.createRow(this.rownum++);
        titleRow.setHeightInPoints(30.0F);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellStyle((CellStyle)this.styles.get("title"));
        titleCell.setCellValue(title);
        this.sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), titleRow.getRowNum(), headerList.size() - 1));
      }

      if (headerList == null) {
        throw new RuntimeException("headerList not null!");
      }
      Row headerRow = this.sheet.createRow(this.rownum++);
      headerRow.setHeightInPoints(16.0F);
      for (int i = 0; i < headerList.size(); i++) {
        Cell cell = headerRow.createCell(i);

        String[] ss = StringUtils.split((String)headerList.get(i), "**", 2);
        if (ss.length == 2) {
          cell.setCellValue(ss[0]);
          Comment comment = this.sheet.createDrawingPatriarch().createCellComment(new XSSFClientAnchor(0, 0, 0, 0, 3, 3, 5, 6));
          comment.setString(new XSSFRichTextString(ss[1]));
          cell.setCellComment(comment);
        } else {
          cell.setCellValue((String)headerList.get(i));
        }
        this.sheet.autoSizeColumn(i);
      }
      for (int i = 0; i < headerList.size(); i++) {
        int colWidth = this.sheet.getColumnWidth(i) * 2;
        this.sheet.setColumnWidth(i, colWidth < 3000 ? 3000 : colWidth);
      }
      ExcelUtils.log.debug("Initialize success.");
    }

    public Row addRow()
    {
      return this.sheet.createRow(this.rownum++);
    }

    public Cell addCell(Row row, int column, Object val)
    {
      return addCell(row, column, val, 0, Class.class);
    }

    public Cell addCell(Row row, int column, Object val, int align, Class<?> fieldType)
    {
      Cell cell = row.createCell(column);
      try
      {
        if (val == null) {
          cell.setCellValue("");
        } else if ((val instanceof String)) {
          ExcelUtils.log.debug("String val = " + val);
          cell.setCellType(1);
          cell.setCellValue((String)val);
        } else if ((val instanceof Integer)) {
          ExcelUtils.log.debug("Integer val = " + val);
          cell.setCellType(0);

          cell.setCellValue(((Integer)val).intValue());
        } else if ((val instanceof Long)) {
          ExcelUtils.log.debug("Long val = " + val);
          cell.setCellType(0);

          cell.setCellValue(((Long)val).longValue());
        } else if ((val instanceof Double)) {
          ExcelUtils.log.debug("Double val = " + val);
          cell.setCellType(0);

          cell.setCellValue(((Double)val).doubleValue());
        } else if ((val instanceof BigDecimal)) {
          ExcelUtils.log.debug("BigDecimal val = " + val);
          cell.setCellType(0);

          cell.setCellValue(((BigDecimal)val).intValue());
        } else if ((val instanceof Float)) {
          ExcelUtils.log.debug("Float val = " + val);
          cell.setCellType(0);
          cell.setCellValue(((Float)val).floatValue());
        } else if ((val instanceof java.util.Date)) {
          ExcelUtils.log.debug("Date val = " + val);

          cell.setCellType(1);
          String strDateVal = DateUtils.format((java.util.Date)val, "yyyy-MM-dd");
          cell.setCellValue(strDateVal);
        } else if ((val instanceof java.sql.Date)) {
          cell.setCellType(1);

          String strDateVal = DateUtils.format((java.sql.Date)val, "yyyy-MM-dd");
          cell.setCellValue(strDateVal);
        } else {
          ExcelUtils.log.debug("else val = " + val + ",val.class = " + val.getClass().getName());
          if (fieldType != Class.class)
            cell.setCellValue((String)fieldType.getMethod("setValue", new Class[] { Object.class }).invoke(null, new Object[] { val }));
          else
            cell.setCellValue((String)Class.forName(getClass().getName().replaceAll(getClass().getSimpleName(), "fieldtype." + val.getClass().getSimpleName() + "Type")).getMethod("setValue", new Class[] { Object.class }).invoke(null, new Object[] { val }));
        }
      }
      catch (Exception ex)
      {
        ExcelUtils.log.debug("Set cell value [" + row.getRowNum() + "," + column + "] error: " + ex.toString());
        cell.setCellValue(val.toString());
      }

      return cell;
    }

    public ExcelUtils.IExportExcel write(OutputStream os)
      throws IOException
    {
      this.wb.write(os);
      return this;
    }

    public ExcelUtils.IExportExcel write(HttpServletResponse response, String fileName)
      throws IOException
    {
      response.reset();
      response.setContentType("application/octet-stream; charset=utf-8");
      response.setHeader("Content-Disposition", "attachment; filename=" + EncoderUtils.urlEncode(fileName));
      write(response.getOutputStream());
      return this;
    }

    public File writeFile(String name)
      throws FileNotFoundException, IOException
    {
      File excelFile = new File(name);
      FileOutputStream os = new FileOutputStream(excelFile);
      write(os);
      return excelFile;
    }

    public ExportExcel dispose()
    {
      try
      {
        if (this.outputStream != null) this.outputStream.close();

        this.wb.dispose();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      return this;
    }
  }

  public static class ExportExcel2003
    implements ExcelUtils.IExportExcel
  {
    private HSSFWorkbook wb;
    private HSSFSheet sheet;
    private int rownum;
    FileOutputStream outputStream;

    public ExportExcel2003(String title, String[] headers)
    {
      initialize(title, Lists.newArrayList(headers));
    }

    public ExportExcel2003(String title, List<String> headerList)
    {
      initialize(title, headerList);
    }

    private void initialize(String title, List<String> headerList)
    {
      this.wb = new HSSFWorkbook();
      this.sheet = this.wb.createSheet();

      if (headerList == null) {
        throw new RuntimeException("headerList not null!");
      }
      HSSFFont font = this.wb.createFont();
      font.setColor(32767);
      font.setBoldweight(700);
      HSSFCellStyle cellStyle = this.wb.createCellStyle();
      cellStyle.setFont(font);
      cellStyle.setAlignment(2);
      cellStyle.setVerticalAlignment(1);

      HSSFRow titleRow = this.sheet.createRow(this.rownum++);
      int i = 0; for (int size = headerList.size(); i < size; i++) {
        this.sheet.setColumnWidth(i, 4000);
        HSSFCell cell = titleRow.createCell(i, 0);
        cell.setCellStyle(cellStyle);
        cell.setCellValue((String)headerList.get(i));
      }
      ExcelUtils.log.debug("Initialize success.");
    }

    public Row addRow()
    {
      return this.sheet.createRow(this.rownum++);
    }

    public Cell addCell(Row row, int column, Object val)
    {
      return addCell(row, column, val, 0, Class.class);
    }

    public Cell addCell(Row row, int column, Object val, int align, Class<?> fieldType)
    {
      Cell cell = row.createCell(column);
      try
      {
        if (val == null) {
          cell.setCellValue("");
        } else if ((val instanceof String)) {
          ExcelUtils.log.debug("String val = " + val);
          cell.setCellType(1);
          cell.setCellValue((String)val);
        } else if ((val instanceof Integer)) {
          ExcelUtils.log.debug("Integer val = " + val);
          cell.setCellType(0);

          cell.setCellValue(((Integer)val).intValue());
        } else if ((val instanceof Long)) {
          ExcelUtils.log.debug("Long val = " + val);
          cell.setCellType(0);

          cell.setCellValue(((Long)val).longValue());
        } else if ((val instanceof Double)) {
          ExcelUtils.log.debug("Double val = " + val);
          cell.setCellType(0);

          cell.setCellValue(((Double)val).doubleValue());
        } else if ((val instanceof BigDecimal)) {
          ExcelUtils.log.debug("BigDecimal val = " + val);
          cell.setCellType(0);

          cell.setCellValue(((BigDecimal)val).intValue());
        } else if ((val instanceof Float)) {
          ExcelUtils.log.debug("Float val = " + val);
          cell.setCellType(0);
          cell.setCellValue(((Float)val).floatValue());
        } else if ((val instanceof java.util.Date)) {
          ExcelUtils.log.debug("Date val = " + val);

          cell.setCellType(1);
          String strDateVal = DateUtils.format((java.util.Date)val, "yyyy-MM-dd");
          cell.setCellValue(strDateVal);
        } else if ((val instanceof java.sql.Date)) {
          cell.setCellType(1);

          String strDateVal = DateUtils.format((java.sql.Date)val, "yyyy-MM-dd");
          cell.setCellValue(strDateVal);
        } else {
          ExcelUtils.log.debug("else val = " + val + ",val.class = " + val.getClass().getName());
          if (fieldType != Class.class)
            cell.setCellValue((String)fieldType.getMethod("setValue", new Class[] { Object.class }).invoke(null, new Object[] { val }));
          else
            cell.setCellValue((String)Class.forName(getClass().getName().replaceAll(getClass().getSimpleName(), "fieldtype." + val.getClass().getSimpleName() + "Type")).getMethod("setValue", new Class[] { Object.class }).invoke(null, new Object[] { val }));
        }
      }
      catch (Exception ex)
      {
        ExcelUtils.log.debug("Set cell value [" + row.getRowNum() + "," + column + "] error: " + ex.toString());
        cell.setCellValue(val.toString());
      }

      return cell;
    }

    public ExcelUtils.IExportExcel write(OutputStream os)
      throws IOException
    {
      this.wb.write(os);
      return this;
    }

    public ExcelUtils.IExportExcel write(HttpServletResponse response, String fileName)
      throws IOException
    {
      response.reset();
      response.setContentType("application/octet-stream; charset=utf-8");
      response.setHeader("Content-Disposition", "attachment; filename=" + EncoderUtils.urlEncode(fileName));
      write(response.getOutputStream());
      return this;
    }

    public File writeFile(String name)
      throws FileNotFoundException, IOException
    {
      File excelFile = new File(name);
      this.outputStream = new FileOutputStream(excelFile);
      write(this.outputStream);
      return excelFile;
    }

    public ExcelUtils.IExportExcel dispose()
    {
      try
      {
        if (this.outputStream != null) this.outputStream.close(); 
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
      return this;
    }
  }

  private static abstract interface IExportExcel
  {
    public abstract Row addRow();

    public abstract Cell addCell(Row paramRow, int paramInt, Object paramObject);

    public abstract File writeFile(String paramString)
      throws FileNotFoundException, IOException;

    public abstract IExportExcel write(HttpServletResponse paramHttpServletResponse, String paramString)
      throws IOException;

    public abstract IExportExcel dispose();
  }

  private static class ImportExcel
  {
    private Workbook wb;
    private Sheet sheet;
    private int headerNum;
    FileInputStream inputStream;

    public void dispose()
    {
    }

    public ImportExcel(String fileName, int headerNum)
      throws InvalidFormatException, IOException
    {
      this(new File(fileName), headerNum);
    }

    public ImportExcel(File file, int headerNum)
      throws InvalidFormatException, IOException
    {
      this(file, headerNum, 0);
    }

    public ImportExcel(String fileName, int headerNum, int sheetIndex)
      throws InvalidFormatException, IOException
    {
      this(new File(fileName), headerNum, sheetIndex);
    }

    public ImportExcel(File file, int headerNum, int sheetIndex)
      throws InvalidFormatException, IOException
    {
      this(file.getName(), new FileInputStream(file), headerNum, sheetIndex);
    }

    public ImportExcel(MultipartFile multipartFile, int headerNum, int sheetIndex)
      throws InvalidFormatException, IOException
    {
      this(multipartFile.getOriginalFilename(), multipartFile.getInputStream(), headerNum, sheetIndex);
    }

    public ImportExcel(String fileName, InputStream is, int headerNum, int sheetIndex)
      throws InvalidFormatException, IOException
    {
      if (StringUtils.isBlank(fileName))
        throw new RuntimeException("导入文档为空!");
      if (fileName.toLowerCase().endsWith("xls"))
        this.wb = new HSSFWorkbook(is);
      else if (fileName.toLowerCase().endsWith("xlsx"))
        this.wb = new XSSFWorkbook(is);
      else {
        throw new RuntimeException("文档格式不正确!");
      }
      if (this.wb.getNumberOfSheets() < sheetIndex) {
        throw new RuntimeException("文档中没有工作表!");
      }
      this.sheet = this.wb.getSheetAt(sheetIndex);
      this.headerNum = headerNum;
      ExcelUtils.log.debug("Initialize success.");
    }

    public Row getRow(int rownum)
    {
      return this.sheet.getRow(rownum);
    }

    public int getDataRowNum()
    {
      return this.headerNum + 1;
    }

    public int getLastDataRowNum()
    {
      return this.sheet.getLastRowNum() + this.headerNum;
    }

    public int getLastCellNum()
    {
      return getRow(this.headerNum).getLastCellNum();
    }

    private String getCellValue(Row row, int column)
    {
      String result = new String();
      Cell cell = row.getCell(column);
      if (cell == null) return "";
      switch (cell.getCellType()) {
      case 0:
        short _format = cell.getCellStyle().getDataFormat();

        if (HSSFDateUtil.isCellDateFormatted(cell)) {
          java.util.Date date = cell.getDateCellValue();
          if (date == null) return null;
          SimpleDateFormat sdf = null;
          if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm"))
            sdf = new SimpleDateFormat("HH:mm");
          else {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
          }
          result = sdf.format(date);
        } else if ((_format == 14) || (_format == 31) || (_format == 57) || (_format == 58)) {
          SimpleDateFormat sdf = null;
          if ((_format == 14) || (_format == 31) || (_format == 57) || (_format == 58))
          {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
          } else if ((_format == 20) || (_format == 32))
          {
            sdf = new SimpleDateFormat("HH:mm");
          }
          double value = cell.getNumericCellValue();
          java.util.Date date = DateUtil.getJavaDate(value);
          result = sdf.format(date);
        } else {
          double value = cell.getNumericCellValue();
          result = value + "";
        }
        break;
      case 1:
        result = cell.getRichStringCellValue() == null ? "" : cell.getRichStringCellValue().toString();
        break;
      case 2:
        result = cell.getNumericCellValue() + "";

        break;
      case 4:
        result = !cell.getBooleanCellValue() ? "false" : "true";
        break;
      case 3:
        result = "";
      default:
        result = cell.getStringCellValue();
      }

      return result;
    }

    public <E> List<E> parseExcel2Obj(Class cls, String[] arrRefFieldNames)
      throws InstantiationException, IllegalAccessException
    {
      List dataList = Lists.newArrayList();
      for (int i = getDataRowNum(); i < getLastDataRowNum(); i++) {
        Object e = cls.newInstance();
        Row row = getRow(i);
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < arrRefFieldNames.length; j++) {
          Object val = getCellValue(row, j);
          if (val != null)
          {
            Class valType = ClassUtils.getType(cls, arrRefFieldNames[j]);
            try
            {
              if (valType == String.class) {
                String s = String.valueOf(val.toString());
                if (StringUtils.endsWith(s, ".0"))
                  val = StringUtils.substringBefore(s, ".0");
                else
                  val = String.valueOf(val.toString());
              }
              else if (valType == Integer.class) {
                val = Integer.valueOf(Double.valueOf(val.toString()).intValue());
              } else if (valType == Long.class) {
                val = Long.valueOf(Double.valueOf(val.toString()).longValue());
              } else if (valType == Double.class) {
                val = Double.valueOf(val.toString());
              } else if (valType == Float.class) {
                val = Float.valueOf(val.toString());
              } else if (valType == java.util.Date.class) {
                val = DateUtil.getJavaDate(((Double)val).doubleValue());
              }
            } catch (Exception ex) {
              ExcelUtils.log.debug("Get cell value [" + i + "," + j + "] error: " + ex.toString());
              val = null;
            }

            ClassUtils.set(e, arrRefFieldNames[j], val);
          }
          sb.append(val + ", ");
        }
        dataList.add(e);
        ExcelUtils.log.debug("Read success: [" + i + "] " + sb.toString());
      }
      return dataList;
    }
  }
}
