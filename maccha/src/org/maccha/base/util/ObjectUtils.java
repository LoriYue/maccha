package org.maccha.base.util;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.maccha.base.exception.SysException;

public class ObjectUtils
{
  public static Logger logger = Logger.getLogger(ObjectUtils.class.getName());

  private static final byte[] hexval = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };

  public static Object map2PO(Class bean, Map paramMap, String SPLIT_DELIM, ParameterProcessor parameterProcessor)
  {
    return map2PO(bean, paramMap, SPLIT_DELIM, parameterProcessor, new ArrayList());
  }

  public static Object map2PO(Class bean, Map paramMap, String SPLIT_DELIM, ParameterProcessor parameterProcessor, List excludePropName)
  {
    String className = StringUtils.unqualify(bean.getName().toLowerCase());
    HashMap hashObj = new HashMap();
    Object _srcObj = null;
    try {
      String strKey = null;
      Object objValue = null;
      String strPropName = null;
      _srcObj = bean.newInstance();
      Iterator itrKeys = paramMap.keySet().iterator();
      PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(bean);
      while (itrKeys.hasNext()) {
        strKey = (String)itrKeys.next();
        objValue = paramMap.get(strKey);
        if ((SPLIT_DELIM != null) && (strKey.indexOf(SPLIT_DELIM) != -1))
        {
          String[] arry = StringUtils.split(strKey, SPLIT_DELIM);
          if (className.equalsIgnoreCase(arry[0]))
          {
            strPropName = arry[1];
            if ((null == excludePropName) || (!excludePropName.contains(strPropName)))
              _setObjectValue(_srcObj, strPropName, objValue);
          }
          continue;
        }strPropName = strKey;
        if ((null != excludePropName) && (excludePropName.contains(strPropName)))
          continue;
        for (int i = 0; i < descriptors.length; i++) {
          if ((descriptors[i].getName().equals(strPropName)) && (PropertyUtils.isWriteable(_srcObj, strPropName)))
            _setObjectValue(_srcObj, strPropName, objValue);
        }
      }
    }
    catch (Exception ex)
    {
      System.out.println(":::::::::::::::::::::::::::::::::::::hello11");

      SysException.handleMessageException(ex.getMessage());
    }
    if (parameterProcessor != null) parameterProcessor.parameterProcess(new ParameterRecord((HashMap)paramMap), _srcObj);
    return _srcObj;
  }
  private static void _setObjectValue(Object bean, String strPropName, Object objValue) {
    try {
      Object objConvert = TypeConvertorUtils.convert(objValue, PropertyUtils.getPropertyType(bean, strPropName));
      BeanUtils.setProperty(bean, strPropName, objConvert);
    } catch (Exception ex) {
      System.out.println(":::::::::::::::::::::::::::::::::::::hello");
      SysException.handleMessageException(ex.getMessage());
    }
  }

  public static List getFields(Object bean)
  {
    List listField = new ArrayList();
    PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(bean);
    for (int i = 0; i < descriptors.length; i++) {
      if (PropertyUtils.isWriteable(bean, descriptors[i].getName())) {
        listField.add(descriptors[i].getName());
      }
    }
    return listField;
  }

  public static Map<String, Object> po2map(Object obj, boolean isIncludeNull)
  {
    Map _map = new HashMap();
    PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(obj);
    for (int i = 0; i < descriptors.length; i++) {
      String strPropName = descriptors[i].getName();
      if ((!PropertyUtils.isWriteable(obj, descriptors[i].getName())) || 
        ("class".equals(strPropName))) continue;
      Object _objValue = ClassUtils.get(obj, strPropName);
      if (isIncludeNull) {
        _map.put(strPropName, _objValue);
      } else {
        if (_objValue == null) continue; _map.put(strPropName, _objValue);
      }

    }

    return _map;
  }

  private static void _getFields(Class cls, List listField)
  {
    Field[] fieldArray = cls.getDeclaredFields();

    for (int i = 0; i < fieldArray.length; i++) {
      Field field = fieldArray[i];
      if (!listField.contains(field.getName()))
        listField.add(field.getName());
    }
    Class superCls = cls.getSuperclass();

    if (superCls.getName() != Object.class.getName())
      _getFields(superCls, listField);
  }

  public static String[] getFields2(Object bean)
  {
    Field[] fieldArray = bean.getClass().getFields();
    String[] strFieldArray = new String[fieldArray.length];
    for (int i = 0; i < fieldArray.length; i++) {
      Field field = fieldArray[i];
      strFieldArray[i] = field.getName();
    }
    return strFieldArray;
  }

  public static List getMethods(Class cls)
  {
    List listMethod = new ArrayList();
    _getMethods(cls, listMethod);
    return listMethod;
  }

  private static void _getMethods(Class cls, List listMethod)
  {
    Method[] methodArray = cls.getDeclaredMethods();
    for (int i = 0; i < methodArray.length; i++) {
      Method method = methodArray[i];
      if (!listMethod.contains(method.getName()))
        listMethod.add(method.getName());
    }
    Class superCls = cls.getSuperclass();
    if ((superCls != null) && 
      (superCls.getName() != Object.class.getName()))
      _getMethods(superCls, listMethod);
  }

  public static String[] getMethods2(Object bean)
  {
    Method[] methodArray = bean.getClass().getMethods();
    String[] strMethodArray = new String[methodArray.length];
    for (int i = 0; i < methodArray.length; i++) {
      Method method = methodArray[i];
      strMethodArray[i] = method.getName();
    }
    return strMethodArray;
  }

  public static String object2String(Object obj)
  {
    StringBuffer buff = new StringBuffer();
    try {
      Map mapObj = BeanUtils.describe(obj);
      Set mapPropName = mapObj.keySet();
      Iterator itr = mapPropName.iterator();
      while (itr.hasNext()) {
        String strPropName = (String)itr.next();
        if (!"class".equals(strPropName)) {
          String strGetMethodName = "get" + StringUtils.capitalizeFirst(strPropName);
          Object objPropValue = ClassUtils.invoke(obj, strGetMethodName);
          if (objPropValue != null)
            buff.append(strPropName).append(" = ").append(objPropValue.toString());
          else
            buff.append(strPropName).append(" = ").append("null");
          buff.append("\n");
        }
      }
    } catch (Exception ex) {
    }
    return buff.toString();
  }

  public static void copyProperties(Object originObj, Object destObj)
    throws Exception
  {
    copyProperties(originObj, destObj, false);
  }

  public static void copyProperties(Object _origObj, Object _destObj, boolean _isIncludeNullValue)
    throws Exception
  {
    PropertyDescriptor[] _descArray = PropertyUtils.getPropertyDescriptors(_origObj.getClass());
    for (int i = 0; i < _descArray.length; i++)
      if (PropertyUtils.isWriteable(_origObj, _descArray[i].getName())) {
        String _getMethodName = "get" + StringUtils.capitalizeFirst(_descArray[i].getName());
        Object _propValueObj = ClassUtils.invoke(_origObj, _getMethodName);

        if ((_isIncludeNullValue) && (StringUtils.isNull(_propValueObj))) {
          BeanUtils.setProperty(_destObj, _descArray[i].getName(), _propValueObj);
        }
        else
        {
          if ((!_isIncludeNullValue) && (StringUtils.isNull(_propValueObj)))
          {
            continue;
          }
          if ((_propValueObj instanceof Timestamp)) {
            Object _obj = TypeConvertorUtils.convert(_propValueObj, Timestamp.class);
            BeanUtils.setProperty(_destObj, _descArray[i].getName(), _obj);
          } else if ((_propValueObj instanceof java.sql.Date)) {
            Object _obj = TypeConvertorUtils.convert(_propValueObj, java.util.Date.class);
            BeanUtils.setProperty(_destObj, _descArray[i].getName(), _obj);
          } else {
            BeanUtils.setProperty(_destObj, _descArray[i].getName(), _propValueObj);
          }
        }
      }
  }

  public static void copyProperties(Object _origObj, Object _destObj, Map _selectMap)
    throws Exception
  {
    Map mapObj = BeanUtils.describe(_origObj);
    Set mapPropName = mapObj.keySet();
    Iterator itr = mapPropName.iterator();
    while (itr.hasNext()) {
      String strPropName = (String)itr.next();
      if (!"class".equals(strPropName)) {
        String strGetMethodName = "get" + StringUtils.capitalizeFirst(strPropName);
        Object objPropValue = ClassUtils.invoke(_origObj, strGetMethodName);
        if (objPropValue != null) {
          String strConvPropName = (String)_selectMap.get(strPropName);
          if (strConvPropName != null)
            BeanUtils.setProperty(_destObj, strConvPropName, objPropValue);
        }
      }
    }
  }

  private static byte[] hexDecode(String s)
  {
    int length = s.length();

    if (length % 2 == 1) {
      throw new IllegalArgumentException("String has odd length " + length);
    }

    byte[] b = new byte[length / 2];
    char[] c = new char[length];
    s.toUpperCase().getChars(0, length, c, 0);
    for (int i = 0; i < length; i += 2) {
      b[(i / 2)] = (byte)(decodeNibble(c[i]) << 4 & 0xF0 | decodeNibble(c[(i + 1)]) & 0xF);
    }
    return b;
  }

  private static byte decodeNibble(char c)
  {
    for (byte i = 0; i < hexval.length; i = (byte)(i + 1)) {
      if ((byte)c == hexval[i]) {
        return i;
      }
    }
    throw new IllegalArgumentException("\"" + c + "\"" + " does not represent a nibble.");
  }

  private static String hexEncode(byte[] s)
  {
    return hexEncode(s, 0, s.length);
  }

  private static String hexEncode(byte[] s, int offset, int length)
  {
    StringBuffer b = new StringBuffer(length * 2);
    for (int i = offset; i < offset + length; i++) {
      int c = s[i];
      b.append((char)hexval[((c & 0xF0) >> 4)]);
      b.append((char)hexval[((c & 0xF) >> 0)]);
    }
    return b.toString();
  }

  public static String hexEncoded(Object obj)
    throws Exception
  {
    if (obj == null) {
      return null;
    }
    String valueStr = null;
    byte[] barr = null;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ObjectOutputStream s = new ObjectOutputStream(out);
    s.writeObject(obj);
    s.flush();
    barr = out.toByteArray();
    valueStr = hexEncode(barr);
    return valueStr;
  }

  public static Object hexDecoded(String bytes)
    throws Exception
  {
    if (bytes == null) {
      return null;
    }
    byte[] barr = hexDecode(bytes);
    Object obj = null;
    ByteArrayInputStream in = new ByteArrayInputStream(barr);
    ObjectInputStream sin = new ObjectInputStream(in);
    obj = sin.readObject();
    return obj;
  }

  public static byte[] serializer(Object object)
    throws Exception
  {
    ByteArrayOutputStream objectOut = null;
    ByteArrayOutputStream zipOut = null;
    GZIPOutputStream gZIPOutputStream = null;
    byte[] objectBytes = null;
    byte[] zipBytes = null;
    try {
      objectOut = new ByteArrayOutputStream();
      new ObjectOutputStream(objectOut).writeObject(object);
      objectBytes = objectOut.toByteArray();
      zipOut = new ByteArrayOutputStream();
      gZIPOutputStream = new GZIPOutputStream(zipOut);
      gZIPOutputStream.write(objectBytes);
      gZIPOutputStream.finish();
      zipBytes = zipOut.toByteArray();
    }
    finally {
      try {
        if (objectOut != null) {
          objectOut.close();
        }
        if (gZIPOutputStream != null) {
          gZIPOutputStream.close();
        }
        if (zipOut != null)
          zipOut.close();
      }
      finally
      {
        objectBytes = null;
        objectOut = null;
        gZIPOutputStream = null;
        zipOut = null;
      }
    }
    return zipBytes;
  }

  public static Object deSerializer(byte[] zipBytes)
    throws Exception
  {
    GZIPInputStream gZIPInputStream = null;
    ByteArrayOutputStream unzipOut = null;
    ObjectInputStream objectIn = null;
    byte[] unzipBytes = null;
    byte[] buf = new byte[1024];
    Object object = null;
    try {
      gZIPInputStream = new GZIPInputStream(new ByteArrayInputStream(zipBytes));

      unzipOut = new ByteArrayOutputStream();
      int len;
      while ((len = gZIPInputStream.read(buf)) >= 0) {
        unzipOut.write(buf, 0, len);
      }
      unzipBytes = unzipOut.toByteArray();
      objectIn = new ObjectInputStream(new ByteArrayInputStream(unzipBytes));

      object = objectIn.readObject();
    } finally {
      try {
        if (gZIPInputStream != null) {
          gZIPInputStream.close();
        }
        if (unzipOut != null) {
          unzipOut.close();
        }
        if (objectIn != null)
          objectIn.close();
      }
      finally {
        unzipBytes = null;
        buf = null;
        gZIPInputStream = null;
        unzipOut = null;
        objectIn = null;
      }
    }
    return object;
  }

  public static List changeSetToList(Set set)
  {
    Iterator setIter = set.iterator();
    List list = new ArrayList();
    while (setIter.hasNext()) {
      list.add(setIter.next());
    }
    return list;
  }

  public static List getObjectAttributeNames(Object obj)
  {
    List list = new ArrayList();
    Map mapObj = new HashMap();
    try {
      mapObj = BeanUtils.describe(obj);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getCause());
    }
    Set mapPropName = mapObj.keySet();
    Iterator itr = mapPropName.iterator();
    while (itr.hasNext()) {
      String strPropName = (String)itr.next();
      if (!"class".equals(strPropName)) {
        list.add(strPropName);
      }
    }
    return list;
  }

}
