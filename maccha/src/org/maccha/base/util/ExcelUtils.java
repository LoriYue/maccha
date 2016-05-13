package org.maccha.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Clob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import com.google.common.collect.Lists;

public class ExcelUtils {
	private static Logger log = LoggerFactory.getLogger(ExcelUtils.class);
	private static int EXCEL2003 = 2007 ;
	private static int EXCEL2010 = 2010 ;
	
	/**
	 * 读取excel数据,根据excel和对象属性对照关系生成 
	 * 
	 * @param excelFile excel文件名称，包括路径
	 * 
	 */
	public static List<Map> parseExcel2Map(String excelFile) throws Exception {
		List<Map> list = new ArrayList<Map>();
		ImportExcel ei = new ImportExcel(excelFile, 0);
		String[] arryColHead = new String[ei.getLastCellNum()];
		Row rowHead = ei.getRow(0);
		for (int j = 0; j < ei.getLastCellNum(); j++) {
			arryColHead[j] =  ei.getCellValue(rowHead, j);
		}
		for (int i = ei.getDataRowNum(); i <= ei.getLastDataRowNum(); i++) {
			Row row = ei.getRow(i);
			if(row == null)continue;
			Map<String,String> rowMap = new java.util.LinkedHashMap<String,String>();
			for (int j = 0; j < arryColHead.length; j++) {
				String val = ei.getCellValue(row, j);
				if(arryColHead[j] == null||arryColHead[j].trim().length() == 0)continue ;
				rowMap.put(arryColHead[j].replaceAll(" ", ""), val);
			}
			list.add(rowMap);
		}
		return list ;
	}
	/**
	 * 读取excel数据,根据excel和对象属性对照关系生成 
	 * 
	 * @param excelFile excel文件名称，包括路径
	 * @cls 要成的类
	 * @param refFieldName excel和对象属性对照关系, 索引:excel列索引，值:po对象属性名称
	 */
	public static List parseExcel2Obj(String excelFile, Class cls, String[] arrRefFieldNames) throws Exception {
		return parseExcel2Obj(new File(excelFile), cls, arrRefFieldNames);
	}

	/**
	 * 读取excel数据,根据excel和对象属性对照关系生成
	 * 
	 * @param excelFile excel文件名称，包括路径
     * @param cls 导入对象类型
	 * @param refFieldName excel和对象属性对照关系, 索引:excel列索引，值:po对象属性名称
	 */
	public static <E> List<E> parseExcel2Obj(File excelFile, Class cls, String[] arrRefFieldNames) throws Exception {
		ImportExcel ei = new ImportExcel(excelFile,1);
		return ei.parseExcel2Obj(cls, arrRefFieldNames);
	}
	/**
	 * 读取excel数据,根据excel和对象属性对照关系生成
	 * 
	 * @param excelFile excel文件名称，包括路径
     * @param cls 导入对象类型
	 * @param refFieldName excel和对象属性对照关系, 索引:excel列索引，值:po对象属性名称
	 */
	public static <E> List<E> parseExcel2Obj(MultipartFile multipartFile, Class cls, String[] arrRefFieldNames) throws Exception {
		List<E> dataList = Lists.newArrayList();
		try{
			ImportExcel ei = new ImportExcel(multipartFile,1,1);
			return ei.parseExcel2Obj(cls, arrRefFieldNames);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return dataList;
	}
	
	/**
	 * 导出Excel 功能
	 * @param listPo value 可以是PO对象也可以是Map键值对
	 * @param excelHeads key:对象属性名称，value: 属性对应的中文描述（输出到excel的第一行）
	 * @return 返回Excel文件
	 */
	public static void parseList2Excel(List listPo,Map<String,String> excelHeads,HttpServletResponse response, String fileName ) throws Exception{
		IExportExcel _exportExcel = null ;
		try{
			_exportExcel = parseList2Excel(listPo,excelHeads,EXCEL2010);
			_exportExcel.write(response, fileName);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			_exportExcel.dispose();
		}
		
	}
	/**
	 * 导出Excel 功能
	 * @param listPo value 可以是PO对象也可以是Map键值对
	 * @param excelHeads key:对象属性名称，value: 属性对应的中文描述（输出到excel的第一行）
	 * @return 返回Excel文件
	 */
	public static File parseList2Excel(List listPo,Map<String,String> excelHeads,String excelFilePath ) throws Exception{
		IExportExcel _exportExcel = null ;
		File _fileExcel = null ;
		try{
			_exportExcel = parseList2Excel(listPo,excelHeads,EXCEL2010);
			_fileExcel = _exportExcel.writeFile(excelFilePath);
		}catch(Exception ex){
			ex.printStackTrace();
			BizException.handleMessageException("export error!", ex.getCause());
		}finally{
			try{
				_exportExcel.dispose();
			}catch(Exception ex){}
		}
		return _fileExcel ;
	}
	
	/**
	 * 导出Excel 功能
	 * @param listPo value 可以是PO对象也可以是Map键值对
	 * @param excelHeads key:对象属性名称，value: 属性对应的中文描述（输出到excel的第一行）
	 * @return 返回Excel文件
	 */
	public static File parseList2Excel2003(List listPo,Map<String,String> excelHeads,String excelFilePath ) throws Exception{
		IExportExcel _exportExcel = null ;
		File _fileExcel = null ;
		try{
			_exportExcel = parseList2Excel(listPo,excelHeads,EXCEL2003);
			_fileExcel = _exportExcel.writeFile(excelFilePath);
		}catch(Exception ex){
			ex.printStackTrace();
			BizException.handleMessageException("export error!", ex.getCause());
		}finally{
			try{
				_exportExcel.dispose();
			}catch(Exception ex){}
		}
		return _fileExcel ;
	}
	/**
	 * 导出Excel 功能
	 * @param listPo value 可以是PO对象也可以是Map键值对
	 * @param excelHeads key:对象属性名称，value: 属性对应的中文描述（输出到excel的第一行）
	 * @return 返回Excel文件
	 */
	private static IExportExcel parseList2Excel(List listPo,Map<String,String> excelHeads,int intExcelFormat) throws Exception{
		List<String> headerList = Lists.newArrayList();
		int colNumber = 0 ;
		Iterator<String> itrKey = excelHeads.keySet().iterator() ;
		List<String> listFields = new ArrayList();
		String strKey = null ;
		while(itrKey.hasNext()){
			strKey = itrKey.next() ;
			listFields.add(strKey);
			headerList.add(excelHeads.get(strKey));
			colNumber++ ;
		}
		IExportExcel _exportExcel = null;
		if(intExcelFormat == EXCEL2003){
			_exportExcel = new ExportExcel2003("", headerList);
		}else {
			_exportExcel = new ExportExcel("", headerList);
		}
		String strPropName = null ;
		Object objPropValue = null ;
		String strPropValue = null ;
		for (int i = 0; i < listPo.size(); i++) {
			//System.out.println(i+"");
			Object objPoEntity  = listPo.get(i);
			Row row = _exportExcel.addRow();
			for(int j = 0 ; j < listFields.size();j++){
				strPropName = (String)listFields.get(j);
				if(objPoEntity instanceof Map){
					objPropValue = ((Map)objPoEntity).get(strPropName);
				}else{
					objPropValue = ClassUtils.get(objPoEntity, strPropName);
				}
				if(objPropValue != null && objPropValue instanceof Date){
					strPropValue = DateUtils.format((Date)objPropValue,DateUtils.YMDHMS_PATTERN);
					_exportExcel.addCell(row, j, strPropValue);
				}else if(objPropValue != null && objPropValue instanceof java.util.Date){
					strPropValue = DateUtils.format((java.util.Date)objPropValue,DateUtils.YMDHMS_PATTERN);
					_exportExcel.addCell(row, j, strPropValue);
				}else if (objPropValue != null && objPropValue instanceof Clob) {
		            Clob _obj1= (Clob)objPropValue ;
		            strPropValue = (String)TypeConvertorUtils.convert(_obj1,Clob.class, String.class);
		            _exportExcel.addCell(row, j, strPropValue);
		        }else if (objPropValue != null && objPropValue instanceof java.sql.Timestamp) {
		        	Object _obj = TypeConvertorUtils.convert(objPropValue,java.util.Date.class);
		        	strPropValue = DateUtils.format((Date)_obj,DateUtils.YMDHMS_PATTERN);
		        	_exportExcel.addCell(row, j, strPropValue);
		        }else if(objPropValue != null && objPropValue instanceof BigDecimal){
		        	Object _obj =TypeConvertorUtils.convert(objPropValue,Double.class);
		        	_exportExcel.addCell(row, j, _obj);
				}else{
					_exportExcel.addCell(row, j, objPropValue == null?"":objPropValue);
		        }
			}
		}
		return _exportExcel ;
	}
	/**
	 * 导入Excel文件（支持“XLS”和“XLSX”格式）
	 * @author ThinkGem
	 * @version 2013-03-10
	 */
	private static class ImportExcel {
		/**
		 * 工作薄对象
		 */
		private Workbook wb;
		
		/**
		 * 工作表对象
		 */
		private Sheet sheet;
		
		/**
		 * 标题行号
		 */
		private int headerNum;
		
		/**
		 * 输入流
		 */
		FileInputStream inputStream ;
		
		public void dispose() {
			//wb.
		}
		/**
		 * 构造函数
		 * @param path 导入文件，读取第一个工作表
		 * @param headerNum 标题行号，数据行号=标题行号+1
		 * @throws InvalidFormatException 
		 * @throws IOException 
		 */
		public ImportExcel(String fileName, int headerNum) throws InvalidFormatException, IOException {
			this(new File(fileName), headerNum);
		}
		
		/**
		 * 构造函数
		 * @param path 导入文件对象，读取第一个工作表
		 * @param headerNum 标题行号，数据行号=标题行号+1
		 * @throws InvalidFormatException 
		 * @throws IOException 
		 */
		public ImportExcel(File file, int headerNum) 
				throws InvalidFormatException, IOException {
			this(file, headerNum, 0);
		}

		/**
		 * 构造函数
		 * @param path 导入文件
		 * @param headerNum 标题行号，数据行号=标题行号+1
		 * @param sheetIndex 工作表编号
		 * @throws InvalidFormatException 
		 * @throws IOException 
		 */
		public ImportExcel(String fileName, int headerNum, int sheetIndex) 
				throws InvalidFormatException, IOException {
			this(new File(fileName), headerNum, sheetIndex);
		}
		
		/**
		 * 构造函数
		 * @param path 导入文件对象
		 * @param headerNum 标题行号，数据行号=标题行号+1
		 * @param sheetIndex 工作表编号
		 * @throws InvalidFormatException 
		 * @throws IOException 
		 */
		public ImportExcel(File file, int headerNum, int sheetIndex) 
				throws InvalidFormatException, IOException {
			this(file.getName(), new FileInputStream(file), headerNum, sheetIndex);
		}
		
		/**
		 * 构造函数
		 * @param file 导入文件对象
		 * @param headerNum 标题行号，数据行号=标题行号+1
		 * @param sheetIndex 工作表编号
		 * @throws InvalidFormatException 
		 * @throws IOException 
		 */
		public ImportExcel(MultipartFile multipartFile, int headerNum, int sheetIndex) 
				throws InvalidFormatException, IOException {
			this(multipartFile.getOriginalFilename(), multipartFile.getInputStream(), headerNum, sheetIndex);
		}

		/**
		 * 构造函数
		 * @param path 导入文件对象
		 * @param headerNum 标题行号，数据行号=标题行号+1
		 * @param sheetIndex 工作表编号
		 * @throws InvalidFormatException 
		 * @throws IOException 
		 */
		public ImportExcel(String fileName, InputStream is, int headerNum, int sheetIndex) 
				throws InvalidFormatException, IOException {
			
			if (StringUtils.isBlank(fileName)){
				throw new RuntimeException("导入文档为空!");
			}else if(fileName.toLowerCase().endsWith("xls")){    
				this.wb = new HSSFWorkbook(is);    
	        }else if(fileName.toLowerCase().endsWith("xlsx")){  
	        	this.wb = new XSSFWorkbook(is);
	        }else{  
	        	throw new RuntimeException("文档格式不正确!");
	        }  
			if (this.wb.getNumberOfSheets()<sheetIndex){
				throw new RuntimeException("文档中没有工作表!");
			}
			this.sheet = this.wb.getSheetAt(sheetIndex);
			this.headerNum = headerNum;
			log.debug("Initialize success.");
		}
		
		/**
		 * 获取行对象
		 * @param rownum
		 * @return
		 */
		public Row getRow(int rownum){
			return this.sheet.getRow(rownum);
		}

		/**
		 * 获取数据行号
		 * @return
		 */
		public int getDataRowNum(){
			return headerNum+1;
		}
		
		/**
		 * 获取最后一个数据行号
		 * @return
		 */
		public int getLastDataRowNum(){
			return this.sheet.getLastRowNum()+headerNum;
		}
		
		/**
		 * 获取最后一个列号
		 * @return
		 */
		public int getLastCellNum(){
			return this.getRow(headerNum).getLastCellNum();
		}
		
		/**
		 * 获取单元格值
		 * @param row 获取的行
		 * @param column 获取单元格列号
		 * @return 单元格值
		 */
//		public Object getCellValue(Row row, int column){
//			Object val = "";
//			try{
//				Cell cell = row.getCell(column);
//				if (cell != null){
//					log.debug("cell.getCellType() = "+cell.getCellType());
//					if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
//						val = cell.getNumericCellValue();
//					}else if (cell.getCellType() == Cell.CELL_TYPE_STRING){
//						val = cell.getStringCellValue();
//					}else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA){
//						val = cell.getCellFormula();
//					}else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){
//						val = cell.getBooleanCellValue();
//					}else if(cell.getCellType() == Cell.CELL_TYPE_BLANK){
//						val = cell.getStringCellValue();
//					}else if (cell.getCellType() == Cell.CELL_TYPE_ERROR){
//						val = cell.getErrorCellValue();
//					}
//				}
//			}catch (Exception e) {
//				return val;
//			}
//			return val;
//		}
		
		private String getCellValue(Row row, int column) {  
	        String result = new String(); 
	        Cell cell = row.getCell(column);
	        if(cell == null )return "";
	        switch (cell.getCellType()) {  
	        case HSSFCell.CELL_TYPE_NUMERIC:// 数字类型  
	        	short _format = cell.getCellStyle().getDataFormat();
	        	//处理日期格式、时间格式 
	            if (HSSFDateUtil.isCellDateFormatted(cell)) { 
	            	Date date = cell.getDateCellValue();
	            	if(date == null )return null ;
	                SimpleDateFormat sdf = null;  
	                if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {  
	                    sdf = new SimpleDateFormat("HH:mm");  
	                } else {// 日期  
	                    sdf = new SimpleDateFormat("yyyy-MM-dd");  
	                }  
	                result = sdf.format(date);  
	            } else if(_format == 14 || _format == 31 || _format == 57 || _format == 58){  
	            	SimpleDateFormat sdf = null;
	            	if(_format == 14 || _format == 31 || _format == 57 || _format == 58){
	            		//日期
	            		sdf = new SimpleDateFormat("yyyy-MM-dd");
	            	}else if (_format == 20 || _format == 32) {
	            		//时间
	            		sdf = new SimpleDateFormat("HH:mm");
	            	}
	            	double value = cell.getNumericCellValue();
	            	Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
	            	result = sdf.format(date);
	            } else {  
	                double value = cell.getNumericCellValue();    
	                result = value+"";
	            }  
	            break;  
	        case HSSFCell.CELL_TYPE_STRING:// String类型  
	            result = cell.getRichStringCellValue()==null ?"":cell.getRichStringCellValue().toString();  
	            break;  
	        case HSSFCell.CELL_TYPE_FORMULA:// FORMULA类型 (计算列类型)
	        	result = cell.getNumericCellValue()+"";
	            //result = cell.getCellFormula() == null?"":cell.getCellFormula().toString();  
	            break;
	        case HSSFCell.CELL_TYPE_BOOLEAN:// Boolean类型  
	            result = cell.getBooleanCellValue() == false?"false":"true";
	            break;    
	        case HSSFCell.CELL_TYPE_BLANK:  
	            result = "";  
	        default:
	            result = cell.getStringCellValue();
	            break; 
	        }  
	        return result;  
	    }  
		/**
		 * 获取导入数据列表
		 * @param cls 导入对象类型
		 * @param refFieldName excel和对象属性对照关系, 索引:excel列索引，值:po对象属性名称
		 */
		public <E> List<E> parseExcel2Obj(Class cls, String[] arrRefFieldNames) throws InstantiationException, IllegalAccessException{
			// Get excel data
			List<E> dataList = Lists.newArrayList();
			for (int i = this.getDataRowNum(); i < this.getLastDataRowNum(); i++) {
				E e = (E)cls.newInstance();
				Row row = this.getRow(i);
				StringBuilder sb = new StringBuilder();
				for(int j =0 ;j <arrRefFieldNames.length;j++ ){
					Object val = this.getCellValue(row, j);
					if (val != null){
						// Get param type and type cast
						Class<?> valType = ClassUtils.getType(cls, arrRefFieldNames[j]);
						//log.debug("Import value type: ["+i+","+column+"] " + valType);
						try {
							if (valType == String.class){
								String s = String.valueOf(val.toString());
								if(StringUtils.endsWith(s, ".0")){
									val = StringUtils.substringBefore(s, ".0");
								}else{
									val = String.valueOf(val.toString());
								}
							}else if (valType == Integer.class){
								val = Double.valueOf(val.toString()).intValue();
							}else if (valType == Long.class){
								val = Double.valueOf(val.toString()).longValue();
							}else if (valType == Double.class){
								val = Double.valueOf(val.toString());
							}else if (valType == Float.class){
								val = Float.valueOf(val.toString());
							}else if (valType == Date.class){
								val = DateUtil.getJavaDate((Double)val);
							}
						} catch (Exception ex) {
							log.debug("Get cell value ["+i+","+j+"] error: " + ex.toString());
							val = null;
						}
						// set entity value
						ClassUtils.set(e, arrRefFieldNames[j], val);
					}
					sb.append(val+", ");
				}
				dataList.add(e);
				log.debug("Read success: ["+i+"] "+sb.toString());
			}
			return dataList;
		}
	}
	private interface IExportExcel{
		public Row addRow();
		public Cell addCell(Row row, int column, Object val);
		public File writeFile(String name) throws FileNotFoundException, IOException ; 
		public IExportExcel write(HttpServletResponse response, String fileName) throws IOException ;
		public IExportExcel dispose() ;
	}
	public static class ExportExcel2003 implements IExportExcel{
		/**
		 * 工作薄对象
		 */
		private HSSFWorkbook  wb;
		
		/**
		 * 工作表对象
		 */
		private HSSFSheet sheet;
		
		/**
		 * 当前行号
		 */
		private int rownum;
		
		/**
		 * 输出流
		 */
		FileOutputStream outputStream ;
		/**
		 * 构造函数
		 * @param title 表格标题，传“空值”，表示无标题
		 * @param headers 表头数组
		 */
		public ExportExcel2003(String title, String[] headers) {
			initialize(title, Lists.newArrayList(headers));
		}
		
		/**
		 * 构造函数
		 * @param title 表格标题，传“空值”，表示无标题
		 * @param headerList 表头列表
		 */
		public ExportExcel2003(String title, List<String> headerList) {
			initialize(title, headerList);
		}

		/**
		 * 初始化函数
		 * @param title 表格标题，传“空值”，表示无标题
		 * @param headerList 表头列表
		 */
		private void initialize(String title, List<String> headerList) {
			this.wb = new HSSFWorkbook();
			this.sheet = wb.createSheet();
			// Create header
			if (headerList == null){
				throw new RuntimeException("headerList not null!");
			}
			HSSFFont font = this.wb.createFont();  
	        font.setColor(HSSFFont.COLOR_NORMAL);
	        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			HSSFCellStyle cellStyle = this.wb.createCellStyle();//创建格式
            cellStyle.setFont(font);
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			//创建第一行标题 
            HSSFRow titleRow = this.sheet.createRow(rownum++);//第一行标题
            for(int i = 0,size = headerList.size(); i < size; i++){//创建第1行标题单元格    
            	this.sheet.setColumnWidth(i,4000);
                HSSFCell cell = titleRow.createCell(i,0);        
                cell.setCellStyle(cellStyle);
                cell.setCellValue(headerList.get(i));
            }
			log.debug("Initialize success.");
		}

		/**
		 * 添加一行
		 * @return 行对象
		 */
		public Row addRow(){
			return sheet.createRow(rownum++);
		}
		

		/**
		 * 添加一个单元格
		 * @param row 添加的行
		 * @param column 添加列号
		 * @param val 添加值
		 * @return 单元格对象
		 */
		public Cell addCell(Row row, int column, Object val){
			return this.addCell(row, column, val, 0, Class.class);
		}
		
		/**
		 * 添加一个单元格
		 * @param row 添加的行
		 * @param column 添加列号
		 * @param val 添加值
		 * @param align 对齐方式（1：靠左；2：居中；3：靠右）
		 * @return 单元格对象
		 */
		public Cell addCell(Row row, int column, Object val, int align, Class<?> fieldType){
			Cell cell = row.createCell(column);
			//CellStyle cellStyle = this.wb.createCellStyle();  
			try {
				if (val == null){
					cell.setCellValue("");
				} else if (val instanceof String) {
					log.debug("String val = " + val);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue((String) val);
				} else if (val instanceof Integer) {
					log.debug("Integer val = " + val);
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					//cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
					cell.setCellValue((Integer) val);
				} else if (val instanceof Long) {
					log.debug("Long val = " + val);
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					//cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
					cell.setCellValue((Long) val);
				} else if (val instanceof Double) {
					log.debug("Double val = " + val);
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					//cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
					cell.setCellValue((Double)val);
				} else if(val instanceof BigDecimal){
					log.debug("BigDecimal val = " + val);
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					//cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
		        	cell.setCellValue(((BigDecimal) val).intValue());
				}else if (val instanceof Float) {
					log.debug("Float val = " + val);
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue((Float) val);
				} else if (val instanceof Date) {
					log.debug("Date val = " + val);
					//DataFormat format = wb.createDataFormat();
					//cellStyle.setDataFormat(format.getFormat("yyyy-MM-dd"));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					String strDateVal =DateUtils.format((Date) val, DateUtils.YEAR_MONTH_DAY_PATTERN_MIDLINE);
					cell.setCellValue(strDateVal);
				}else if (val instanceof java.sql.Date) {
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					//DataFormat format = wb.createDataFormat();
					//cellStyle.setDataFormat(format.getFormat("yyyy-MM-dd"));
					String strDateVal =DateUtils.format((java.sql.Date) val, DateUtils.YEAR_MONTH_DAY_PATTERN_MIDLINE);
					cell.setCellValue(strDateVal);
				} else {
					log.debug("else val = " + val +",val.class = "+val.getClass().getName());
					if (fieldType != Class.class){
						cell.setCellValue((String)fieldType.getMethod("setValue", Object.class).invoke(null, val));
					}else{
						cell.setCellValue((String)Class.forName(this.getClass().getName().replaceAll(this.getClass().getSimpleName(), 
							"fieldtype."+val.getClass().getSimpleName()+"Type")).getMethod("setValue", Object.class).invoke(null, val));
					}
				}
			} catch (Exception ex) {
				log.debug("Set cell value ["+row.getRowNum()+","+column+"] error: " + ex.toString());
				cell.setCellValue(val.toString());
			}
			//cell.setCellStyle(cellStyle);
			return cell;
		}
		/**
		 * 输出数据流
		 * @param os 输出数据流
		 */
		public IExportExcel write(OutputStream os) throws IOException{
			wb.write(os);
			return this;
		}
		/**
		 * 输出到客户端
		 * @param fileName 输出文件名
		 */
		public IExportExcel write(HttpServletResponse response, String fileName) throws IOException{
			response.reset();
	        response.setContentType("application/octet-stream; charset=utf-8");
	        response.setHeader("Content-Disposition", "attachment; filename="+EncoderUtils.urlEncode(fileName));
			write(response.getOutputStream());
			return this;
		}
		
		/**
		 * 输出到文件
		 * @param fileName 输出文件名
		 */
		public File writeFile(String name) throws FileNotFoundException, IOException{
			File excelFile = new File(name);
			outputStream = new FileOutputStream(excelFile);
			this.write(outputStream);
			return excelFile;
		}
		
		/**
		 * 清理临时文件
		 */
		public IExportExcel dispose(){
			try{
				if(outputStream != null)outputStream.close();
			}catch(Exception ex){
				ex.printStackTrace();
			}
			return this;
		}
	}
	/**
	 * 导出Excel文件（导出“XLSX”格式，支持大数据量导出   @see org.apache.poi.ss.SpreadsheetVersion）
	 * @author ThinkGem
	 * @version 2013-04-21
	 */
	public static class ExportExcel implements IExportExcel{
		private static int rowaccess = 100;
				
		/**
		 * 工作薄对象
		 */
		private SXSSFWorkbook wb;
		
		/**
		 * 工作表对象
		 */
		private SXSSFSheet sheet;
		
		/**
		 * 样式列表
		 */
		private Map<String, CellStyle> styles;
		
		/**
		 * 当前行号
		 */
		private int rownum;
		/**
		 * 输出流
		 */
		FileOutputStream outputStream ;
		/**
		 * 构造函数
		 * @param title 表格标题，传“空值”，表示无标题
		 * @param headers 表头数组
		 */
		public ExportExcel(String title, String[] headers) {
			initialize(title, Lists.newArrayList(headers));
		}
		
		/**
		 * 构造函数
		 * @param title 表格标题，传“空值”，表示无标题
		 * @param headerList 表头列表
		 */
		public ExportExcel(String title, List<String> headerList) {
			initialize(title, headerList);
		}

		/**
		 * 初始化函数
		 * @param title 表格标题，传“空值”，表示无标题
		 * @param headerList 表头列表
		 */
		private void initialize(String title, List<String> headerList) {
			this.wb = new SXSSFWorkbook();
			// temp files will be gzipped
			this.wb.setCompressTempFiles(true); 		
			this.sheet = (SXSSFSheet)wb.createSheet("Export");
			this.sheet.setRandomAccessWindowSize(rowaccess);
			// Create title
			if (StringUtils.isNotBlank(title)){
				Row titleRow = sheet.createRow(rownum++);
				titleRow.setHeightInPoints(30);
				Cell titleCell = titleRow.createCell(0);
				titleCell.setCellStyle(styles.get("title"));
				titleCell.setCellValue(title);
				sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(),
						titleRow.getRowNum(), titleRow.getRowNum(), headerList.size()-1));
			}
			// Create header
			if (headerList == null){
				throw new RuntimeException("headerList not null!");
			}
			Row headerRow = sheet.createRow(rownum++);
			headerRow.setHeightInPoints(16);
			for (int i = 0; i < headerList.size(); i++) {
				Cell cell = headerRow.createCell(i);
				//cell.setCellStyle(styles.get("header"));
				String[] ss = StringUtils.split(headerList.get(i), "**", 2);
				if (ss.length==2){
					cell.setCellValue(ss[0]);
					Comment comment = this.sheet.createDrawingPatriarch().createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6));
					comment.setString(new XSSFRichTextString(ss[1]));
					cell.setCellComment(comment);
				}else{
					cell.setCellValue(headerList.get(i));
				}
				sheet.autoSizeColumn(i);
			}
			for (int i = 0; i < headerList.size(); i++) {  
				int colWidth = sheet.getColumnWidth(i)*2;
		        sheet.setColumnWidth(i, colWidth < 3000 ? 3000 : colWidth);  
			}
			log.debug("Initialize success.");
		}
		


		/**
		 * 添加一行
		 * @return 行对象
		 */
		public Row addRow(){
			return sheet.createRow(rownum++);
		}
		

		/**
		 * 添加一个单元格
		 * @param row 添加的行
		 * @param column 添加列号
		 * @param val 添加值
		 * @return 单元格对象
		 */
		public Cell addCell(Row row, int column, Object val){
			return this.addCell(row, column, val, 0, Class.class);
		}
		
		/**
		 * 添加一个单元格
		 * @param row 添加的行
		 * @param column 添加列号
		 * @param val 添加值
		 * @param align 对齐方式（1：靠左；2：居中；3：靠右）
		 * @return 单元格对象
		 */
		public Cell addCell(Row row, int column, Object val, int align, Class<?> fieldType){
			Cell cell = row.createCell(column);
			//CellStyle cellStyle = this.wb.createCellStyle();  
			try {
				if (val == null){
					cell.setCellValue("");
				} else if (val instanceof String) {
					log.debug("String val = " + val);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue((String) val);
				} else if (val instanceof Integer) {
					log.debug("Integer val = " + val);
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					//cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
					cell.setCellValue((Integer) val);
				} else if (val instanceof Long) {
					log.debug("Long val = " + val);
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					//cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
					cell.setCellValue((Long) val);
				} else if (val instanceof Double) {
					log.debug("Double val = " + val);
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					//cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
					cell.setCellValue((Double)val);
				} else if(val instanceof BigDecimal){
					log.debug("BigDecimal val = " + val);
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					//cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
		        	cell.setCellValue(((BigDecimal) val).intValue());
				}else if (val instanceof Float) {
					log.debug("Float val = " + val);
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue((Float) val);
				} else if (val instanceof Date) {
					log.debug("Date val = " + val);
					//DataFormat format = wb.createDataFormat();
					//cellStyle.setDataFormat(format.getFormat("yyyy-MM-dd"));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					String strDateVal =DateUtils.format((Date) val, DateUtils.YEAR_MONTH_DAY_PATTERN_MIDLINE);
					cell.setCellValue(strDateVal);
				}else if (val instanceof java.sql.Date) {
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					//DataFormat format = wb.createDataFormat();
					//cellStyle.setDataFormat(format.getFormat("yyyy-MM-dd"));
					String strDateVal =DateUtils.format((java.sql.Date) val, DateUtils.YEAR_MONTH_DAY_PATTERN_MIDLINE);
					cell.setCellValue(strDateVal);
				} else {
					log.debug("else val = " + val +",val.class = "+val.getClass().getName());
					if (fieldType != Class.class){
						cell.setCellValue((String)fieldType.getMethod("setValue", Object.class).invoke(null, val));
					}else{
						cell.setCellValue((String)Class.forName(this.getClass().getName().replaceAll(this.getClass().getSimpleName(), 
							"fieldtype."+val.getClass().getSimpleName()+"Type")).getMethod("setValue", Object.class).invoke(null, val));
					}
				}
			} catch (Exception ex) {
				log.debug("Set cell value ["+row.getRowNum()+","+column+"] error: " + ex.toString());
				cell.setCellValue(val.toString());
			}
			//cell.setCellStyle(cellStyle);
			return cell;
		}
		/**
		 * 输出数据流
		 * @param os 输出数据流
		 */
		public IExportExcel write(OutputStream os) throws IOException{
			wb.write(os);
			return this;
		}
		
		/**
		 * 输出到客户端
		 * @param fileName 输出文件名
		 */
		public IExportExcel write(HttpServletResponse response, String fileName) throws IOException{
			response.reset();
	        response.setContentType("application/octet-stream; charset=utf-8");
	        response.setHeader("Content-Disposition", "attachment; filename="+EncoderUtils.urlEncode(fileName));
			write(response.getOutputStream());
			return this;
		}
		
		/**
		 * 输出到文件
		 * @param fileName 输出文件名
		 */
		public File writeFile(String name) throws FileNotFoundException, IOException{
			File excelFile = new File(name);
			FileOutputStream os = new FileOutputStream(excelFile);
			this.write(os);
			return excelFile;
		}
		
		/**
		 * 清理临时文件
		 */
		public ExportExcel dispose(){
			try{
				if(outputStream != null)outputStream.close();
				// dispose of temporary files backing this workbook on disk
				wb.dispose();
			}catch(Exception ex){
				ex.printStackTrace();
			}
			return this;
		}
	}
}
