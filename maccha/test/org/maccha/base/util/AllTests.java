package org.maccha.base.util;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.maccha.base.util");
		//$JUnit-BEGIN$
		suite.addTest(org.maccha.base.util.convertor.AllTests.suite());
		suite.addTestSuite(FileUtilsTest.class);
		suite.addTestSuite(RTFUtilsTest.class);
		suite.addTestSuite(DateUtilsTest.class);
		suite.addTestSuite(ClassUtilsTest.class);
		suite.addTestSuite(Base64UtilsTest.class);
		suite.addTestSuite(DateTimeUtilTest.class);
		suite.addTestSuite(DBExportUtilsTest.class);
		suite.addTestSuite(PropertyFileUtilsTest.class);
		suite.addTestSuite(NumberFormatUtilsTest.class);
		suite.addTestSuite(ObjectUtilsTest.class);
		suite.addTestSuite(SecurityUtilsTest.class);
		suite.addTestSuite(TypeConvertorUtilsTest.class);
		suite.addTestSuite(ParameterRecordTest.class);
		suite.addTestSuite(XmlUtilsTest.class);
		suite.addTestSuite(ArrayUtilsTest.class);
		suite.addTestSuite(ConfigUtilsTest.class);
		suite.addTestSuite(ExcelUtilsTest.class);
		suite.addTestSuite(JoSQLUtilTest.class);
		suite.addTestSuite(JarUtilsTest.class);
		suite.addTestSuite(ZipUtilTest.class);
		suite.addTestSuite(MapUtilsTest.class);
		suite.addTestSuite(Xml2MapUtilsTest.class);
		suite.addTestSuite(ErrorCheckerTest.class);
		suite.addTestSuite(GridHeaderTest.class);
		suite.addTestSuite(ProxyUtilsTest.class);
		suite.addTestSuite(ExistMapTest.class);
		suite.addTestSuite(RMBUtilsTest.class);
		suite.addTestSuite(StringUtilsTest.class);
		suite.addTestSuite(EncoderUtilsTest.class);
		suite.addTestSuite(MathUtilsTest.class);
		suite.addTestSuite(IoUtilsTest.class);
		suite.addTestSuite(RandomGUIDUtilTest.class);
		suite.addTestSuite(CollectionsUtilsTest.class);
		suite.addTestSuite(FileSendUtilsTest.class);
		//$JUnit-END$
		return suite;
	}

}
