package org.maccha.base.exception;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.maccha.spring.SpringManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class BaseExceptionTest extends TestCase{
	private BaseException baseException;
	@Before
    public void setUp() throws Exception {
		System.out.println("start:");
    }
	@After
	public void tearDown() throws Exception {
		System.out.println("end:");
	}
	@Test
	public void testGetMessage() {
		SpringManager springManager = new SpringManager();
		String[] paths = { "config/oracle_config/spring_config/applicationContext.xml"};
		ApplicationContext ctx = new FileSystemXmlApplicationContext(paths);
		springManager.setApplicationContext(ctx);
		Object [] args = {1, "2"};
		baseException = new BaseException("baseException", args, "defaultMessage", new Throwable());
		System.out.println(baseException.getMessage());
	}

	@Test
	public void testHandleErrorThrowable() {
		BaseException.handleError(new Throwable("testHandleErrorThrowable"));
	}

	@Test
	public void testHandleErrorStringThrowable() {
		BaseException.handleError("testHandleErrorStringThrowable", new Throwable("testHandleErrorStringThrowable"));
	}

	@Test
	public void testHandleWarnThrowable() {
		BaseException.handleWarn(new Throwable("testHandleWarnThrowable"));
	}

	@Test
	public void testHandleWarnStringThrowable() {
		BaseException.handleWarn("testHandleWarnStringThrowable", new Throwable("testHandleWarnStringThrowable"));
	}

	@Test
	public void testHandleWarnString() {
		BaseException.handleWarn("testHandleWarnString");
	}

	@Test
	public void testHandleInfoThrowable() {
		BaseException.handleInfo(new Throwable("testHandleInfoThrowable"));
	}

	@Test
	public void testHandleInfoStringThrowable() {
		BaseException.handleInfo("testHandleInfoStringThrowable", new Throwable("testHandleInfoStringThrowable"));
	}

	@Test
	public void testHandleInfoString() {
		BaseException.handleInfo("testHandleInfoString");
	}

}
