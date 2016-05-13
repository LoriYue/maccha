package org.maccha.dao.impl;

public class IsBetweenExprProcess implements ISQLExprProcess{
    String sqlOpt = null;
    String colName = null ;
    Object colValue = null ;
    private String paramName = null ;
    public IsBetweenExprProcess() {
    }
    /**
     * 设置表达式的列名和列值
     * 注：列值可以表示为：
     * {如果表达式为'IN',则列值可以为：对象数组，java.util.Collection实现对象}
     * @param strColName 列名
     * @param objColValue　列值
     * @param strSqlOpt　操作符({@link org.simpro.dao.SqlOperator})
     */
    public void setFilter(String strColName,String strParamName,Object objColValue,String strSqlOpt){
        this.colName = strColName ;
        this.colValue = objColValue ;
        this.paramName = strParamName ;
        this.sqlOpt = strSqlOpt ;
    }
    /**
     * 返回SQL 操作表带式
     * @return 返回"IN" 字符串
     */
    public String getSQLExpr(){
        return this.sqlOpt ;
    }
    /**
     * 返回"列名 IS NULL"表达式
     */
    public String getFilterExpr(){
    	//age BETWEEN 10 AND 30
        StringBuffer buffExpr = new StringBuffer();
        if(colValue instanceof Object[]){
        	buffExpr.append(this.colName).append(" ").append(this.sqlOpt.replace("{a}", ":"+this.paramName+"_1").replace("{b}", ":"+this.paramName+"_2"));
        }else{
        	buffExpr.append("1=1") ;
        }
        return buffExpr.toString();
    }
}
