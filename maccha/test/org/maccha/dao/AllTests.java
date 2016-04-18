package org.maccha.dao;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.maccha.dao");
		//$JUnit-BEGIN$
		suite.addTestSuite(UpdateSetTest.class);
		suite.addTestSuite(SqlFuncTest.class);
		suite.addTestSuite(PageExtQueryParameterTest.class);
		suite.addTestSuite(OrderByTest.class);
		suite.addTestSuite(PageExtParameterTest.class);
		suite.addTestSuite(SelectTest.class);
		suite.addTestSuite(PageTest.class);
		suite.addTestSuite(FilterTest.class);
		suite.addTestSuite(SqlExprTest.class);
		suite.addTest(org.maccha.dao.dialect.AllTests.suite());
		suite.addTest(org.maccha.dao.impl.AllTests.suite());
		suite.addTest(org.maccha.dao.interceptor.AllTests.suite());
		suite.addTest(org.maccha.dao.service.impl.AllTests.suite());
		suite.addTest(org.maccha.dao.type.AllTests.suite());
		suite.addTest(org.maccha.dao.util.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
