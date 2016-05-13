package org.maccha.base.metadata;


import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.maccha.base.metadata");
		//$JUnit-BEGIN$
		suite.addTestSuite(ObjectMetadataUtilTest.class);
		suite.addTestSuite(ObjectMetadataTest.class);
		suite.addTestSuite(PropertyMetadataTest.class);
		//$JUnit-END$
		return suite;
	}

}
