package org.maccha.dao.impl;

public class INExprProcess implements ISQLExprProcess {
	private String sqlOpt = null;
    private String colName = null;
    private String paramName = null;
    private Object colValue = null;    
    public INExprProcess() {
    	
    }

    /**
     * 设置表达式的列名和列值
     * 注：列值可以表示为：
     * {如果表达式为'IN',则列值可以为：对象数组，java.util.Collection实现对象}
     * @param strColName 列名
     * @param objColValue　列值
     * @param strSqlOpt　操作符({@link org.simpro.dao.SqlOperator})
     */
    public void setFilter(String strColName,String strParamName, Object objColValue, String strSqlOpt) {
    	this.paramName = strParamName ;
        this.colName = strColName;
        this.colValue = objColValue;
        this.sqlOpt = strSqlOpt;
    }

    /**
     * 返回SQL 操作表带式
     * @return 返回"IN" 字符串
     */
    public String getSQLExpr() {
        return this.sqlOpt;
    }

    /**
     * 返回"列名 IN (?,?)"表达式
     */
    public String getFilterExpr() {
        StringBuffer buffSQLExpr = new StringBuffer();
        buffSQLExpr.append(this.colName).append(" ").append(sqlOpt).append(" (");
        buffSQLExpr.append(":").append(this.paramName);
        buffSQLExpr.append(")");
        return buffSQLExpr.toString();
    }

    public static void main(String[] args) {
        java.util.Vector vec = new java.util.Vector();
        vec.addElement("0");
        vec.addElement("1");
        vec.addElement("2");
        INExprProcess inExprProcess = new INExprProcess();
        inExprProcess.setFilter("col", "col_11",vec, ISqlOperator.In);
        System.out.println(inExprProcess.getFilterExpr());
    }
}
