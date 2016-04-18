package org.maccha.base.util;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;
import org.maccha.base.exception.SysException;
import org.maccha.base.util.convertor.ConvertorTypesUtility;
import org.maccha.base.util.convertor.TypeConvertor;

public class TypeConvertorUtils
{
  private String aa;

  public static void main(String[] args)
  {
    String strDate = "2006-1-5 16:49:02";
    Timestamp timeStamp = new Timestamp(System.currentTimeMillis());

    System.out.println(convert(timeStamp, Date.class));

    System.out.println(getType(TypeConvertorUtils.class, "aa"));
  }
  public static Object convert(Object obj, Class fromType, Class toType) {
    Object objReturn = null;
    if ((obj == null) || (toType == null)) return null; try
    {
      if (!toType.equals(obj.getClass())) {
        TypeConvertor typeConvertor = ConvertorTypesUtility.getConvertor(fromType, toType);
        objReturn = typeConvertor.convert(obj, null);
        return objReturn;
      }
      return obj;
    }
    catch (Exception ex) {
      SysException.handleMessageException(ex.getMessage());
      ex.printStackTrace();

      if (objReturn == null)
        objReturn = obj; 
    }
    return objReturn;
  }
  public static Object convert(Object obj, Class toType) {
    Object objReturn = null;
    if ((obj == null) || (toType == null)) return null; try
    {
      if (!toType.equals(obj.getClass())) {
        TypeConvertor typeConvertor = ConvertorTypesUtility.getConvertor(obj.getClass(), toType);
        objReturn = typeConvertor.convert(obj, null);
        return objReturn;
      }
      return obj;
    }
    catch (Exception ex) {
      SysException.handleMessageException(ex.getMessage());
      ex.printStackTrace();

      if (objReturn == null)
        objReturn = obj; 
    }
    return objReturn;
  }
  public static Object convert(Object obj, Class toType, String param) {
    Object objReturn = null;
    if ((obj == null) || (toType == null)) return null; try
    {
      if (!toType.equals(obj.getClass())) {
        TypeConvertor typeConvertor = ConvertorTypesUtility.getConvertor(obj.getClass(), toType);
        objReturn = typeConvertor.convert(obj, param);
        return objReturn;
      }
      return obj;
    }
    catch (Exception ex) {
      SysException.handleMessageException(ex.getMessage());
      ex.printStackTrace();

      if (objReturn == null) objReturn = obj; 
    }
    return objReturn;
  }

  public static Class getType(Class bean, String field) {
    try {
      String strMethodName = "get" + StringUtils.capitalizeFirst(field);
      return bean.getMethod(strMethodName, new Class[0]).getReturnType();
    }
    catch (Exception ex) {
    }
    return null;
  }

  public String getAa()
  {
    return null;
  }
}
