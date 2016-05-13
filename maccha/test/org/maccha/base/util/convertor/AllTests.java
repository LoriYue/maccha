package org.maccha.base.util.convertor;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.maccha.base.util.convertor");
		//$JUnit-BEGIN$
		suite.addTestSuite(ConvertorTypesUtilityTest.class);
		//$JUnit-END$
		return suite;
	}

}
