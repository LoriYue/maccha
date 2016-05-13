package org.maccha.dao.impl;

public class CommonExprProcess implements ISQLExprProcess{
    private String sqlOpt = null ;
    private String paramName = null ;
    private String colName = null ;
    private Object colValue = null ;
    public CommonExprProcess() {
    }
    /**
     * 设置表达式的列名和列值
     * 注：列值可以表示为：
     * {如果表达式为'IN',则列值可以为：对象数组，java.util.Collection实现对象}
     * @param strColName 列名
     * @param objColValue　列值
     * @param strSqlOpt　操作符({@link org.simpro.dao.SqlOperator})
     */
    public void setFilter(String strColName,String strParamName, Object objColValue,String strSqlOpt){
    	this.paramName = strParamName ;
        this.colName = strColName ;
        this.colValue = objColValue ;
        this.sqlOpt = strSqlOpt ;
    }
    /**
     * 返回SQL 操作表带式
     * 接口支持："=,>,<,IN,LIKE,IS NULL,<=,>=,<>,IS NOT NULL,BETWEEN"操作符号
     * @return
     */
    public String getSQLExpr(){
        return this.sqlOpt ;
    }
    /**
     * 返回 where 表带式字符串
     */
    public String getFilterExpr(){
        StringBuffer buffExpr = new StringBuffer();
        buffExpr.append(this.colName).append(" ").append(this.sqlOpt).append(" :").append(this.paramName).append(" ")  ;
        return buffExpr.toString();
    }
}
