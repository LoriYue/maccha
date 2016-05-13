package org.maccha.dao.impl;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.maccha.dao.impl");
		//$JUnit-BEGIN$
		suite.addTestSuite(CommonExprProcessTest.class);
		suite.addTestSuite(FieldFilterTest.class);
		suite.addTestSuite(IsWhereExprProcessTest.class);
		suite.addTestSuite(INExprProcessTest.class);
		suite.addTestSuite(GroupFilterTest.class);
		suite.addTestSuite(ISNotNULLExprProcessTest.class);
		suite.addTestSuite(DaoServiceImplTest.class);
		suite.addTestSuite(IsBetweenExprProcessTest.class);
		suite.addTestSuite(ISNULLExprProcessTest.class);
		//$JUnit-END$
		return suite;
	}

}
