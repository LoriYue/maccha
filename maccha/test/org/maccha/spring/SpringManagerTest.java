package org.maccha.spring;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * Created by Lori Yue on 16-3-4.
 */
public class SpringManagerTest extends TestCase{
	private SpringManager springManager;
	private ApplicationContext ctx;
    @Before
    public void setUp() throws Exception {
    	springManager = new SpringManager();
    	String[] paths = { "config/oracle_config/spring_config/applicationContext.xml"};
    	ctx = new FileSystemXmlApplicationContext(paths);
    }

    @Test
    public void testSetApplicationContext() throws Exception {
    	springManager.setApplicationContext(ctx);
    	assertEquals(ctx, springManager.getApplicationContext());
    }

    @Test
    public void testGetApplicationContext() throws Exception {
    	springManager.setApplicationContext(ctx);
    	assertEquals(ctx, springManager.getApplicationContext());
    }

    @Test
    public void testGetComponent() throws Exception {
    	springManager.setApplicationContext(ctx);
    	DruidDataSource dataSource = SpringManager.getComponent("dataSource");
    	assertEquals(1, dataSource.getMinIdle());
    }
}
