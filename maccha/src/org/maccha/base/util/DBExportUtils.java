package org.maccha.base.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang.StringEscapeUtils;

public class DBExportUtils {
	private Connection conn = null;
	private String DATA_SOURCE_JDBC_DRIVER = null ;
	private String DATA_SOURCE_JDBC_URL = null;
	private String DATA_SOURCE_JDBC_USER_NAME = null ;
	private String DATA_SOURCE_JDBC_PASSWORD = null ;
	
	public static DBExportUtils getInstance(String jdbcDriver,String jdbcURL,String jdbcUserName,String jdbcPassword){
		return new DBExportUtils(jdbcDriver,jdbcURL,jdbcUserName,jdbcPassword);
	}
	public static DBExportUtils getInstance(Connection _conn){
		return new DBExportUtils(_conn);
	}	
	public DBExportUtils(Connection _conn){
		this.conn = _conn ;
		try{
			this.DATA_SOURCE_JDBC_DRIVER = this.conn.getMetaData().getDriverName() ;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	public DBExportUtils(String jdbcDriver,String jdbcURL,String jdbcUserName,String jdbcPassword){
		this.DATA_SOURCE_JDBC_DRIVER = jdbcDriver ;
		this.DATA_SOURCE_JDBC_URL  = jdbcURL ; 
		this.DATA_SOURCE_JDBC_USER_NAME = jdbcUserName ;
		this.DATA_SOURCE_JDBC_PASSWORD = jdbcPassword ;
		this.conn = this.getConnection();
	}	
	public Connection getConnection(){
		Connection _conn = null;
		try {
			Class.forName(this.DATA_SOURCE_JDBC_DRIVER).newInstance();
			_conn = DriverManager.getConnection(this.DATA_SOURCE_JDBC_URL, this.DATA_SOURCE_JDBC_USER_NAME, this.DATA_SOURCE_JDBC_PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _conn;
	}

	public  void colseConnection() {
		try {
			if ((this.conn != null) && (!this.conn.isClosed())) {
				try {
					this.conn.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			this.conn = null;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * 导出一个表数据到文件
	 * @param tableName 表名
	 * @param cols 列名数组，为空则导出全部列名
	 * @param filePath sql文件存储绝对路径
	 */
	public  void exportOneTable2File(String tableName,String[] cols,String filePath){
        	try{
        		if(filePath != null)this.writeFile(_exportTable(tableName,cols), filePath);
        	}catch (Exception ex) {
                ex.printStackTrace();
            }finally{
            	colseConnection();
        	}
	}
	/**
	 * 导出多个表数据到文件
	 * @param tableNames 表名数组
	 * @param filePath sql文件存储绝对路径
	 */
	public  void exportMoreTable2File(String[] tableNames,String filePath){
		StringBuffer buffSQL = new StringBuffer();
		for(int i=0;i<tableNames.length;i++){
			buffSQL.append(_exportTable(tableNames[i],null));
		}
		try{
    		if(filePath != null)this.writeFile(buffSQL.toString(), filePath);
    	}catch (Exception ex) {
            ex.printStackTrace();
        }finally{
        	colseConnection();
    	}
	}
	
	private String _exportTable(String tableName,String[] cols){
		StringBuffer buffSQL = new StringBuffer();
        PreparedStatement preStm = null;
        ResultSet resultSet = null;
        HashMap mapColsType = new HashMap();
        try {
            //编译生成Select SQL 语句
            String query = "select * from " + tableName;
            //预处理SQL语句
            preStm = this.conn.prepareStatement(query);
            //执行查询语句
            resultSet = preStm.executeQuery();

            ArrayList arryColumnList = new ArrayList();
            ArrayList arryColumnType = new ArrayList();
            
            if (!resultSet.next()) {
                return "";
            } else {
                java.sql.ResultSetMetaData metaData = resultSet.getMetaData();
                //数据库列名
                String strColumnName = null;
                //数据库数据类型
                int type;
                int count = metaData.getColumnCount();
                for (int i = 1; i < (count + 1); i++) {
                    //通过结果集获取字段名称,字段类型
                    strColumnName = metaData.getColumnName(i);
                    type = metaData.getColumnType(i);
                    arryColumnList.add(strColumnName);
                    mapColsType.put(strColumnName.toLowerCase(), type+"");
                }
                if(cols == null){
                	cols = new String[arryColumnList.size()] ;
                	for(int j=0 ;j < arryColumnList.size();j++){
                		cols[j]=(String)arryColumnList.get(j);
                	}
                }
            }
            int count = cols.length ;
            StringBuffer buffInsertSQL = new StringBuffer("insert into "+tableName+"(") ;
            for (int i = 0; i < count; i++) {
            	if(i==count-1){
            		buffInsertSQL.append(cols[i]+") values (");
            	}else{
            		buffInsertSQL.append(cols[i]+",");
            	}
            }
            do {
            	buffSQL.append(buffInsertSQL.toString());
                for (int i = 0; i < count; i++) {
                	Object objValue = resultSet.getObject(cols[i]) ;
                	if(objValue == null){
                		int intType = Integer.parseInt((String)mapColsType.get(cols[i].toLowerCase()));
	                	switch(intType){ 
	                        case  Types.LONGVARCHAR://-1 
	                        		buffSQL.append("0");
	                                break; 
	                        case  Types.NUMERIC://2 
	                        		buffSQL.append("0");
	                                break; 
	                        default:
	                        		buffSQL.append("null");
	                	}
                	}else{
                		int intType = Integer.parseInt((String)mapColsType.get(cols[i].toLowerCase()));
	                	switch(intType){ 
	                        case  Types.LONGVARCHAR://-1 
	                        		buffSQL.append("'"+StringEscapeUtils.escapeSql(objValue.toString())+"'");
	                                break; 
	                        case  Types.CHAR://1 
	                        		buffSQL.append("'"+StringEscapeUtils.escapeSql(objValue.toString())+"'");
	                                break; 
	                        case  Types.NUMERIC://2 
	                        		buffSQL.append(objValue);
	                                break; 
	                        case  Types.VARCHAR://12 
	                        		buffSQL.append("'"+StringEscapeUtils.escapeSql(objValue.toString())+"'"); 
	                                break; 
	                        case  Types.DATE://91
	                        		if(DATA_SOURCE_JDBC_DRIVER.equals("com.mysql.jdbc.Driver")){
	                        			buffSQL.append("'"+objValue.toString()+"'"); 
	                        		}else if(DATA_SOURCE_JDBC_DRIVER.equals("net.sourceforge.jtds.jdbc.Driver")){
	                        			buffSQL.append("'"+objValue.toString()+"'"); 
	                        		}else if(DATA_SOURCE_JDBC_DRIVER.equals("oracle.jdbc.driver.OracleDriver")){
	                        			buffSQL.append("to_date('"+objValue.toString()+"','yyyy-mm-dd')");
	                        		}
	                                break;
	                        case  Types.TIMESTAMP: //93 
		                        	if(DATA_SOURCE_JDBC_DRIVER.equals("com.mysql.jdbc.Driver")){
		                        		buffSQL.append("'"+objValue.toString()+"'"); 
	                        		}else if(DATA_SOURCE_JDBC_DRIVER.equals("net.sourceforge.jtds.jdbc.Driver")){
	                        			buffSQL.append("'"+objValue.toString()+"'"); 
	                        		}else if(DATA_SOURCE_JDBC_DRIVER.equals("oracle.jdbc.driver.OracleDriver")){
	                        			buffSQL.append("to_date('"+objValue.toString()+"','YYYY-MON-DD HH24:MI:SS')"); 
	                        		}
	                                break;
	                        case  Types.BLOB:
	                        	    buffSQL.append("' '");
	                                break;
	                        default: 
	                        		buffSQL.append("'"+StringEscapeUtils.escapeSql(objValue.toString())+"'"); 
	                	} 
                	}
                	if(i==(count-1))buffSQL.append(");\n");
                	else buffSQL.append(",");
                }
            }while(resultSet.next());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                resultSet.close();
                preStm.close();
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        return buffSQL.toString() ;
	}
	/**
	 * 将字符串写到指定文件路径
	 * @param str 字符串
	 * @param filePath 输出文件(包括文件绝对路径)
	 * @return 返回文件是否写成功
	 */
	public boolean writeFile(String str,String filePath){
		boolean isSucess = false ;
		java.io.FileWriter fileWriter = null ;
		try{
			File file = new File(filePath);
			fileWriter = new java.io.FileWriter(file);
			fileWriter.write(str);
			isSucess = true ;
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			try{
				fileWriter.close();
			}catch(Exception ex){
			}
		}
		return isSucess ;
	}
}