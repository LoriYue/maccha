package org.maccha.base.util.convertor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.type.ClobType;
import org.maccha.base.util.DateUtils;
import org.maccha.base.util.StringUtils;

public final class ConvertorTypesUtility
{
  private static DateFormat _dateFormat = new SimpleDateFormat();

  private static SimpleDateFormat _paramDateFormat = new SimpleDateFormat();

  private static DecimalFormat _decimalFormat = new DecimalFormat("#################0");

  static TypeConvertorInfo[] _typeConvertors = { new TypeConvertorInfo(new SimpleTypeConvertor(Boolean.class)
  {
    public Object convert(Object obj, String param)
    {
      return ((Short)obj).shortValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(BigDecimal.class)
  {
    public Object convert(Object obj, String param)
    {
      return new BigDecimal((String)obj);
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Short.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Short(((Boolean)obj).booleanValue() ? 1 : 0);
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Boolean.class)
  {
    public Object convert(Object obj, String param)
    {
      return ((Integer)obj).intValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Integer.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Integer(((Boolean)obj).booleanValue() ? 1 : 0);
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Boolean.TYPE)
  {
    public Object convert(Object obj, String param)
    {
      switch (((String)obj).length()) {
      case 0:
        return Boolean.FALSE;
      case 1:
        char ch = ((String)obj).charAt(0);
        if ((param == null) || (param.length() != 2)) {
          return (ch == 'T') || (ch == 't') || (ch == '1') ? Boolean.TRUE : Boolean.FALSE;
        }
        return ch == param.charAt(1) ? Boolean.TRUE : Boolean.FALSE;
      case 4:
        return ((String)obj).equalsIgnoreCase("true") ? Boolean.TRUE : Boolean.FALSE;
      case 5:
        return ((String)obj).equalsIgnoreCase("false") ? Boolean.FALSE : Boolean.TRUE;
      case 2:
      case 3: } return Boolean.FALSE;
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Boolean.class)
  {
    public Object convert(Object obj, String param)
    {
      switch (((String)obj).length()) {
      case 0:
        return Boolean.FALSE;
      case 1:
        char ch = ((String)obj).charAt(0);
        if ((param == null) || (param.length() != 2)) {
          return (ch == 'T') || (ch == 't') || (ch == '1') ? Boolean.TRUE : Boolean.FALSE;
        }
        return ch == param.charAt(1) ? Boolean.TRUE : Boolean.FALSE;
      case 4:
        return ((String)obj).equalsIgnoreCase("true") ? Boolean.TRUE : Boolean.FALSE;
      case 5:
        return ((String)obj).equalsIgnoreCase("false") ? Boolean.FALSE : Boolean.TRUE;
      case 2:
      case 3: } return Boolean.FALSE;
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Boolean.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Boolean(((BigDecimal)obj).intValue() != 0);
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(String.class)
  {
    public Object convert(Object obj, String param)
    {
      return ((BigDecimal)obj).toString();
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Integer.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Integer(((Byte)obj).intValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Integer.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Integer(((Short)obj).intValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Integer.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Integer(((Long)obj).intValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Integer.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Integer(((Float)obj).intValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Integer.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Integer(((Double)obj).intValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Integer.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Integer(((BigDecimal)obj).intValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Integer.class)
  {
    public Object convert(Object obj, String param)
    {
      if (!StringUtils.isNotNull((String)obj))
        return new Integer(0);
      return Integer.valueOf((String)obj);
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Long.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Long(((Integer)obj).longValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Long.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Long(((Short)obj).longValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Long.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Long(((Float)obj).longValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Long.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Long(((Double)obj).longValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Long.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Long(((BigDecimal)obj).longValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Long.class)
  {
    public Object convert(Object obj, String param)
    {
      if (!StringUtils.isNotNull((String)obj))
        return new Long(0L);
      return NumberUtils.createLong((String)obj);
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Long.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Long(((java.util.Date)obj).getTime());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Short.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Short(((Byte)obj).shortValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Short.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Short(((Integer)obj).shortValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Short.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Short(((Long)obj).shortValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Short.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Short((short)((BigDecimal)obj).intValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Short.class)
  {
    public Object convert(Object obj, String param)
    {
      if (!StringUtils.isNotNull((String)obj))
        return new Short("0");
      return Short.valueOf((String)obj);
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Byte.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Byte(((Short)obj).byteValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Byte.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Byte(((Integer)obj).byteValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Byte.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Byte((byte)((BigDecimal)obj).intValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Double.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Double(((Float)obj).floatValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Double.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Double(((Integer)obj).intValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Double.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Double(((Long)obj).longValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Double.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Double(((BigDecimal)obj).doubleValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Double.class)
  {
    public Object convert(Object obj, String param)
    {
      if (!StringUtils.isNotNull((String)obj)) return new Double(0.0D);
      return Double.valueOf((String)obj);
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Float.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Float(((Double)obj).floatValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Float.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Float(((Integer)obj).intValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Float.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Float((float)((Long)obj).longValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Float.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Float(((BigDecimal)obj).floatValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Float.class)
  {
    public Object convert(Object obj, String param)
    {
      if (!StringUtils.isNotNull((String)obj)) return new Float(0.0D);
      return Float.valueOf((String)obj);
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(BigDecimal.class)
  {
    public Object convert(Object obj, String param)
    {
      return new BigDecimal(((Double)obj).toString());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(BigDecimal.class)
  {
    public Object convert(Object obj, String param)
    {
      return new BigDecimal(((Float)obj).toString());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(BigDecimal.class)
  {
    public Object convert(Object obj, String param)
    {
      return BigDecimal.valueOf(((Integer)obj).intValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(BigDecimal.class)
  {
    public Object convert(Object obj, String param)
    {
      return BigDecimal.valueOf(((Byte)obj).byteValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(BigDecimal.class)
  {
    public Object convert(Object obj, String param)
    {
      return BigDecimal.valueOf(((Short)obj).shortValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(BigDecimal.class)
  {
    public Object convert(Object obj, String param)
    {
      return BigDecimal.valueOf(((Long)obj).longValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(BigDecimal.class)
  {
    public Object convert(Object obj, String param)
    {
      return BigDecimal.valueOf(((Boolean)obj).booleanValue() ? 1L : 0L);
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(String.class)
  {
    public Object convert(Object obj, String param)
    {
      return obj.toString();
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(String.class)
  {
    public Object convert(Object obj, String param)
    {
      return obj.toString();
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(String.class)
  {
    public Object convert(Object obj, String param)
    {
      return obj.toString();
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(String.class)
  {
    public Object convert(Object obj, String param)
    {
      return obj.toString();
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(String.class)
  {
    public Object convert(Object obj, String param)
    {
      return obj.toString();
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(String.class)
  {
    public Object convert(Object obj, String param)
    {
      return obj.toString();
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(String.class)
  {
    public Object convert(Object obj, String param)
    {
      if ((param == null) || (param.length() == 0)) {
        return obj.toString();
      }
      ConvertorTypesUtility._paramDateFormat.applyPattern(param);
      return ConvertorTypesUtility._paramDateFormat.format((java.util.Date)obj);
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(String.class)
  {
    public Object convert(Object obj, String param)
    {
      return obj.toString();
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(String.class)
  {
    public Object convert(Object obj, String param)
    {
      return new String((char[])(char[])obj);
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(String.class)
  {
    public Object convert(Object obj, String param)
    {
      if ((param == null) || (param.length() != 2)) {
        return ((Boolean)obj).booleanValue() ? "T" : "F";
      }
      return ((Boolean)obj).booleanValue() ? param.substring(1, 2) : param.substring(0, 1);
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Character.class)
  {
    public Object convert(Object obj, String param)
    {
      String str = (String)obj;
      return new Character(str.length() == 0 ? '\000' : str.charAt(0));
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor([C.class)
  {
    public Object convert(Object obj, String param)
    {
      return ((String)obj).toCharArray();
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(java.util.Date.class)
  {
    public Object convert(Object obj, String param)
    {
      return new java.util.Date(((Long)obj).longValue());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(String.class)
  {
    public Object convert(Object obj, String param)
    {
      try
      {
        ConvertorTypesUtility._paramDateFormat.applyPattern("yyyy-MM-dd");
        return ConvertorTypesUtility._paramDateFormat.format((java.util.Date)obj); } catch (Exception except) {
      }
      throw new IllegalArgumentException(except.toString());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(java.sql.Date.class)
  {
    public Object convert(Object obj, String param)
    {
      try
      {
        if ((obj != null) && (((String)obj).length() > 10))
          param = "yyyy-MM-dd HH:mm:ss";
        if ((obj != null) && (((String)obj).trim().length() == 10))
          param = "yyyy-MM-dd";
        java.util.Date dataObj = null;
        if ((param == null) || (param.length() == 0)) {
          dataObj = DateUtils.parse((String)obj);
        } else {
          ConvertorTypesUtility._paramDateFormat.applyPattern(param);
          dataObj = ConvertorTypesUtility._paramDateFormat.parse((String)obj);
        }

        return new java.sql.Date(dataObj.getTime()); } catch (ParseException except) {
      }
      throw new IllegalArgumentException(except.toString());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(java.util.Date.class)
  {
    public Object convert(Object obj, String param)
    {
      if (!StringUtils.isNotNull((String)obj)) return null; try
      {
        if ((obj != null) && (((String)obj).length() > 10))
          param = "yyyy-MM-dd HH:mm:ss";
        if ((obj != null) && (((String)obj).trim().length() == 10))
          param = "yyyy-MM-dd";
        java.util.Date dataObj = null;
        if ((param == null) || (param.length() == 0)) {
          dataObj = DateUtils.parse((String)obj);
        } else {
          ConvertorTypesUtility._paramDateFormat.applyPattern(param);
          dataObj = ConvertorTypesUtility._paramDateFormat.parse((String)obj);
        }
        return new java.util.Date(dataObj.getTime()); } catch (ParseException except) {
      }
      throw new IllegalArgumentException(except.toString());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(java.sql.Date.class)
  {
    public Object convert(Object obj, String param)
    {
      return new java.sql.Date(((java.util.Date)obj).getTime());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(java.util.Date.class)
  {
    public Object convert(Object obj, String param)
    {
      return new java.util.Date(((java.sql.Date)obj).getTime());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Time.class)
  {
    public Object convert(Object obj, String param)
    {
      return new Time(((java.util.Date)obj).getTime());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(java.util.Date.class)
  {
    public Object convert(Object obj, String param)
    {
      return obj;
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Timestamp.class)
  {
    public Object convert(Object obj, String param)
    {
      long time = ((java.util.Date)obj).getTime();
      Timestamp timestamp = new Timestamp(time);
      timestamp.setNanos((int)(time % 1000L * 1000000L));

      return timestamp;
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(java.util.Date.class)
  {
    public Object convert(Object obj, String param)
    {
      Timestamp timestamp = (Timestamp)obj;
      return new java.util.Date(timestamp.getTime() + timestamp.getNanos() / 1000000);
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Timestamp.class)
  {
    public Object convert(Object obj, String param)
    {
      if ((param == null) || (param.length() == 0))
      {
        param = "yyyy-MM-dd HH:mm:ss";
      }long time;
      try { ConvertorTypesUtility._paramDateFormat.applyPattern(param);
        time = ConvertorTypesUtility._paramDateFormat.parse((String)obj).getTime();
      } catch (ParseException except) {
        throw new IllegalArgumentException(except.toString());
      }
      Timestamp timestamp = new Timestamp(time);
      timestamp.setNanos((int)(time % 1000L * 1000000L));

      return timestamp;
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(String.class)
  {
    public Object convert(Object obj, String param)
    {
      if ((param == null) || (param.length() == 0)) {
        param = "yyyy-MM-dd HH:mm:ss.SSS";
      }
      Timestamp timestamp = (Timestamp)obj;
      ConvertorTypesUtility._paramDateFormat.applyPattern(param);
      return ConvertorTypesUtility._paramDateFormat.format(new java.util.Date(timestamp.getTime() + timestamp.getNanos() / 1000000));
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(InputStream.class)
  {
    public Object convert(Object obj, String param)
    {
      return new ByteArrayInputStream((byte[])(byte[])obj);
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor([B.class)
  {
    public Object convert(Object obj, String param)
    {
      try
      {
        InputStream is = (InputStream)obj;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[256];
        int len = 0;

        while ((len = is.read(buffer)) > 0)
          bos.write(buffer, 0, len);
        return bos.toByteArray(); } catch (IOException except) {
      }
      throw new IllegalArgumentException(except.toString());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Clob.class)
  {
    public Object convert(Object obj, String param)
    {
      String str = (String)obj;
      return new ClobType().fromString(str);
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Clob.class)
  {
    public Object convert(Object obj, String param)
    {
      char[] chars = (char[])(char[])obj;
      return new ClobType().fromString(new String(chars));
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(String.class)
  {
    public Object convert(Object obj, String param)
    {
      try
      {
        Reader reader = ((Clob)obj).getCharacterStream();

        CharArrayWriter writer = new CharArrayWriter();
        char[] buffer = new char[256];
        int len = 0;

        while ((len = reader.read(buffer)) > 0)
          writer.write(buffer, 0, len);
        return writer.toString(); } catch (Exception except) {
      }
      throw new IllegalArgumentException(except.toString());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor([C.class)
  {
    public Object convert(Object obj, String param)
    {
      try
      {
        Reader reader = ((Clob)obj).getCharacterStream();

        CharArrayWriter writer = new CharArrayWriter();
        char[] buffer = new char[256];
        int len = 0;

        while ((len = reader.read(buffer)) > 0)
          writer.write(buffer, 0, len);
        return writer.toCharArray(); } catch (Exception except) {
      }
      throw new IllegalArgumentException(except.toString());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(InputStream.class)
  {
    public Object convert(Object obj, String param)
    {
      return new ByteArrayInputStream((byte[])(byte[])obj);
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Serializable.class)
  {
    public Object convert(Object obj, String param)
    {
      try
      {
        InputStream is = (InputStream)obj;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[256];
        int len = 0;

        while ((len = is.read(buffer)) > 0)
          bos.write(buffer, 0, len);
        return bos.toByteArray(); } catch (IOException except) {
      }
      throw new IllegalArgumentException(except.toString());
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor([B.class)
  {
    public Object convert(Object obj, String param)
    {
      return obj;
    }
  }), new TypeConvertorInfo(new SimpleTypeConvertor(Serializable.class)
  {
    public Object convert(Object obj, String param)
    {
      return obj;
    }
  }) };

  public static TypeConvertor getConvertor(Class fromType, Class toType)
    throws Exception
  {
    for (int i = 0; i < _typeConvertors.length; i++)
    {
      if ((_typeConvertors[i].fromType.equals(fromType)) && (toType.equals(_typeConvertors[i].toType)))
      {
        return _typeConvertors[i].convertor;
      }
    }

    for (int i = 0; i < _typeConvertors.length; i++)
      if ((_typeConvertors[i].fromType.isAssignableFrom(fromType)) && (toType.isAssignableFrom(_typeConvertors[i].toType)))
      {
        return _typeConvertors[i].convertor;
      }
    throw new Exception("From :" + fromType.getName() + " To :" + toType.getName() + " : mapping.noConvertor");
  }

  private static abstract class SimpleTypeConvertor
    implements TypeConvertor
  {
    Class fromType;
    Class toType;

    SimpleTypeConvertor(Class fromType, Class toType)
    {
      this.fromType = fromType;
      this.toType = toType;
    }
    public abstract Object convert(Object paramObject, String paramString);

    public String toString() {
      return this.fromType.getName() + "-->" + this.toType.getName();
    }
  }

  static class TypeConvertorInfo
  {
    final Class toType;
    final Class fromType;
    final ConvertorTypesUtility.SimpleTypeConvertor convertor;

    TypeConvertorInfo(ConvertorTypesUtility.SimpleTypeConvertor convertor)
    {
      this.convertor = convertor;
      this.fromType = convertor.fromType;
      this.toType = convertor.toType;
    }
  }
}
