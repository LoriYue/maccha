package org.maccha.base.exception;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.maccha.base.exception");
		//$JUnit-BEGIN$
		suite.addTestSuite(BaseExceptionTest.class);
		suite.addTestSuite(BizConfirmExceptionTest.class);
		suite.addTestSuite(BizExceptionTest.class);
		suite.addTestSuite(SysConfirmExceptionTest.class);
		suite.addTestSuite(SysExceptionTest.class);
		//$JUnit-END$
		return suite;
	}

}
