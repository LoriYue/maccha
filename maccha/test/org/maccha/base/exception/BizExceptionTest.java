package org.maccha.base.exception;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.maccha.spring.SpringManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class BizExceptionTest extends TestCase{
	private BizException bizException;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}
	@Before
    public void setUp() throws Exception {
		System.out.println("start:");
    }
	@After
	public void tearDown() throws Exception {
		System.out.println("end:");
	}
	@Test
	public void testHandleExceptionStringThrowable() {
		SpringManager springManager = new SpringManager();
		String[] paths = { "config/oracle_config/spring_config/applicationContext.xml"};
		ApplicationContext ctx = new FileSystemXmlApplicationContext(paths);
		springManager.setApplicationContext(ctx);
		Throwable t = null;
		try {
			BizException.handleException("testHandleExceptionStringThrowable", new Throwable("testHandleExceptionStringThrowable"));
		} catch (Exception e) {
			t = e;
		}
		assertEquals(new Throwable("testHandleExceptionStringThrowable").toString(), t.getCause().toString());
		assertNotNull(t);
	    assertTrue(t instanceof BizException);
	    assertTrue(t.getMessage().contains("testHandleExceptionStringThrowable"));
	}

	@Test
	public void testHandleExceptionString() {
		Throwable t = null;
		try {
			BizException.handleException("testHandleExceptionString");
		} catch (Exception e) {
			t = e;
		}
		assertEquals(null, t.getCause());
		assertNotNull(t);
	    assertTrue(t instanceof BizException);
	    assertTrue(t.getMessage().contains("testHandleExceptionString"));
	}

	@Test
	public void testHandleMessageExceptionStringThrowable() {
		Throwable t = null;
		try {
			BizException.handleMessageException("testHandleMessageExceptionStringThrowable", new Throwable("testHandleMessageExceptionStringThrowable"));
		} catch (Exception e) {
			t = e;
		}
		assertEquals(new Throwable("testHandleMessageExceptionStringThrowable").toString(), t.getCause().toString());
		assertNotNull(t);
	    assertTrue(t instanceof BizException);
	    assertTrue(t.getMessage().contains("testHandleMessageExceptionStringThrowable"));
	}

	@Test
	public void testHandleMessageExceptionString() {
		Throwable t = null;
		try {
			BizException.handleMessageException("testHandleMessageExceptionString");
		} catch (Exception e) {
			t = e;
		}
		assertEquals(null, t.getCause());
		assertNotNull(t);
	    assertTrue(t instanceof BizException);
	    assertTrue(t.getMessage().contains("testHandleMessageExceptionString"));
	}

	@Test
	public void testHandleExceptionStringObjectArray() {
		Object [] args = {1, "2"};
		Throwable t = null;
		try {
			BizException.handleException("testHandleExceptionStringObjectArray", args);
		} catch (Exception e) {
			t = e;
		}
		assertEquals(null, t.getCause());
		assertNotNull(t);
	    assertTrue(t instanceof BizException);
	    assertTrue(t.getMessage().contains("testHandleExceptionStringObjectArray"));
	}

	@Test
	public void testHandleExceptionStringStringThrowable() {
		Throwable t = null;
		try {
			BizException.handleException("testHandleExceptionStringStringThrowableKey", "testHandleExceptionStringStringThrowableMessage", new Throwable("testHandleExceptionStringStringThrowable"));
		} catch (Exception e) {
			t = e;
		}
		assertEquals(new Throwable("testHandleExceptionStringStringThrowable").toString(), t.getCause().toString());
		assertNotNull(t);
	    assertTrue(t instanceof BizException);
	    assertTrue(t.getMessage().contains("testHandleExceptionStringStringThrowableKey"));
	}

}
