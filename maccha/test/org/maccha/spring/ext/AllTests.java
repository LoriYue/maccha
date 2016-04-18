package org.maccha.spring.ext;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.maccha.spring.ext");
		//$JUnit-BEGIN$
		suite.addTestSuite(RegexPropertyMessageResourcesTest.class);
		suite.addTestSuite(BeanNameAutoProxyCreatorTest.class);
		//$JUnit-END$
		return suite;
	}

}
