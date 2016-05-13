package org.maccha.base.metadata;

import java.util.List;

import junit.framework.TestCase;

import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.maccha.spring.SpringManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ObjectMetadataUtilTest extends TestCase{
	private ApplicationContext ctx;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		SpringManager springManager = new SpringManager();
    	String[] paths = { "config/oracle_config/spring_config/applicationContext.xml"};
    	ctx = new FileSystemXmlApplicationContext(paths);
    	springManager.setApplicationContext(ctx);
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void testGetConfiguration() {
		Configuration conf = ObjectMetadataUtil.getConfiguration();
		assertTrue(conf != null);
	}

	@Test
	public void testGetEntityMetadata() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEntityMetadataString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEntityPropertyMetadata() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEntityPropertyMetadataList() {
		fail("Not yet implemented");
	}

}
