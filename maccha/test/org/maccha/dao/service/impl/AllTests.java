package org.maccha.dao.service.impl;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.maccha.dao.service.impl");
		//$JUnit-BEGIN$
		suite.addTestSuite(BaseBizServiceImplTest.class);
		//$JUnit-END$
		return suite;
	}

}
