package org.maccha.base.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DBExportUtilsTest extends TestCase {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExportOneTable2File() throws IOException {
		DBExportUtils exportUtils = DBExportUtils.getInstance("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@123.59.52.182:1521:orcl", "wes1", "wes1");
		String filePath = "f:\\test2.txt";
		exportUtils.exportOneTable2File("ebo_live", new String [] {"live_id", "live_number", "live_title"}, filePath);
		String encoding="GBK";
        File file=new File(filePath);
        if(file.isFile() && file.exists()){ //判断文件是否存在
            InputStreamReader read = new InputStreamReader(
            new FileInputStream(file),encoding);//考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            StringBuffer buffer = new StringBuffer();
            while((lineTxt = bufferedReader.readLine()) != null){
            	buffer.append(lineTxt);
            }
            read.close();
            assertTrue(buffer.toString().contains("ebo_live"));
        } else {
        	fail("文件不存在");
        }
	}

	@Test
	public void testExportMoreTable2File() throws IOException {
		DBExportUtils exportUtils = DBExportUtils.getInstance("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@123.59.52.182:1521:orcl", "wes1", "wes1");
		String filePath = "f:\\test1.txt";
		exportUtils.exportMoreTable2File(new String [] {"ebo_live", "wes_weixin_user"}, filePath);
		String encoding="GBK";
        File file=new File(filePath);
        if(file.isFile() && file.exists()){ //判断文件是否存在
            InputStreamReader read = new InputStreamReader(
            new FileInputStream(file),encoding);//考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            StringBuffer buffer = new StringBuffer();
            while((lineTxt = bufferedReader.readLine()) != null){
            	buffer.append(lineTxt);
            }
            read.close();
            assertTrue(buffer.toString().contains("ebo_live"));
            assertTrue(buffer.toString().contains("wes_weixin_user"));
        } else {
        	fail("文件不存在");
        }
	}

	@Test
	public void testWriteFile() throws IOException {
		DBExportUtils exportUtils = DBExportUtils.getInstance("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@123.59.52.182:1521:orcl", "wes1", "wes1");
		String filePath = "f:\\test.txt";
		String str = "hello world";
		boolean r = exportUtils.writeFile(str, filePath);
		exportUtils.colseConnection();
		String encoding="GBK";
        File file=new File(filePath);
        if(file.isFile() && file.exists()){ //判断文件是否存在
            InputStreamReader read = new InputStreamReader(
            new FileInputStream(file),encoding);//考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            StringBuffer buffer = new StringBuffer();
            while((lineTxt = bufferedReader.readLine()) != null){
            	buffer.append(lineTxt);
            }
            read.close();
            if(r) {
    			assertEquals(str, buffer.toString());
    		} else {
    			assertFalse(str.equals(buffer.toString()));
    		}
        } else {
        	fail("文件不存在");
        }
		
	}

}
