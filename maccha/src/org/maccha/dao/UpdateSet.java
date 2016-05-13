package org.maccha.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateSet {
    UpdateSet updateSet = null;
    private Map map = new HashMap();
    private Map exprMap = new HashMap();
    private Map exprValueMap = new HashMap();
    public static UpdateSet getUpdateSet() {
        return new UpdateSet();
    }
    public UpdateSet set(Map map) {
        this.map.putAll(map);
        return this;
    }
    /**
     * 设置更新值
     * @param name String
     * @param value Object
     * @return UpdateSet
     */
    public UpdateSet set(String name, Object value) {
        this.map.put(name, value);
        return this;
    }
    /**
     * 设置表达式更新(表达式中的值用？表示，如：a+?)
     * @param name String
     * @param expr String
     * @return UpdateSet
     */
    public UpdateSet exprSet(String name, String expr, Object[] values) {
        this.exprMap.put(name, expr);
        this.exprValueMap.put(name, values);
        return this;
    }
    /**
     * 表达式更新(表达式和值混合一起，如:a+10)
     * @param name String
     * @param expr String  a+10
     * @return UpdateSet
     */
    public UpdateSet exprSet(String name, String expr) {
        Object[] values = getExprValues(expr);
        expr = getExpr(expr);
        exprSet(name, expr, values);
        return this;
    }
    /**
     * a+90+'xyz'+80+b  ---->a+?+?+?+b
     * @param complexExpr String
     * @return String
     */
    public String getExpr(String complexExpr) {
        //String str = "a+89*90+('abcdef'+ab+'pp')";
        String reg = "([\\*\\-\\+\\/\\(\\)])(\\d+|\\'\\w+\\')";
        return complexExpr.replaceAll(reg, "$1?");
    }
    /**
     * a+90+'xyz'+80+b  ---->{Integer(90),"zyz",Integer(80)}
     * @param complexExpr String
     * @return Object[]
     */
    public Object[] getExprValues(String complexExpr) {
        String reg = "([\\*\\-\\+\\/\\(\\)])(\\d+|\\'\\w+\\')";
        Vector exprValues_vector = new Vector();
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(complexExpr);
        boolean result = m.find();
        for (; result; result = m.find()) {
            String strValue = m.group(2);
            Object objValue = null;
            if (strValue.indexOf("'") > -1)
                objValue = new String(strValue.replaceAll("\\'", ""));
            else
                objValue = new Integer(strValue);
            exprValues_vector.add(objValue);
        }
        return exprValues_vector.toArray();
    }
    public String getUpdateExprStr() {
        return "";
    }
    public static void main(String[] args) {
        String str = "a+89*90+('abcdef'+ab+'pp')";
        String reg = "([\\*\\-\\+\\/\\(\\)])(\\d+|\\'\\w+\\')";
        Vector exprValues_vector = new Vector();
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        boolean result = m.find();
        for (; result; result = m.find()) {
            String strValue = m.group(2);
            Object objValue = null;
            if (strValue.indexOf("'") > -1)
                objValue = new String(strValue.replaceAll("\\'", ""));
            else
                objValue = new Integer(strValue);
            exprValues_vector.add(objValue);
        }
    }
    public boolean isHasUpdateSet() {
        return this.exprMap.isEmpty() || this.map.isEmpty();
    }
    public Map[] getExprSet() {
        return new Map[] {
            exprMap, exprValueMap};
    }
    public Map getSet() {
        return this.map;
    }
}
