package org.maccha.dao.impl;

public class ISNotNULLExprProcess extends ISNULLExprProcess implements ISQLExprProcess {
	String sqlOpt = "IS NOT NULL";
    public ISNotNULLExprProcess() {
    }
    public static void main(String[] args){
        ISNotNULLExprProcess isNotNULLExprProcess = new ISNotNULLExprProcess();
        isNotNULLExprProcess.setFilter("col","","",null) ;
        System.out.println("........"+isNotNULLExprProcess.getSQLExpr() ) ;
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
        StringBuffer buffExpr = new StringBuffer();
        buffExpr.append(this.colName).append(" ").append(this.sqlOpt) ;
        //return(this.colName +" "+ this.sqlOpt);
        return buffExpr.toString();
    }
}
