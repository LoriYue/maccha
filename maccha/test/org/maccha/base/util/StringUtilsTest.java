package org.maccha.base.util;

import junit.framework.TestCase;
import org.junit.Test;
import org.maccha.base.metadata.ObjectMetadata;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

/**
 * Created by Lori Yue on 16-3-4.
 */
public class StringUtilsTest  extends TestCase {
    @Test
    public void testCutStrLenth() throws Exception {
        assertEquals("bcd...", StringUtils.cutStrLenth("abcd efg", 1, 3, "..."));
        assertEquals("bcdnull", StringUtils.cutStrLenth("abcd efg", 1, 3, null));
        assertEquals(null, StringUtils.cutStrLenth(null, 1, 3, "..."));
        assertEquals("", StringUtils.cutStrLenth("abcd efg", 1, -3, "..."));
        assertEquals("abcd efg", StringUtils.cutStrLenth("abcd efg", 1, 13, "..."));
    }

    @Test
    public void testCutStrLenth1() throws Exception {
        assertEquals("abc...", StringUtils.cutStrLenth("abcd efg", 3));
        assertEquals(null, StringUtils.cutStrLenth(null, 3));
        assertEquals("", StringUtils.cutStrLenth("abcd efg", -3));
        assertEquals("abcd efg", StringUtils.cutStrLenth("abcd efg", 13));
    }

    @Test
    public void testCutStrLenth4Bean() throws Exception {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setEntityName("person");
        objectMetadata.setTableName("bizframe_person");
        StringUtils.cutStrLenth4Bean(objectMetadata, "entityName,tableName".split(","), 1, 3, "_");
        assertEquals("ers_", objectMetadata.getEntityName());
        assertEquals("izf_", objectMetadata.getTableName());
    }

    @Test
    public void testCutStrLenth4Bean1() throws Exception {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setEntityName("person");
        objectMetadata.setTableName("bizframe_person");
        StringUtils.cutStrLenth4Bean(objectMetadata, "entityName,tableName".split(","), 3);
        assertEquals("per...", objectMetadata.getEntityName());
        assertEquals("biz...", objectMetadata.getTableName());
    }

    @Test
    public void testCutStrLenth4Beans() throws Exception {
        ObjectMetadata objectMetadata1 = new ObjectMetadata();
        objectMetadata1.setEntityName("person");
        objectMetadata1.setTableName("bizframe_person");
        ObjectMetadata objectMetadata2 = new ObjectMetadata();
        objectMetadata2.setEntityName("hello");
        objectMetadata2.setTableName("world_person");
        List<ObjectMetadata> list = new ArrayList<ObjectMetadata>();
        list.add(objectMetadata1);
        list.add(objectMetadata2);
        StringUtils.cutStrLenth4Beans(list, "entityName,tableName".split(","), 1, 3, "_");
        assertEquals("ers_", list.get(0).getEntityName());
        assertEquals("izf_", list.get(0).getTableName());
        assertEquals("ell_", list.get(1).getEntityName());
        assertEquals("orl_", list.get(1).getTableName());
    }

    @Test
    public void testCutStrLenth4Beans1() throws Exception {
        ObjectMetadata objectMetadata1 = new ObjectMetadata();
        objectMetadata1.setEntityName("person");
        objectMetadata1.setTableName("bizframe_person");
        ObjectMetadata objectMetadata2 = new ObjectMetadata();
        objectMetadata2.setEntityName("hello");
        objectMetadata2.setTableName("world_person");
        List<ObjectMetadata> list = new ArrayList<ObjectMetadata>();
        list.add(objectMetadata1);
        list.add(objectMetadata2);
        StringUtils.cutStrLenth4Beans(list, "entityName,tableName".split(","), 3);
        assertEquals("per...", list.get(0).getEntityName());
        assertEquals("biz...", list.get(0).getTableName());
        assertEquals("hel...", list.get(1).getEntityName());
        assertEquals("wor...", list.get(1).getTableName());
    }

    @Test
    public void testCutStrLenth4Map() throws Exception {
        Map map = new HashMap();
        map.put("entityName", "person");
        map.put("tableName", "bizframe_person");
        StringUtils.cutStrLenth4Map(map, "entityName,tableName".split(","), 1, 3, "_");
        assertEquals("ers_", map.get("entityName"));
        assertEquals("izf_", map.get("tableName"));
    }

    @Test
    public void testCutStrLenth4Map1() throws Exception {
        Map map = new HashMap();
        map.put("entityName", "person");
        map.put("tableName", "bizframe_person");
        StringUtils.cutStrLenth4Map(map, "entityName,tableName".split(","), 3);
        assertEquals("per...", map.get("entityName"));
        assertEquals("biz...", map.get("tableName"));
    }

    @Test
    public void testCutStrLenth4Maps() throws Exception {
        Map map1 = new HashMap();
        map1.put("entityName", "person");
        map1.put("tableName", "bizframe_person");
        Map map2 = new HashMap();
        map2.put("entityName", "hello");
        map2.put("tableName", "world_person");
        List<Map> list = new ArrayList<Map>();
        list.add(map1);
        list.add(map2);
        StringUtils.cutStrLenth4Maps(list, "entityName,tableName".split(","), 1, 3, "_");
        assertEquals("ers_", list.get(0).get("entityName"));
        assertEquals("izf_", list.get(0).get("tableName"));
        assertEquals("ell_", list.get(1).get("entityName"));
        assertEquals("orl_", list.get(1).get("tableName"));
    }

    @Test
    public void testCutStrLenth4Maps1() throws Exception {
        Map map1 = new HashMap();
        map1.put("entityName", "person");
        map1.put("tableName", "bizframe_person");
        Map map2 = new HashMap();
        map2.put("entityName", "hello");
        map2.put("tableName", "world_person");
        List<Map> list = new ArrayList<Map>();
        list.add(map1);
        list.add(map2);
        StringUtils.cutStrLenth4Maps(list, "entityName,tableName".split(","), 3);
        assertEquals("per...", list.get(0).get("entityName"));
        assertEquals("biz...", list.get(0).get("tableName"));
        assertEquals("hel...", list.get(1).get("entityName"));
        assertEquals("wor...", list.get(1).get("tableName"));
    }

    @Test
    public void testFormat() throws Exception {
        String template = "xxx{}yyyy{}zzz{}";
        String [] arr = {"X", "Y" , "Z"};
        assertEquals("xxxXyyyyYzzzZ", StringUtils.format(template, arr));
    }

    @Test
    public void testConversionNullToBlank() throws Exception {
        BigDecimal o1 = null;
        StringBuffer o2 = new StringBuffer("");
        StringBuffer o3 = new StringBuffer("    ");
        StringBuffer o4 = new StringBuffer("undefined");
        StringBuffer o5 = new StringBuffer("sd");
        StringBuffer o6 = new StringBuffer("null");
        assertEquals("", StringUtils.conversionNullToBlank(o1));
        assertEquals("", StringUtils.conversionNullToBlank(o2));
        assertEquals("    ", StringUtils.conversionNullToBlank(o3));
        assertEquals("", StringUtils.conversionNullToBlank(o4));
        assertEquals("sd", StringUtils.conversionNullToBlank(o5));
        assertEquals("", StringUtils.conversionNullToBlank(o6));
    }

    @Test
    public void testAsciiToString() throws Exception {
    	fail("Not yet implemented");
    }

    @Test
    public void testIsNull() throws Exception {
        String str1 = null;
        String str2 = "";
        String str3 = "    ";
        String str4 = "sd";
        String str5 = "null";
        assertEquals(true, StringUtils.isNull(str1));
        assertEquals(true, StringUtils.isNull(str2));
        assertEquals(true, StringUtils.isNull(str3));
        assertEquals(false, StringUtils.isNull(str4));
        assertEquals(true, StringUtils.isNull(str5));
    }

    @Test
    public void testIsNull1() throws Exception {
        BigDecimal o1 = null;
        StringBuffer o2 = new StringBuffer("");
        StringBuffer o3 = new StringBuffer("    ");
        StringBuffer o4 = new StringBuffer("undefined");
        StringBuffer o5 = new StringBuffer("sd");
        StringBuffer o6 = new StringBuffer("null");
        assertEquals(true, StringUtils.isNull(o1));
        assertEquals(true, StringUtils.isNull(o2));
        assertEquals(false, StringUtils.isNull(o3));
        assertEquals(true, StringUtils.isNull(o4));
        assertEquals(false, StringUtils.isNull(o5));
        assertEquals(true, StringUtils.isNull(o6));
    }

    @Test
    public void testIsNotNull() throws Exception {
        String str1 = null;
        String str2 = "";
        String str3 = "    ";
        String str4 = "sd";
        String str5 = "null";
        assertEquals(false, StringUtils.isNotNull(str1));
        assertEquals(false, StringUtils.isNotNull(str2));
        assertEquals(false, StringUtils.isNotNull(str3));
        assertEquals(true, StringUtils.isNotNull(str4));
        assertEquals(false, StringUtils.isNotNull(str5));
    }

    @Test
    public void testIsNotNull1() throws Exception {
        BigDecimal o1 = null;
        StringBuffer o2 = new StringBuffer("");
        StringBuffer o3 = new StringBuffer("    ");
        StringBuffer o4 = new StringBuffer("undefined");
        StringBuffer o5 = new StringBuffer("sd");
        StringBuffer o6 = new StringBuffer("null");
        assertEquals(false, StringUtils.isNotNull(o1));
        assertEquals(false, StringUtils.isNotNull(o2));
        assertEquals(true, StringUtils.isNotNull(o3));
        assertEquals(false, StringUtils.isNotNull(o4));
        assertEquals(true, StringUtils.isNotNull(o5));
        assertEquals(false, StringUtils.isNotNull(o6));
    }

    @Test
    public void testHasLength() throws Exception {
        String str1 = null;
        String str2 = "";
        String str3 = "    ";
        String str4 = "undefined";
        String str5 = "sd";
        String str6 = "null";
        assertEquals(false, StringUtils.hasLength(str1));
        assertEquals(false, StringUtils.hasLength(str2));
        assertEquals(true, StringUtils.hasLength(str3));
        assertEquals(false, StringUtils.hasLength(str4));
        assertEquals(true, StringUtils.hasLength(str5));
        assertEquals(false, StringUtils.hasLength(str6));
    }

    @Test
    public void testHasLength1() throws Exception {
        BigDecimal o1 = null;
        StringBuffer o2 = new StringBuffer("");
        StringBuffer o3 = new StringBuffer("    ");
        StringBuffer o4 = new StringBuffer("undefined");
        StringBuffer o5 = new StringBuffer("sd");
        StringBuffer o6 = new StringBuffer("null");
        assertEquals(false, StringUtils.hasLength(o1));
        assertEquals(false, StringUtils.hasLength(o2));
        assertEquals(true, StringUtils.hasLength(o3));
        assertEquals(false, StringUtils.hasLength(o4));
        assertEquals(true, StringUtils.hasLength(o5));
        assertEquals(false, StringUtils.hasLength(o6));
    }

    @Test
    public void testGetPingYin() throws Exception {
        assertEquals("yulong", StringUtils.getPingYin("余龙"));
        assertEquals("yulonglori", StringUtils.getPingYin("余龙lori"));
        assertEquals("@#123yulonglori", StringUtils.getPingYin("@#123余龙lori"));
    }

    @Test
    public void testGetSimplePinYin() throws Exception {
        assertEquals("yl", StringUtils.getSimplePinYin("余龙"));
        assertEquals("@#123yllori", StringUtils.getSimplePinYin("@#123余龙lori"));
    }

    @Test
    public void testHasText() throws Exception {
        String str1 = "      ";
        String str2 = "   23h3&&*   ";
        String str3 = "23h3&&*";
        String str4 = "";
        String str5 = null;
        assertEquals(false, StringUtils.hasText(str1));
        assertEquals(true, StringUtils.hasText(str2));
        assertEquals(true, StringUtils.hasText(str3));
        assertEquals(false, StringUtils.hasText(str4));
        assertEquals(false, StringUtils.hasText(str5));
    }

    @Test
    public void testCountSubString() throws Exception {
        assertEquals(3, StringUtils.countSubString("aa aaa a caa", "aa"));
    }

    @Test
    public void testReplace() throws Exception {
        assertEquals("AA AAa a cAA", StringUtils.replace("aa aaa a caa", "aa", "AA"));
    }

    @Test
    public void testDelete() throws Exception {
        assertEquals(" a a c", StringUtils.delete("aa aaa a caa", "aa"));
    }

    @Test
    public void testDeleteAny() throws Exception {
        assertEquals("   ", StringUtils.deleteAny("aa aaa a caa", "ac"));
    }

    @Test
    public void testSplit() throws Exception {
        String s = "123 44, 2222sss, sss 7878s ,    ";
        String [] ss1 = {"123 44", "2222sss", "sss 7878s"};
        String [] ss2 = StringUtils.split(s, ",", true, true);
        assertEquals(ss1.length, ss2.length);
        assertEquals(ss1[0], ss2[0]);
        assertEquals(ss1[1], ss2[1]);
        assertEquals(ss1[2], ss2[2]);
    }

    @Test
    public void testSplit1() throws Exception {
        String s = "123 44, 2222sss, sss 7878s ,    ";
        String [] ss1 = {"123 44", " 2222sss", " sss 7878s ", "    "};
        String [] ss2 = StringUtils.split(s, ",");
        assertEquals(ss1.length, ss2.length);
        assertEquals(ss1[0], ss2[0]);
        assertEquals(ss1[1], ss2[1]);
        assertEquals(ss1[2], ss2[2]);
        assertEquals(ss1[3], ss2[3]);
    }

    @Test
    public void testSplitToIntArray() throws Exception {
        String s = "123,56,78";
        int [] i1 = {123, 56, 78};
        int [] i2 = StringUtils.splitToIntArray(s, ",");
        assertEquals(i1.length, i2.length);
        assertEquals(i1[0], i2[0]);
        assertEquals(i1[1], i2[1]);
        assertEquals(i1[2], i2[2]);
    }

    @Test
    public void testSplitToSet() throws Exception {
        String s = "123,56,78";
        Set set1 = new TreeSet();
        set1.add("123");
        set1.add("56");
        set1.add("78");
        Set set2 = StringUtils.splitToSet(s , ",");
        assertEquals(set1.size(), set2.size());
    }

    @Test
    public void testCsvStringToArray() throws Exception {
        String s = "123,xx,7aa8";
        String [] ss1 = {"123", "xx", "7aa8"};
        String [] ss2 = StringUtils.csvStringToArray(s);
        assertEquals(ss1.length, ss2.length);
        assertEquals(ss1[0], ss2[0]);
        assertEquals(ss1[1], ss2[1]);
        assertEquals(ss1[2], ss2[2]);
    }

    @Test
    public void testCsvStringToSet() throws Exception {
        String s = "123,56,78";
        Set set1 = new TreeSet();
        set1.add("123");
        set1.add("56");
        set1.add("78");
        Set set2 = StringUtils.csvStringToSet(s);
        assertEquals(set1.size(), set2.size());
    }

    @Test
    public void testJoinCsvString() throws Exception {
        String [] ss1 = {"123", "xx", "7aa8"};
        assertEquals("123,xx,7aa8", StringUtils.joinCsvString(ss1));
    }

    @Test
    public void testJoinCsvString1() throws Exception {
        Collection c = new ArrayList();
        c.add(123);
        c.add("ee");
        c.add("1&&");
        assertEquals("123,ee,1&&", StringUtils.joinCsvString(c));
    }

    @Test
    public void testJoinCsvString2() throws Exception {
        Map args = new HashMap();
        args.put("name", "lori");
        args.put("pw", "123");
        assertEquals("name=lori,pw=123", StringUtils.joinCsvString(args));
    }

    @Test
    public void testJoin() throws Exception {
        String [] s1 = {"2016" , "03", "04"};
        assertEquals("2016-03-04", StringUtils.join(s1, "-"));
    }

    @Test
    public void testJoin1() throws Exception {
        Collection c = new ArrayList();
        c.add(123);
        c.add("ee");
        c.add("1&&");
        assertEquals("123|ee|1&&", StringUtils.join(c, "|"));
    }

    @Test
    public void testJoin2() throws Exception {
        Map args = new HashMap();
        args.put("name", "lori");
        args.put("pw", "123");
        assertEquals("name=lori&pw=123", StringUtils.join(args, "&"));
    }

    @Test
    public void testJoinTag() throws Exception {
        List list = new ArrayList();
        list.add("aaa");
        list.add("123");
        list.add(555);
        assertEquals("aaaX123X555", StringUtils.joinTag(list, "X"));
    }

    @Test
    public void testSplitToMap() throws Exception {
        String s = "a=1;b=2;c=3";
        Map m1 = new HashMap();
        m1.put("a", "1");
        m1.put("b", "2");
        m1.put("c", "3");
        Map m2 = StringUtils.splitToMap(s, ";");
        assertEquals(m1.get("a"), m2.get("a"));
        assertEquals(m1.get("b"), m2.get("b"));
        assertEquals(m1.get("c"), m2.get("c"));
    }

    @Test
    public void testAddToArray() throws Exception {
        String [] ss1 = {"2016" , "03", "04"};
        String [] ss2 = StringUtils.addToArray(ss1, "10");
        assertEquals(ss1.length, ss2.length - 1);
        assertEquals(ss1[0], ss2[0]);
        assertEquals(ss1[1], ss2[1]);
        assertEquals(ss1[2], ss2[2]);
        assertEquals("10", ss2[3]);
    }

    @Test
    public void testUnqualify() throws Exception {
        assertEquals("Yue", StringUtils.unqualify("Lori.Yue"));
    }

    @Test
    public void testUnqualify1() throws Exception {
        assertEquals("Yue", StringUtils.unqualify("Lori.Yue", '.'));
    }

    @Test
    public void testCapitalizeFirst() throws Exception {
        assertEquals("Lori", StringUtils.capitalizeFirst("lori"));
        assertEquals("Lori", StringUtils.capitalizeFirst("Lori"));
        assertEquals("123", StringUtils.capitalizeFirst("123"));
        assertEquals("呵呵", StringUtils.capitalizeFirst("呵呵"));
    }

    @Test
    public void testUncapitalizeFirst() throws Exception {
        assertEquals("lori", StringUtils.uncapitalizeFirst("lori"));
        assertEquals("lori", StringUtils.uncapitalizeFirst("Lori"));
    }

    @Test
    public void testCleanPath() throws Exception {
        assertEquals("E:/myeclipse8.5/techframe4.0/WebRoot/WEB-INF/lib", StringUtils.cleanPath("E:\\myeclipse8.5\\techframe4.0\\WebRoot\\WEB-INF\\lib"));
        assertEquals("myeclipse8.5/techframe4.0/WebRoot/WEB-INF/lib", StringUtils.cleanPath("./myeclipse8.5/techframe4.0/WebRoot/WEB-INF/lib"));
        assertEquals("myeclipse8.5/techframe4.0/WebRoot/WEB-INF/lib", StringUtils.cleanPath("../myeclipse8.5/techframe4.0/WebRoot/WEB-INF/lib"));
    }

    @Test
    public void testGetStrByBlob() throws Exception {
    	fail("Not yet implemented");
        /*String url = "jdbc:oracle:thin:@123.59.52.182:1521:orcl";
        Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
        Connection conn = DriverManager.getConnection(url, "wes1", "wes1");
        Statement s = conn.createStatement();
        ResultSet rs = s.executeQuery("SELECT neit_content FROM wcms_news_item");
        while (rs.next()) {
            Blob blob = rs.getBlob(1);
            System.out.println(
                    StringUtils.getStrByBlob(blob)
            );
        }
        conn.close();*/
    }

    @Test
    public void testGetStrByClob() throws Exception {
    	fail("Not yet implemented");
        /*String url = "jdbc:oracle:thin:@123.59.52.182:1521:orcl";
        Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
        Connection conn = DriverManager.getConnection(url, "wes1", "wes1");
        Statement s = conn.createStatement();
        ResultSet rs = s.executeQuery("SELECT neit_content FROM wcms_news_item");
        while (rs.next()) {
            Clob clob = rs.getClob(1);
            System.out.println(
                    StringUtils.getStrByClob(clob)
            );
        }
        conn.close();*/
    }

    @Test
    public void testCompress() throws Exception {
    	fail("Not yet implemented");
    }

    @Test
    public void testDeCompress() throws Exception {
    	fail("Not yet implemented");
    }

    @Test
    public void testEncodedBase64() throws Exception {
    	fail("Not yet implemented");
    }

    @Test
    public void testDecodedBase64() throws Exception {
    	fail("Not yet implemented");
    }

    @Test
    public void testApp2DbEncode() throws Exception {
    	fail("Not yet implemented");
    }

    @Test
    public void testDb2AppEncode() throws Exception {
    	fail("Not yet implemented");
    }

    @Test
    public void testUnicodeToGB() throws Exception {
    	fail("Not yet implemented");
    }

    @Test
    public void testEscape() throws Exception {
    	fail("Not yet implemented");
    }

    @Test
    public void testUnescape() throws Exception {
    	fail("Not yet implemented");
    }

    @Test
    public void testGBToUnicode() throws Exception {
    	fail("Not yet implemented");
    }

    @Test
    public void testHtmlEncode() throws Exception {
    	fail("Not yet implemented");
    }

    @Test
    public void testDeleteHtmlTag() throws Exception {
    	fail("Not yet implemented");
    }

    @Test
    public void testJoinTag1() throws Exception {
        assertEquals("<a><a><a>", StringUtils.joinTag(3, "<a>"));
    }

    @Test
    public void testGetURLParameter() throws Exception {
        String url = "http://maccha.org";
        String parameterName1 = "uname";
        String parameterValue1 = "lori";
        String str =  StringUtils.addURLParameter(url, parameterName1, parameterValue1);
        assertEquals("lori", StringUtils.getURLParameter(str, "uname"));
        String parameterName2 = "pw";
        String parameterValue2 = "1234";
        str = StringUtils.addURLParameter(str, parameterName2, parameterValue2);
        assertEquals("lori", StringUtils.getURLParameter(str, "uname"));
        assertEquals("1234", StringUtils.getURLParameter(str, "pw"));
    }

    @Test
    public void testAddURLParameter() throws Exception {
        String url = "http://maccha.org";
        String parameterName1 = "uname";
        String parameterValue1 = "lori";
        String str =  StringUtils.addURLParameter(url, parameterName1, parameterValue1);
        assertEquals(url + "?" + parameterName1 + "=" + parameterValue1, str);
        String parameterName2 = "pw";
        String parameterValue2 = "1234";
        assertEquals(str + "&" + parameterName2 + "=" + parameterValue2,  StringUtils.addURLParameter(str, parameterName2, parameterValue2));
    }

    @Test
    public void testUtf8URLencode() throws Exception {
    	fail("Not yet implemented");
    }

    @Test
    public void testDecodeURI2UTF8() throws Exception {
    	fail("Not yet implemented");
    }

    @Test
    public void testUtf8URLdecode() throws Exception {
    	fail("Not yet implemented");
    }

    @Test
    public void test_isUtf8Url() throws Exception {
    	fail("Not yet implemented");
    }

    @Test
    public void testGenerate8Uuid() throws Exception {
        assertEquals(8, StringUtils.generate8Uuid().length());
    }
}
