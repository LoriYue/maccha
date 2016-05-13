package org.maccha.base.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.lang.reflect.*;

/**
 * Created by Lori Yue on 16-3-4.
 */
public class ClassUtils {
    private static final String SETTER_PREFIX = "set";
    private static final String GETTER_PREFIX = "get";
    private static final String CGLIB_CLASS_SEPARATOR = "$$";
    private static Logger logger = LoggerFactory.getLogger(ClassUtils.class);
    /**
	 * 调用Getter方法.
	 * 支持多级，如：对象名.对象名.方法
	 */
    public static Object get(Object obj, String propertyName) {
    	Object object = obj;
		for (String name : StringUtils.split(propertyName, ".")){
			String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(name);
			object = invoke(object, getterMethodName, new Class[] {}, new Object[] {});
		}
		return object;
    }
    /**
	 * 调用Setter方法, 仅匹配方法名。
	 * 支持多级，如：对象名.对象名.方法
	 */
    public static void set(Object obj, String propertyName, Object value) {
    	Object object = obj;
		String[] names = StringUtils.split(propertyName, ".");
		for (int i=0; i<names.length; i++){
			if(i<names.length-1){
				String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(names[i]);
				object = invoke(object, getterMethodName, new Class[] {}, new Object[] {});
			}else{
				String setterMethodName = SETTER_PREFIX + StringUtils.capitalize(names[i]);
				invoke(object, setterMethodName, new Object[] { value });
			}
		}
    }
    /**
	 * 设置Bean实例的属性值
	 * @param obj
	 * @param fieldName 属性名
	 * @param value
	 * @throws Exception
	 */
    public static void set(Object obj, String propertyName, Class objectType, Object value) throws Exception {
    	Object object = obj;
		String[] names = StringUtils.split(propertyName, ".");
		for (int i=0; i<names.length; i++){
			if(i<names.length-1){
				String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(names[i]);
				object = invoke(object, getterMethodName, new Class[] {}, new Object[] {});
			}else{
				String setterMethodName = SETTER_PREFIX + StringUtils.capitalize(names[i]);
				invoke(object, setterMethodName,new Class[] { objectType }, new Object[] { value });
			}
		}
    }
    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     */
    public static Object getFieldValue(Object obj, String fieldName) {
    	Field field = getAccessibleField(obj, fieldName);
		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}
		Object result = null;
		try {
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常{}", e.getMessage());
		}
		return result;
    }
    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     */
    public static void setFieldValue(Object obj, String fieldName, Object value) {
    	Field field = getAccessibleField(obj, fieldName);
		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}
		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}
    }
    /**
     * 直接调用对象方法, 无视private/protected修饰符.
	 * 用于一次性调用的情况，否则应使用getAccessibleMethod()函数获得Method后反复调用.
	 * 同时匹配方法名+参数类型，
     */
    public static Object invoke(Object obj, String methodName, Class<?>[] parameterTypes, Object[] args) {
    	Method method = getAccessibleMethod(obj, methodName, parameterTypes);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}
		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
    }
    /**
     * 直接调用对象方法, 无视private/protected修饰符，
	 * 用于一次性调用的情况，否则应使用getAccessibleMethodByName()函数获得Method后反复调用.
	 * 只匹配函数名，如果有多个同名函数调用第一个。
     */
    public static Object invoke(Object obj, String methodName, Object[] args) {
    	Method method = getAccessibleMethodByName(obj, methodName);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}
		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
    }
    /**
	 * 通过方法名调用类方法
	 * @param cls 类
	 * @param methodName 方法名字
	 * @param argsType 参数类型类表
	 * @param args 参数列表
	 * @return Object
	 * @throws java.lang.Exception
	 */
    public static Object invokeStaticMethod(Class cls, String methodName, Object[] args) throws Exception {
    	Object objModel = null ;
		try{
			Method m = cls.getMethod(methodName, getClasses(args));
			return m.invoke(cls, args);
		}catch(Exception ex){
		}
		return objModel ;
    }
    /**
     * 通过方法名调用类方法
	 * @param cls 类
	 * @param methodName 方法名字
	 * @param argsType 参数类型类表
	 * @param args 参数列表
	 * @return Object
	 * @throws java.lang.Exception
     */
    public static Object invokeStaticMethod(Class cls, String methodName, Class[] argsType, Object[] args) throws Exception {
    	Object objModel = null ;
		try{
			Method m = cls.getMethod(methodName, argsType);
			return m.invoke(cls, args);
		}catch(Exception ex){
		}
		return objModel ;
    }
    /**
     * 通过方法名调用对象无参数方法
     * @param invokeObj 对象
     * @param methodName 方法名
     * @return Object类型的返回值
     */
    public static Object invoke(Object invokeObj, String methodName) throws Exception {
        return invoke(invokeObj, methodName, null, null);
    }
    /**
     * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.
     * @param obj 对象
     * @param fieldName 属性名
     * @return 如向上转型到Object仍无法找到, 返回null.
     */
    public static Field getAccessibleField(Object obj, String fieldName) {
    	Validate.notNull(obj, "object can't be null");
		Validate.notBlank(fieldName, "fieldName can't be blank");
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				makeAccessible(field);
				return field;
			} catch (NoSuchFieldException e) {//NOSONAR
				continue;// new add
			}
		}
		return null;
    }
    /**
	 * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
	 * 如向上转型到Object仍无法找到, 返回null.
	 * 匹配函数名+参数类型。
	 * 
	 * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
	 */
    public static Method getAccessibleMethod(Object obj, String methodName, Class<?>[] parameterTypes) {
    	Validate.notNull(obj, "object can't be null");
		Validate.notBlank(methodName, "methodName can't be blank");
		for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
			try {
				Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
				makeAccessible(method);
				return method;
			} catch (NoSuchMethodException e) {
				// Method不在当前类定义,继续向上转型
				continue;// new add
			}
		}
		return null;
    }
    /**
	 * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
	 * 如向上转型到Object仍无法找到, 返回null.
	 * 只匹配函数名。
	 * 
	 * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
	 */
    public static Method getAccessibleMethodByName(Object obj, String methodName) {
    	Validate.notNull(obj, "object can't be null");
		Validate.notBlank(methodName, "methodName can't be blank");
		for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
			Method[] methods = searchType.getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					makeAccessible(method);
					return method;
				}
			}
		}
		return null;
    }
    /**
	 * 改变private/protected的方法为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
	 */
    public static void makeAccessible(Method method) {
    	if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
				&& !method.isAccessible()) {
			method.setAccessible(true);
		}
    }
    /**
     * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     */
    public static void makeAccessible(Field field) {
    	if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier
				.isFinal(field.getModifiers())) && !field.isAccessible()) {
			field.setAccessible(true);
		}
    }
    /**
	 * 通过反射, 获得Class定义中声明的泛型参数的类型, 注意泛型必须定义在父类处
	 * 如无法找到, 返回Object.class.
	 * eg.
	 * public UserDao extends HibernateDao<User>
	 *
	 * @param clazz The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be determined
	 */
    public static <T> Class<T> getClassGenricType(Class clazz) {
        return getClassGenricType(clazz, 0);
    }
    /**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
	 * 如无法找到, 返回Object.class.
	 * 
	 * 如public UserDao extends HibernateDao<User,Long>
	 *
	 * @param clazz clazz The class to introspect
	 * @param index the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or Object.class if cannot be determined
	 */
    public static Class getClassGenricType(Class clazz, int index) {
    	Type genType = clazz.getGenericSuperclass();
		if (!(genType instanceof ParameterizedType)) {
			logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if (index >= params.length || index < 0) {
			logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}
		return (Class) params[index];
    }
    /**
     * 获得对象（包括代理对象）真实类型
     * @param instance 实例对象
     * @return Class
     */
    public static Class<?> getUserClass(Object instance) {
    	Assert.notNull(instance, "Instance must not be null");
		Class clazz = instance.getClass();
		if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
			Class<?> superClass = clazz.getSuperclass();
			if (superClass != null && !Object.class.equals(superClass)) {
				return superClass;
			}
		}
		return clazz;
    }
    /**
     * 将反射时的checked exception转换为unchecked exception.
     * @param e 异常
     * @return IllegalAccessException，IllegalArgumentException，NoSuchMethodException转换为IllegalArgumentException，其他转换为RuntimeException
     */
    public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
    	if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException) {
			return new IllegalArgumentException(e);
		} else if (e instanceof InvocationTargetException) {
			return new RuntimeException(((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException("Unexpected Checked Exception.", e);
    }
    /**
     * 指定构造参数类型和参数构造类实例
     * @param cls 类
     * @param argsType 参数类型数组
     * @param args 参数数组
     * @return 实例化的对象
     * @throws Exception
     */
    public static Object getInstance(Class cls, Class[] argsType, Object[] args) throws Exception {
        Object obj = null;
        Constructor constructor = cls.getConstructor(argsType);
        obj = constructor.newInstance(args);
        return obj;
    }
    /**
     * 检查一个类是否实现了指定的接口
     * @param classClass 类
     * @param interfaceClass 接口
     * @return
     */
    public static boolean isImplements(Class classClass, Class interfaceClass) {
    	Class[] interfaceClassL = classClass.getInterfaces();
		for (int i = 0; interfaceClassL != null && i < interfaceClassL.length; i++) {
			if (interfaceClass.equals(interfaceClassL[i]))
				return true;
		}
		return false;
    }
    /**
     * 获得数组中每个元素的类型，并返回结果为Class类型的数组
     * @param objects 数组
     * @return
     */
    public static Class[] getClasses(Object[] objects) {
    	if (objects == null) {
			return null;
		}
		Class[] result = new Class[objects.length];
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] != null) {
				result[i] = objects[i].getClass();
			}
		}
		return result;
    }
    /**
     * 获得类的某个成员属性的类型
     * @param bean  类
     * @param field 成员属性，必须是有get方法的
     * @return class
     */
    public static Class getType(Class bean, String field) {
    	try {
			String strMethodName = GETTER_PREFIX + StringUtils.capitalize(field);
			return bean.getMethod(strMethodName, new Class[] {}).getReturnType();
		} catch (Exception ex) {
		}
		return null;
    }

}
