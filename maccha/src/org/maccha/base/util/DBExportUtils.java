package org.maccha.base.util;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang.StringEscapeUtils;

public class DBExportUtils
{
  private Connection conn = null;
  private String DATA_SOURCE_JDBC_DRIVER = null;
  private String DATA_SOURCE_JDBC_URL = null;
  private String DATA_SOURCE_JDBC_USER_NAME = null;
  private String DATA_SOURCE_JDBC_PASSWORD = null;

  public static void main(String[] agrs)
  {
    DBExportUtils dbExportUtils = getInstance("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/nhd_pms?autoReconnect=true&amp;useUnicode=true&amp;characterEncoding=UTF-8", "root", "");

    dbExportUtils.exportMoreTable2File(new String[] { "nhdpms_patient", "bizframe_rbac_user" }, "c:\\jhf.sql");
  }
  public static DBExportUtils getInstance(String jdbcDriver, String jdbcURL, String jdbcUserName, String jdbcPassword) {
    return new DBExportUtils(jdbcDriver, jdbcURL, jdbcUserName, jdbcPassword);
  }
  public static DBExportUtils getInstance(Connection _conn) {
    return new DBExportUtils(_conn);
  }
  public DBExportUtils(Connection _conn) {
    this.conn = _conn;
    try {
      this.DATA_SOURCE_JDBC_DRIVER = this.conn.getMetaData().getDriverName(); } catch (Exception ex) {
    }
  }

  public DBExportUtils(String jdbcDriver, String jdbcURL, String jdbcUserName, String jdbcPassword) {
    this.DATA_SOURCE_JDBC_DRIVER = jdbcDriver;
    this.DATA_SOURCE_JDBC_URL = jdbcURL;
    this.DATA_SOURCE_JDBC_USER_NAME = jdbcUserName;
    this.DATA_SOURCE_JDBC_PASSWORD = jdbcPassword;
    this.conn = getConnection();
  }
  public Connection getConnection() {
    Connection _conn = null;
    try {
      Class.forName(this.DATA_SOURCE_JDBC_DRIVER).newInstance();
      _conn = DriverManager.getConnection(this.DATA_SOURCE_JDBC_URL, this.DATA_SOURCE_JDBC_USER_NAME, this.DATA_SOURCE_JDBC_PASSWORD);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return _conn;
  }

  public void colseConnection() {
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

  public void exportOneTable2File(String tableName, String[] cols, String filePath)
  {
    try
    {
      if (filePath != null) writeFile(_exportTable(tableName, cols), filePath); 
    }
    catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      colseConnection();
    }
  }

  public void exportMoreTable2File(String[] tableNames, String filePath)
  {
    StringBuffer buffSQL = new StringBuffer();
    for (int i = 0; i < tableNames.length; i++)
      buffSQL.append(_exportTable(tableNames[i], null));
    try
    {
      if (filePath != null) writeFile(buffSQL.toString(), filePath); 
    }
    catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      colseConnection();
    }
  }

  private String _exportTable(String tableName, String[] cols) {
    StringBuffer buffSQL = new StringBuffer();
    PreparedStatement preStm = null;
    ResultSet resultSet = null;
    HashMap mapColsType = new HashMap();
    try
    {
      String query = "select * from " + tableName;

      preStm = this.conn.prepareStatement(query);

      resultSet = preStm.executeQuery();

      ArrayList arryColumnList = new ArrayList();
      ArrayList arryColumnType = new ArrayList();

      if (!resultSet.next()) {
        String str1 = "";
        return str1;
      }
      ResultSetMetaData metaData = resultSet.getMetaData();

      String strColumnName = null;

      int count = metaData.getColumnCount();
      for (int i = 1; i < count + 1; i++)
      {
        strColumnName = metaData.getColumnName(i);
        int type = metaData.getColumnType(i);
        arryColumnList.add(strColumnName);
        mapColsType.put(strColumnName.toLowerCase(), type + "");
      }
      if (cols == null) {
        cols = new String[arryColumnList.size()];
        for (int j = 0; j < arryColumnList.size(); j++) {
          cols[j] = ((String)arryColumnList.get(j));
        }
      }

      count = cols.length;
      StringBuffer buffInsertSQL = new StringBuffer("insert into " + tableName + "(");
      for (int i = 0; i < count; i++) {
        if (i == count - 1)
          buffInsertSQL.append(cols[i] + ") values (");
        else
          buffInsertSQL.append(cols[i] + ",");
      }
      do
      {
        buffSQL.append(buffInsertSQL.toString());
        for (int i = 0; i < count; i++) {
          Object objValue = resultSet.getObject(cols[i]);
          if (objValue == null) {
            int intType = Integer.parseInt((String)mapColsType.get(cols[i].toLowerCase()));
            switch (intType) {
            case -1:
              buffSQL.append("0");
              break;
            case 2:
              buffSQL.append("0");
              break;
            default:
              buffSQL.append("null");
            }
          } else {
            int intType = Integer.parseInt((String)mapColsType.get(cols[i].toLowerCase()));
            switch (intType) {
            case -1:
              buffSQL.append("'" + StringEscapeUtils.escapeSql(objValue.toString()) + "'");
              break;
            case 1:
              buffSQL.append("'" + StringEscapeUtils.escapeSql(objValue.toString()) + "'");
              break;
            case 2:
              buffSQL.append(objValue);
              break;
            case 12:
              buffSQL.append("'" + StringEscapeUtils.escapeSql(objValue.toString()) + "'");
              break;
            case 91:
              if (this.DATA_SOURCE_JDBC_DRIVER.equals("com.mysql.jdbc.Driver")) {
                buffSQL.append("'" + objValue.toString() + "'");
              } else if (this.DATA_SOURCE_JDBC_DRIVER.equals("net.sourceforge.jtds.jdbc.Driver")) {
                buffSQL.append("'" + objValue.toString() + "'"); } else {
                if (!this.DATA_SOURCE_JDBC_DRIVER.equals("oracle.jdbc.driver.OracleDriver")) break;
                buffSQL.append("to_date('" + objValue.toString() + "','yyyy-mm-dd')"); } break;
            case 93:
              if (this.DATA_SOURCE_JDBC_DRIVER.equals("com.mysql.jdbc.Driver")) {
                buffSQL.append("'" + objValue.toString() + "'");
              } else if (this.DATA_SOURCE_JDBC_DRIVER.equals("net.sourceforge.jtds.jdbc.Driver")) {
                buffSQL.append("'" + objValue.toString() + "'"); } else {
                if (!this.DATA_SOURCE_JDBC_DRIVER.equals("oracle.jdbc.driver.OracleDriver")) break;
                buffSQL.append("to_date('" + objValue.toString() + "','YYYY-MON-DD HH24:MI:SS')"); } break;
            case 2004:
              buffSQL.append("' '");
              break;
            default:
              buffSQL.append("'" + StringEscapeUtils.escapeSql(objValue.toString()) + "'");
            }
          }
          if (i == count - 1) buffSQL.append(");\n"); else
            buffSQL.append(","); 
        }
      }
      while (resultSet.next());
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        resultSet.close();
        preStm.close();
      }
      catch (Exception e) {
      }
    }
    return buffSQL.toString();
  }

  public boolean writeFile(String str, String filePath)
  {
    boolean isSucess = false;
    FileWriter fileWriter = null;
    try {
      File file = new File(filePath);
      fileWriter = new FileWriter(file);
      fileWriter.write(str);
      isSucess = true;
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        fileWriter.close();
      } catch (Exception ex) {
      }
    }
    return isSucess;
  }
}
