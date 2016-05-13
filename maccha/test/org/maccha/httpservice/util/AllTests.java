package org.maccha.httpservice.util;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.maccha.httpservice.util");
		//$JUnit-BEGIN$
		suite.addTestSuite(XmlDataMessageParserImplTest.class);
		suite.addTestSuite(EntityUtilsTest.class);
		suite.addTestSuite(JsonDataMessageParserImplTest.class);
		suite.addTestSuite(Xml2DataMessageUtiltyTest.class);
		suite.addTestSuite(JsonUtilsTest.class);
		//$JUnit-END$
		return suite;
	}

}
