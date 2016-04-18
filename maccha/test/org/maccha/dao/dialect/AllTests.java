package org.maccha.dao.dialect;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.maccha.dao.dialect");
		//$JUnit-BEGIN$
		suite.addTestSuite(MySQL5DialectExtTest.class);
		suite.addTestSuite(SQLServerDialectExtTest.class);
		suite.addTestSuite(OracleDialectExtTest.class);
		//$JUnit-END$
		return suite;
	}

}
