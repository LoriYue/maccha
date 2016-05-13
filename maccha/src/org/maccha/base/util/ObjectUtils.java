package org.maccha.base.util;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

public class ObjectUtils {
  public static Logger logger = Logger.getLogger(ObjectUtils.class.getName());

  /**
	 * 将Map数据设置到指定Bean对象中,规则:
	 *  1. 当SPLIT_DELIM 为null时 ,方法会从 Map 中的取出和PO属性值相同 的 key 对应的值，设置到对象属性中
	 *  2. 当SPLIT_DELIM 不为null时 ,例如：bean.name ,bean 为PO类的最后部分第一个字母小写[如java.util.Iterator中的'iterator']，name:为Bean的属性名称
	 * @param bean PO 类
	 * @param paramMap 参数键值对
	 * @param SPLIT_DELIM 参数分隔符
	 * @param parameterProcessor 参数处理接口
	 * @parem excludePropName 不处理的参数列表
	 * @return PO对象
	 */
	public static Object map2PO(Class bean ,Map paramMap,String SPLIT_DELIM,ParameterProcessor parameterProcessor) {
		return map2PO(bean,paramMap,SPLIT_DELIM,parameterProcessor,new ArrayList()) ;
	}
	/**
	 * 将Map数据设置到指定Bean对象中,规则:
	 *  1. 当SPLIT_DELIM 为null时 ,方法会从 Map 中的取出和PO属性值相同 的 key 对应的值，设置到对象属性中
	 *  2. 当SPLIT_DELIM 不为null时 ,例如：bean.name ,bean 为PO类的最后部分第一个字母小写[如java.util.Iterator中的'iterator']，name:为Bean的属性名称
	 * @param bean PO 类
	 * @param paramMap 参数键值对
	 * @param SPLIT_DELIM 参数分隔符
	 * @param parameterProcessor 参数处理接口
	 * @parem excludePropName 不处理的参数列表
	 * @return PO对象
	 */
	public static Object map2PO(Class bean ,Map paramMap,String SPLIT_DELIM,ParameterProcessor parameterProcessor ,List excludePropName) {
		String className = StringUtils.unqualify(bean.getName().toLowerCase());
		HashMap hashObj = new HashMap();
		Object _srcObj = null ;
		try {
			String strKey = null;
			Object objValue = null;
			String strPropName = null ;
			_srcObj = bean.newInstance();
			Iterator itrKeys = paramMap.keySet().iterator();
			PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(bean);			
			while (itrKeys.hasNext()) {
				strKey = (String) itrKeys.next();
				objValue = paramMap.get(strKey);
				if (SPLIT_DELIM != null && strKey.indexOf(SPLIT_DELIM) != -1) {
					//取对象名称和属性名称boj.name
					String[] arry = StringUtils.split(strKey, SPLIT_DELIM);
					if (className.equalsIgnoreCase(arry[0])) {
						//取属性名称
						strPropName = arry[1];
						if(null != excludePropName && excludePropName.contains(strPropName))continue ;
						_setObjectValue(_srcObj, strPropName,objValue);
					}
				}else{
					strPropName = strKey ;
					if(null != excludePropName && excludePropName.contains(strPropName))continue ;
					//System.out.println("name:"+strPropName + ",writeable : " + PropertyUtils.isWriteable(_srcObj,strPropName) );
					for(int i =0 ; i < descriptors.length ;i++){
						if(descriptors[i].getName().equals(strPropName) && PropertyUtils.isWriteable(_srcObj,strPropName)){
							_setObjectValue(_srcObj, strPropName , objValue);
						}
					}
				}
			}			
		} catch (Exception ex) {
			System.out.println(":::::::::::::::::::::::::::::::::::::hello11");
			//ex.printStackTrace();
			SysException.handleMessageException(ex.getMessage());
		}
		if(parameterProcessor != null) parameterProcessor.parameterProcess(new ParameterRecord((HashMap)paramMap), _srcObj);
		return _srcObj ;
	}
	private static void _setObjectValue(Object bean,String strPropName,Object objValue){
		try{
			Object objConvert = TypeConvertorUtils.convert(objValue, PropertyUtils.getPropertyType(bean, strPropName));
			BeanUtils.setProperty(bean, strPropName,objConvert);
		}catch(Exception ex){
			System.out.println(":::::::::::::::::::::::::::::::::::::hello");
			SysException.handleMessageException(ex.getMessage());
		}
	}	
	/**
	 * 获得对象属性名称列表
	 * 
	 * @param bean
	 * @return
	 */
	public static List getFields(Object bean) {
		List listField = new ArrayList();
		PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(bean);
		for(int i = 0 ; i < descriptors.length ; i++){
			if(PropertyUtils.isWriteable(bean,descriptors[i].getName())){
				listField.add(descriptors[i].getName());
			}
		}
		return listField;
	}
	/**
	 * 获得对象属性名称列表
	 * 
	 * @param bean
	 * @return
	 */
	public static Map<String ,Object> po2map(Object obj,boolean isIncludeNull) {
		Map<String ,Object> _map = new HashMap();
		PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(obj);
		for(int i = 0 ; i < descriptors.length ; i++){
			String strPropName = descriptors[i].getName();
			if(PropertyUtils.isWriteable(obj,descriptors[i].getName())){
				if (!"class".equals(strPropName)) {
					Object _objValue = ClassUtils.get(obj, strPropName);
					if(isIncludeNull){
						_map.put(strPropName,_objValue);
					}else{
						if(_objValue != null)_map.put(strPropName,_objValue);
					}
				}
			}
		}
		return _map ;
	}
	/**
	 * 获得对象属性名称列表
	 * 
	 * @param bean
	 * @return
	 */
	private static void _getFields(Class cls, List listField) {
		Field[] fieldArray = cls.getDeclaredFields();
		// String[] strFieldArray = new String[fieldArray.length];
		for (int i = 0; i < fieldArray.length; i++) {
			Field field = fieldArray[i];
			if (!listField.contains(field.getName()))
				listField.add(field.getName());
		}
		Class superCls = cls.getSuperclass();
		// System.out.println(superCls.getName());
		if (superCls.getName() != Object.class.getName()) {
			_getFields(superCls, listField);
		}
	}

	/**
	 * 获得对象属性名称列表
	 * 
	 * @param bean
	 * @return
	 */
	public static String[] getFields2(Object bean) {
		Field[] fieldArray = bean.getClass().getFields();
		String[] strFieldArray = new String[fieldArray.length];
		for (int i = 0; i < fieldArray.length; i++) {
			Field field = fieldArray[i];
			strFieldArray[i] = field.getName();
		}
		return strFieldArray;
	}
	/**
	 * 获得对象方法名称列表
	 * 
	 * @param bean
	 * @return
	 */
	public static List getMethods(Class cls) {
		List listMethod = new ArrayList();
		_getMethods(cls, listMethod);
		return listMethod;
	}
	/**
	 * 获得对象方法名称列表
	 * 
	 * @param bean
	 * @return
	 */
	private static void _getMethods(Class cls, List listMethod) {
		Method[] methodArray = cls.getDeclaredMethods();
		for (int i = 0; i < methodArray.length; i++) {
			Method method = methodArray[i];
			if (!listMethod.contains(method.getName()))
				listMethod.add(method.getName());
		}
		Class superCls = cls.getSuperclass();
		if(superCls != null){
			if (superCls.getName() != Object.class.getName()) {
				_getMethods(superCls, listMethod);
			}
		}
	}

	/**
	 * 获得对象方法名称列表
	 * @param bean
	 * @return
	 */
	public static String[] getMethods2(Object bean){
		Method[] methodArray = bean.getClass().getMethods();
		String[] strMethodArray = new String[methodArray.length];
		for (int i = 0; i < methodArray.length; i++) {
			Method method = methodArray[i];
			strMethodArray[i] = method.getName();
		}
		return strMethodArray;
	}
	/**
	 * 将对象转换为Map格式
	 * 
	 * @param bean
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	/*
	 * public static Map describe(Object bean) throws IllegalAccessException,
	 * InvocationTargetException, NoSuchMethodException { if (bean == null) { //
	 * return (Collections.EMPTY_MAP); return (new java.util.HashMap()); } Map
	 * description = new HashMap();
	 * 
	 * PropertyDescriptor[] descriptors = PropertyUtils
	 * .getPropertyDescriptors(bean); for (int i = 0; i < descriptors.length;
	 * i++) { String name = descriptors[i].getName(); try { if
	 * (PropertyUtils.getReadMethod(descriptors[i]) != null) {
	 * description.put(name, PropertyUtils.getProperty(bean, name)); } } catch
	 * (Exception ex) { } } return (description); }
	 */
	/**
	 * 将对象转换为String字符串
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static String object2String(Object obj) {
		StringBuffer buff = new StringBuffer();
		try {
			Map mapObj = BeanUtils.describe(obj);
			Set mapPropName = mapObj.keySet();
			Iterator itr = mapPropName.iterator();
			while (itr.hasNext()) {
				String strPropName = (String) itr.next();
				if (!"class".equals(strPropName)) {
					String strGetMethodName = "get"+ StringUtils.capitalizeFirst(strPropName);
					Object objPropValue = ClassUtils.invoke(obj,strGetMethodName);
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

	/**
	 * 把源对象值复制到目标对象
	 * @param originObj     源对象
	 * @param destObj       目标对象
   * @throws Exception
   */
	public static void copyProperties(Object originObj, Object destObj)
	throws Exception {
		copyProperties(originObj,destObj,false);
	}
	
	/**
	 * 把源对象值复制到目标对象
	 * @param originObj     源对象
	 * @param destObj       目标对象
	 * @param isIncludeNullValue 是否将空值属性覆盖目标对象属性
	 * @throws Exception
	 */
	public static void copyProperties(Object _origObj, Object _destObj,boolean _isIncludeNullValue)
			throws Exception {
		PropertyDescriptor[] _descArray = PropertyUtils.getPropertyDescriptors(_origObj.getClass());
		for(int i = 0 ; i < _descArray.length;i++){
			if(PropertyUtils.isWriteable(_origObj,_descArray[i].getName())){
				String _getMethodName = "get" + StringUtils.capitalizeFirst(_descArray[i].getName());
				Object _propValueObj = ClassUtils.invoke(_origObj, _getMethodName);
//				if(objPropValue != null)
//					System.out.println("::::::::::::descriptors[i].getName() = " + descriptors[i].getName() + ", objPropValue = " + objPropValue.getClass());
				
				if(_isIncludeNullValue&&StringUtils.isNull(_propValueObj)) {
					BeanUtils.setProperty(_destObj, _descArray[i].getName(), _propValueObj);
					continue;
				}
				
				if(!_isIncludeNullValue&&StringUtils.isNull(_propValueObj)) {
					continue;
				}
				
				if (_propValueObj instanceof java.sql.Timestamp) {
						Object _obj = TypeConvertorUtils.convert(_propValueObj,java.sql.Timestamp.class);
						BeanUtils.setProperty(_destObj, _descArray[i].getName(), _obj);
				}else if (_propValueObj instanceof java.sql.Date) {
						Object _obj = TypeConvertorUtils.convert(_propValueObj,java.util.Date.class);
						BeanUtils.setProperty(_destObj, _descArray[i].getName(), _obj);
				}else{
						BeanUtils.setProperty(_destObj, _descArray[i].getName(), _propValueObj);
				}
			}
		}
	}
	
	/**
	 * 根据属性选择集合,把源对象值复制到目标对象
	 * @param originObj     源对象
	 * @param destObj       目标对象
	 * @param selectMap     属性选择集合
   * @throws Exception
   */
	public static void copyProperties(Object _origObj, Object _destObj, Map _selectMap)throws Exception {
		
		Map mapObj = BeanUtils.describe(_origObj);
		Set mapPropName = mapObj.keySet();
		Iterator itr = mapPropName.iterator();
		while (itr.hasNext()) {
			String strPropName = (String) itr.next();
			if (!"class".equals(strPropName)) {
				String strGetMethodName = "get" + StringUtils.capitalizeFirst(strPropName);
				Object objPropValue = ClassUtils.invoke(_origObj, strGetMethodName);
				if (objPropValue != null) {
					String strConvPropName = (String) _selectMap.get(strPropName);
					if (strConvPropName == null)continue;
					BeanUtils.setProperty(_destObj, strConvPropName,objPropValue);
				}
			}
		}
	}
	
	
	
	/**
	 * <p>
	 * The nibbles' hexadecimal values. A nibble is a half byte.
	 * </p>
	 */
	private static final byte hexval[] = { (byte) '0', (byte) '1', (byte) '2',
			(byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7',
			(byte) '8', (byte) '9', (byte) 'A', (byte) 'B', (byte) 'C',
			(byte) 'D', (byte) 'E', (byte) 'F' };

	/**
	 * <p>
	 * Decodes the hexadecimal representation of a sequence of bytes into a byte
	 * array. Each character in the string represents a nibble (half byte) and
	 * must be one of the characters '0'-'9', 'A'-'F' or 'a'-'f'.
	 * </p>
	 * 
	 * @param s
	 *            The string to be decoded
	 * 
	 * @return The bytes
	 * 
	 * @throw IllegalArgumentException if the string does not contain a valid
	 *        representation of a byte sequence.
	 */
	private static byte[] hexDecode(final String s) {
		final int length = s.length();

		/*
		 * The string to be converted must have an even number of characters.
		 */
		if (length % 2 == 1) {
			throw new IllegalArgumentException("String has odd length "
					+ length);
		}
		byte[] b = new byte[length / 2];
		char[] c = new char[length];
		s.toUpperCase().getChars(0, length, c, 0);
		for (int i = 0; i < length; i += 2) {
			b[i / 2] = (byte) (decodeNibble(c[i]) << 4 & 0xF0 | decodeNibble(c[i + 1]) & 0x0F);
		}
		return b;
	}

	/**
	 * <p>
	 * Decodes a nibble.
	 * </p>
	 * 
	 * @param c
	 *            A character in the range '0'-'9' or 'A'-'F'. Lower case is not
	 *            supported here.
	 * 
	 * @return The decoded nibble in the range 0-15
	 * 
	 * @throws IllegalArgumentException
	 *             if <em>c</em> is not a permitted character
	 */
	private static byte decodeNibble(final char c) {
		for (byte i = 0; i < hexval.length; i++) {
			if ((byte) c == hexval[i]) {
				return i;
			}
		}
		throw new IllegalArgumentException("\"" + c + "\""
				+ " does not represent a nibble.");
	}

	/**
	 * <p>
	 * Converts a byte array into its hexadecimal notation.
	 * </p>
	 */
	private static String hexEncode(final byte[] s) {
		return hexEncode(s, 0, s.length);
	}

	/**
	 * <p>
	 * Converts a part of a byte array into its hexadecimal notation.
	 * </p>
	 */
	private static String hexEncode(final byte[] s, final int offset,
			final int length) {
		StringBuffer b = new StringBuffer(length * 2);
		for (int i = offset; i < offset + length; i++) {
			int c = s[i];
			b.append((char) hexval[(c & 0xF0) >> 4]);
			b.append((char) hexval[(c & 0x0F) >> 0]);
		}
		return b.toString();
	}

	/**
	 * 将对象序列化成字符串
	 * <p>
	 * Converts a string into its hex adecimal notation.
	 * </p>
	 * <p>
	 * <strong>FIXME:</strong> If this method is called frequently, it should
	 * directly implement the algorithm in the called method in order to avoid
	 * creating a string instance.
	 * </p>
	 */
	public static String hexEncoded(Object obj) throws Exception {
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

	/**
	 * 对象序列化字符串还原成对象
	 * 
	 * @param bytes
	 * @return
	 * @throws java.lang.Exception
	 */
	public static Object hexDecoded(final String bytes) throws Exception {
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

	/**
	 * 序列化对象为压缩的字节数组
	 * 
	 * @param object
	 * @return
	 * @throws java.lang.Exception
	 */
	public static byte[] serializer(Object object) throws Exception {

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
			//LogUtils.info(objectBytes.length + "-" + zipBytes.length);
		} finally {
			try {
				if (objectOut != null) {
					objectOut.close();
				}
				if (gZIPOutputStream != null) {
					gZIPOutputStream.close();
				}
				if (zipOut != null) {
					zipOut.close();
				}

			} finally {
				objectBytes = null;
				objectOut = null;
				gZIPOutputStream = null;
				zipOut = null;
			}
		}
		return zipBytes;

	}

	/**
	 * 序列化压缩的字节数组还原对象
	 * 
	 * @param zipBytes
	 * @return
	 * @throws java.lang.Exception
	 */

	public static Object deSerializer(byte[] zipBytes) throws Exception {

		GZIPInputStream gZIPInputStream = null;
		ByteArrayOutputStream unzipOut = null;
		ObjectInputStream objectIn = null;
		byte[] unzipBytes = null;
		byte[] buf = new byte[1024];
		Object object = null;
		try {
			gZIPInputStream = new GZIPInputStream(new ByteArrayInputStream(
					zipBytes));
			unzipOut = new ByteArrayOutputStream();
			int len;
			while ((len = gZIPInputStream.read(buf)) >= 0) {
				unzipOut.write(buf, 0, len);
			}
			unzipBytes = unzipOut.toByteArray();
			objectIn = new ObjectInputStream(new ByteArrayInputStream(
					unzipBytes));
			object = objectIn.readObject();
		} finally {
			try {
				if (gZIPInputStream != null) {
					gZIPInputStream.close();
				}
				if (unzipOut != null) {
					unzipOut.close();
				}
				if (objectIn != null) {
					objectIn.close();
				}
			} finally {
				unzipBytes = null;
				buf = null;
				gZIPInputStream = null;
				unzipOut = null;
				objectIn = null;
			}
		}
		return object;
	}
	/**
	 * 将Set 转换为 List
	 * 
	 * @param set
	 * @return List
	 */
	public static List changeSetToList(Set set) {
		Iterator setIter = set.iterator();
		List list = new ArrayList();
		while (setIter.hasNext()) {
			list.add(setIter.next());
		}
		return list;
	}

	/**
	 * 将对象的所有属性名称以String类型放入List中,并返回该List.
	 * 
	 * @param obj
	 * @return List
	 */
	public static List getObjectAttributeNames(Object obj) {
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
			String strPropName = (String) itr.next();
			if (!"class".equals(strPropName)) {
				list.add(strPropName);
			}
		}
		return list;
	}

}
