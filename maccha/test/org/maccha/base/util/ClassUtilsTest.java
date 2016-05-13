package org.maccha.base.util;

import junit.framework.TestCase;
import org.junit.Test;
import org.maccha.base.exception.BaseException;
import org.maccha.base.metadata.ObjectMetadata;
import org.maccha.base.metadata.PropertyMetadata;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by Lori Yue on 16-3-4.
 */
public class ClassUtilsTest extends TestCase {
    @Test
    public void testGet() throws Exception {
        ObjectMetadata o = new ObjectMetadata();
        o.setEntityName("entity");
        assertEquals("entity", ClassUtils.get(o, "entityName"));
        assertEquals(null, ClassUtils.get(o, "tableName"));
    }

    @Test
    public void testSet() throws Exception {
        ObjectMetadata o = new ObjectMetadata();
        String value = "entity";
        ClassUtils.set(o, "entityName", value);
        assertEquals(value, o.getEntityName());
    }

    @Test
    public void testSet1() throws Exception {
        ObjectMetadata o = new ObjectMetadata();
        String value = "entity";
        ClassUtils.set(o, "entityName", String.class, value);
        assertEquals(value, o.getEntityName());
    }

    @Test
    public void testGetFieldValue() throws Exception {
        ObjectMetadata o = new ObjectMetadata();
        o.setEntityName("entity");
        assertEquals("entity", ClassUtils.getFieldValue(o, "entityName"));
        assertEquals(null, ClassUtils.getFieldValue(o, "tableName"));
    }

    @Test
    public void testSetFieldValue() throws Exception {
        ObjectMetadata o = new ObjectMetadata();
        ClassUtils.setFieldValue(o, "entityName", "entity");
        assertEquals("entity", o.getEntityName());
    }

    @Test
    public void testInvoke() throws Exception {
        ObjectMetadata o = new ObjectMetadata();
        o.setEntityName("entity");
        assertEquals("entity", ClassUtils.invoke(o, "getEntityName", null, null));
        assertEquals(null, ClassUtils.invoke(o, "getTableName", null, null));
    }

    @Test
    public void testInvoke1() throws Exception {
        ObjectMetadata o = new ObjectMetadata();
        Object [] args = new Object[1];
        args[0] = "entity";
        assertEquals(null, ClassUtils.invoke(o, "setEntityName", args));
    }

    @Test
    public void testInvokeStaticMethod() throws Exception {
        Object[] args = new Object[2];
        args[0] = "http://maccha.org?uname=lori";
        args[1] = "uname";
        assertEquals("lori", ClassUtils.invokeStaticMethod(StringUtils.class, "getURLParameter", args));
    }

    @Test
    public void testInvokeStaticMethod1() throws Exception {
        Class[] argsType = new Class[2];
        argsType[0] = String.class;
        argsType[1] = String.class;
        Object[] args = new Object[2];
        args[0] = "http://maccha.org?uname=lori";
        args[1] = "uname";
        assertEquals("lori", ClassUtils.invokeStaticMethod(StringUtils.class, "getURLParameter", argsType, args));
    }

    @Test
    public void testInvoke2() throws Exception {
        ObjectMetadata o = new ObjectMetadata();
        o.setEntityName("entity");
        assertEquals("entity", ClassUtils.invoke(o, "getEntityName"));
    }

    @Test
    public void testGetAccessibleField() throws Exception {
    	TestClass testClass = new TestClass();
    	Field field = ClassUtils.getAccessibleField(testClass, "testField");
    	assertEquals("1", (String)field.get(testClass));
    	field.set(testClass, "2");
    	assertEquals("2", (String)field.get(testClass));
    }

    @Test
    public void testGetAccessibleMethod() throws Exception {
    	TestClass testClass = new TestClass();
    	Class [] argTypes = {int.class, int.class};
    	Method method = ClassUtils.getAccessibleMethod(testClass, "max", argTypes);
    	Object result = method.invoke(testClass, 100, 120);
    	assertEquals(120, (Integer)result+0);
    }

    @Test
    public void testGetAccessibleMethodByName() throws Exception {
    	TestClass testClass = new TestClass();
    	Method method = ClassUtils.getAccessibleMethodByName(testClass, "max");
    	Object result = method.invoke(testClass, 100, 120);
    	assertEquals(120, (Integer)result+0);
    }

    @Test
    public void testMakeAccessible() throws Exception {
    	TestClass testClass = new TestClass();
    	Method method = testClass.getClass().getDeclaredMethod("max", new Class[] {int.class, int.class});
    	ClassUtils.makeAccessible(method);
    	assertEquals(150, (Integer)method.invoke(testClass, 150, 100)+0); 
    }

    @Test
    public void testMakeAccessible1() throws Exception {
    	TestClass testClass = new TestClass();
    	Field field = testClass.getClass().getDeclaredField("testField");
    	ClassUtils.makeAccessible(field);
    	assertEquals("1", (String)field.get(testClass));
    }

    @Test
    public void testGetClassGenricType() throws Exception {
    	Class cl = ClassUtils.getClassGenricType(TestClass.class);
    	assertEquals(String.class, cl);
    }

    @Test
    public void testGetClassGenricType1() throws Exception {
    	Class cl = ClassUtils.getClassGenricType(TestClass.class, 1);
    	assertEquals(Integer.class, cl);
    }

    @Test
    public void testGetUserClass() throws Exception {
        BigDecimal b = new BigDecimal("1000");
        assertEquals(BigDecimal.class, ClassUtils.getUserClass(b));
    }

    @Test
    public void testConvertReflectionExceptionToUnchecked() throws Exception {
        IllegalAccessException e1 = new IllegalAccessException();
        IllegalArgumentException e2 = new IllegalArgumentException();
        NoSuchMethodException e3 = new NoSuchMethodException();
        RuntimeException e5 = new RuntimeException();
        NullPointerException e6 = new NullPointerException();
        assertTrue(ClassUtils.convertReflectionExceptionToUnchecked(e1) instanceof IllegalArgumentException);
        assertTrue(ClassUtils.convertReflectionExceptionToUnchecked(e2) instanceof IllegalArgumentException);
        assertTrue(ClassUtils.convertReflectionExceptionToUnchecked(e3) instanceof IllegalArgumentException);
        assertTrue(ClassUtils.convertReflectionExceptionToUnchecked(e5) instanceof RuntimeException);
        assertTrue(ClassUtils.convertReflectionExceptionToUnchecked(e6) instanceof RuntimeException);
    }

    @Test
    public void testGetInstance() throws Exception {
        Class[] argsType = {String.class, Throwable.class};
        Object[] args = {"呵呵呵", new Throwable()};
        BaseException e = (BaseException)ClassUtils.getInstance(BaseException.class, argsType, args);
        assertEquals(args[0], e.getMessage());
    }

    @Test
    public void testIsImplements() throws Exception {
        assertEquals(true, ClassUtils.isImplements(ObjectMetadata.class, Serializable.class));
        assertEquals(false, ClassUtils.isImplements(ObjectMetadata.class, Comparable.class));
    }

    @Test
    public void testGetClasses() throws Exception {
        Class[] c = new Class[3];
        c[0] = Object.class;
        c[1] = ObjectMetadata.class;
        c[2] = PropertyMetadata.class;
        Object[] o = new Object[3];
        o[0] = new Object();
        o[1] = new ObjectMetadata();
        o[2] = new PropertyMetadata();
        Class[] result = ClassUtils.getClasses(o);
        assertEquals(c.length, result.length);
        assertEquals(c[0], result[0]);
        assertEquals(c[1], result[1]);
        assertEquals(c[2], result[2]);
    }

    @Test
    public void testGetType() throws Exception {
        assertEquals(String.class, ClassUtils.getType(ObjectMetadata.class, "entityName"));
    }
    
    class TestClass extends HashMap<String, Integer> {
    	private String testField = "1";
    	
    	private int max(int a, int b) {
    		return a > b ? a : b;
    	}
    }
}
