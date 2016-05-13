package org.maccha.base.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.log4j.Logger;

import java.io.*;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by Lori Yue on 16-3-4.
 */
public class StringUtils {
    public static Logger logger = Logger.getLogger(StringUtils.class.getName());
    private static final String CHANGE_PATH = "/";
    private static final String WIN_CHANGE_PATH = "\\";
    private static final String TOP_PATH = "..";
    private static final String CURRENT_PATH = ".";
    public static final String DOT = ".";
    public static final String SLASH = "/";
    public static final String EMPTY = "";
    public static String[] chars = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

    /**
     * 截取fullStr的一部分并与标志字符串tag组成新的字符串
     * @param fullStr 被截取的字符串，若为null则返回null
     * @param start 开始截取的位置
     * @param lenth 截取长度，若小于0则返回""，若大于fullStr的长度则返回fullStr
     * @param tag  标志字符串
     * @return
     */
    public static String cutStrLenth(String fullStr, int start, int lenth, String tag) {
        if (fullStr == null)
            return fullStr;
        if (lenth <= 0)
            return "";
        String cutStr = "";
        if (fullStr.length() > lenth) {
            cutStr = fullStr.substring(start, start + lenth);
            cutStr = cutStr + tag;
            return cutStr;
        }
        return fullStr;
    }
    /**
     * 截取fullStr的长度为lenth的一部分并与标志字符串"..."组成新的字符串
     * @param fullStr 被截取的字符串，若为null则返回null
     * @param lenth 截取长度，若小于0则返回""，若大于fullStr的长度则返回fullStr
     * @return
     */
    public static String cutStrLenth(String fullStr, int lenth) {
        if (fullStr == null)
            return fullStr;
        if (lenth <= 0)
            return "";
        String cutStr = "";
        if (fullStr.length() > lenth) {
            cutStr = fullStr.substring(0, lenth);
            cutStr = cutStr + "...";
            return cutStr;
        }
        return fullStr;
    }
    /**
     * 截取bean的某些成员变量的一部分并与标志字符串tag组成新的字符串
     * @param bean bean
     * @param propertyNames  要截取的成员变量
     * @param start 开始截取的位置
     * @param lenth 截取长度，若小于0则返回""，若大于fullStr的长度则返回fullStr
     * @param tag  标志字符串
     */
    public static void cutStrLenth4Bean(Object bean, String[] propertyNames, int start, int lenth, String tag) {
        for (int i = 0; (propertyNames != null) && (i < propertyNames.length); i++) {
            String fullStr = null;
            try {
                Object vobj = ClassUtils.get(bean, propertyNames[i]);
                if ((vobj instanceof Date))
                    fullStr = DateUtils.format((Date)vobj, "yyyy-MM-dd");
                else
                    fullStr = vobj + "";
            } catch (Exception e) {
                e.printStackTrace();
            }
            String cutStr = cutStrLenth(fullStr, start, lenth, tag);
            try {
                ClassUtils.set(bean, propertyNames[i], String.class, cutStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            fullStr = null;
            cutStr = null;
        }
    }
    /**
     * 截取bean的某些成员变量的一部分并与标志字符串"..."组成新的字符串
     * @param bean bean
     * @param propertyNames  要截取的成员变量
     * @param lenth 截取长度，若小于0则返回""，若大于fullStr的长度则返回fullStr
     */
    public static void cutStrLenth4Bean(Object bean, String[] propertyNames, int lenth) {
        cutStrLenth4Bean(bean, propertyNames, 0, lenth, "...");
    }
    /**
     * 截取list中的bean的某些成员变量的一部分并与标志字符串tag组成新的字符串
     * @param beanList beanList
     * @param propertyNames 要截取的成员变量
     * @param start 开始截取的位置
     * @param lenth 截取长度，若小于0则返回""，若大于fullStr的长度则返回fullStr
     * @param tag 标志字符串
     */
    public static void cutStrLenth4Beans(List beanList, String[] propertyNames, int start, int lenth, String tag) {
        for (int i = 0; (beanList != null) && (i < beanList.size()); i++)
            cutStrLenth4Bean(beanList.get(i), propertyNames, start, lenth, tag);
    }
    /**
     * 截取list中的bean的某些成员变量的一部分并与标志字符串"..."组成新的字符串
     * @param beanList beanList
     * @param propertyNames 要截取的成员变量
     * @param lenth 截取长度，若小于0则返回""，若大于fullStr的长度则返回fullStr
     */
    public static void cutStrLenth4Beans(List beanList, String[] propertyNames, int lenth) {
        cutStrLenth4Beans(beanList, propertyNames, 0, lenth, "...");
    }
    /**
     * 截取map中的某些value的一部分并与标志字符串tag组成新的字符串
     * @param map map
     * @param keyNames  要截取的value对应的key
     * @param start 开始截取的位置
     * @param lenth 截取长度，若小于0则返回""，若大于fullStr的长度则返回fullStr
     * @param tag  标志字符串
     */
    public static void cutStrLenth4Map(Map map, String[] keyNames, int start, int lenth, String tag) {
        for (int i = 0; (keyNames != null) && (i < keyNames.length); i++) {
            String fullStr = null;
            try {
                Object vobj = map.get(keyNames[i]);
                if ((vobj instanceof Date))
                    fullStr = DateUtils.format((Date)vobj, "yyyy-MM-dd");
                else
                    fullStr = vobj + "";
            } catch (Exception e) {
                e.printStackTrace();
            }
            String cutStr = cutStrLenth(fullStr, start, lenth, tag);
            try {
                map.put(keyNames[i], cutStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
            fullStr = null;
            cutStr = null;
        }
    }
    /**
     * 截取map中的某些value的一部分并与标志字符串"..."组成新的字符串
     * @param map map
     * @param keyNames  要截取的value对应的key
     * @param lenth 截取长度，若小于0则返回""，若大于fullStr的长度则返回fullStr
     */
    public static void cutStrLenth4Map(Map map, String[] keyNames, int lenth) {
        cutStrLenth4Map(map, keyNames, 0, lenth, "...");
    }
    /**
     * 截取list中的map中的的某些value的一部分并与标志字符串tag组成新的字符串
     * @param mapList mapList
     * @param keyNames 要截取的value对应的key
     * @param start 开始截取的位置
     * @param lenth 截取长度，若小于0则返回""，若大于fullStr的长度则返回fullStr
     * @param tag 标志字符串
     */
    public static void cutStrLenth4Maps(List mapList, String[] keyNames, int start, int lenth, String tag) {
        for (int i = 0; (mapList != null) && (i < mapList.size()); i++)
            cutStrLenth4Map((Map)mapList.get(i), keyNames, start, lenth, tag);
    }
    /**
     * 截取list中的map中的的某些value的一部分并与标志字符串"..."组成新的字符串
     * @param mapList mapList
     * @param keyNames 要截取的value对应的key
     * @param lenth 截取长度，若小于0则返回""，若大于fullStr的长度则返回fullStr
     */
    public static void cutStrLenth4Maps(List mapList, String[] keyNames, int lenth) {
        cutStrLenth4Maps(mapList, keyNames, 0, lenth, "...");
    }
    /**
     * 格式化文本
     * @param template 文本模板，被替换的部分用 {} 表示
     * @param values 参数值
     * @return 格式化后的文本
     */
    public static String format(String template, Object... values) {
        return String.format(template.replace("{}", "%s"), values);
    }
    /**
     * 若对象转换为字符后为空则返回""，否则返回对象转换为字符
     * @param obj
     * @return null或者"null"或者""或者"undefined"皆返回true
     */
    public static String conversionNullToBlank(Object obj) {
        if (isNotNull(obj)) {
            return obj.toString();
        }
        return "";
    }
    /**
	 * 将Ascii转换为String
	 * 
	 * @param str
	 * @return
	 */
    public static String asciiToString(String str) {
        if (isNotNull(str)) {
            return String.valueOf((char)(Integer.parseInt(str) + 64));
        }
        return String.valueOf('A');
    }
    /**
     * 判读字符串是否为空
     * @param value
     * @return null或者"null"或者""或者"     "皆返回true
     */
    public static boolean isNull(String value) {
        return !hasText(value);
    }
    /**
     * 判断对象转换为字符后是否为空
     * @param value
     * @return null或者"null"或者""或者"undefined"皆返回true
     */
    public static boolean isNull(Object value) {
        return !hasLength(value);
    }
    /**
     * 判读字符串是否不为空
     * @param value
     * @return null或者"null"或者""或者"     "皆返回false
     */
    public static boolean isNotNull(String value) {
        return hasText(value);
    }
    /**
     * 判断对象转换为字符后是否不为空
     * @param value
     * @return null或者"null"或者""或者"undefined"皆返回false
     */
    public static boolean isNotNull(Object value) {
        return hasLength(value);
    }
    /**
     * 判断字符中是否含有字符
     * @param str
     * @return null或者"null"或者""或者"undefined"皆返回false
     */
    public static boolean hasLength(String str) {
        return (str != null) && (str.length() > 0) && (!"null".equals(str.toLowerCase())) && (!"undefined".equals(str.toLowerCase()));
    }
    /**
     * 判断对象转换为字符后是否含有字符
     * @param obj
     * @return null或者"null"或者""或者"undefined"皆返回false
     */
    public static boolean hasLength(Object obj) {
        return (obj != null) && ((obj + "").length() > 0) && (!"null".equals((obj + "").toLowerCase())) && (!"undefined".equals((obj + "").toLowerCase()));
    }
    /**
     * 判断字符中是否含有字符
     * @param str
     * @return null或者"null"或者""或者"     "皆返回false
     */
    public static boolean hasText(String str) {
        int strLen;
        if ((str == null) || ((strLen = str.length()) == 0) || ("null".equals(str.toLowerCase()))) {
            return false;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }
    /**
     * 生成汉语小写全拼字母
     * @param src
     * @return
     */
    public static String getPingYin(String src) {
        char[] t1 = null;
        t1 = src.toCharArray();
        String[] t2 = new String[t1.length];
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1.length;
        try {
            for (int i = 0; i < t0; i++)
            {
                if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
                    t4 = t4 + t2[0]; } else {
                    t4 = t4 + Character.toString(t1[i]);
                }
            }
            return t4;
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            e1.printStackTrace();
        }
        return t4;
    }
    /**
     * 生成汉语小写首字母
     * @param str
     * @return
     */
    public static String getSimplePinYin(String str) {
        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null)
                convert = convert + pinyinArray[0].charAt(0);
            else {
                convert = convert + word;
            }
        }
        return convert;
    }
    /**
     * 计算字符串中包含子串的个数
     * @param s 目标字符串
     * @param sub 子串
     * @return
     */
    public static int countSubString(String s, String sub) {
        if ((s == null) || (sub == null) || ("".equals(sub))) {
            return 0;
        }
        int count = 0; int pos = 0; int idx = 0;
        while ((idx = s.indexOf(sub, pos)) != -1) {
            count++;
            pos = idx + sub.length();
        }
        return count;
    }
    /**
     * 替换目标字符串中所有子串
     * @param inString 目标字符串
     * @param oldPattern 旧子串
     * @param newPattern 新子串
     * @return 替换后的结果字符串
     */
    public static String replace(String inString, String oldPattern, String newPattern) {
        if (inString == null) {
            return null;
        }
        if ((oldPattern == null) || (newPattern == null)) {
            return inString;
        }
        StringBuffer sbuf = new StringBuffer();
        int pos = 0;
        int index = inString.indexOf(oldPattern);
        int patLen = oldPattern.length();
        while (index >= 0) {
            sbuf.append(inString.substring(pos, index));
            sbuf.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }
        sbuf.append(inString.substring(pos));
        return sbuf.toString();
    }
    /**
     * 删除目标字符串中的所有子串
     * @param inString 目标字符串
     * @param pattern 子串
     * @return 删除后的结果字符串
     */
    public static String delete(String inString, String pattern) {
        return replace(inString, pattern, "");
    }
    /**
     * 删除目标字符串中所有子串中包含的字符
     * @param inString 目标字符串
     * @param chars 子串
     * @return 删除后的结果字符串
     */
    public static String deleteAny(String inString, String chars) {
        if ((inString == null) || (chars == null)) {
            return inString;
        }
        StringBuffer out = new StringBuffer();
        for (int i = 0; i < inString.length(); i++) {
            char c = inString.charAt(i);
            if (chars.indexOf(c) == -1) {
                out.append(c);
            }
        }
        return out.toString();
    }
    /**
     * 拆分字符串为数组
     * @param s 字符串
     * @param delimiters 分隔符
     * @param trimTokens 是否对拆分后的字串去空格处理
     * @param ignoreEmptyTokens 是否忽略拆分处理后长度为空的字符串
     * @return 拆分后产生的字符串数组
     */
    public static String[] split(String s, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
        StringTokenizer st = new StringTokenizer(s, delimiters);
        List tokens = new ArrayList();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if ((!ignoreEmptyTokens) || (token.length() != 0)) {
                tokens.add(token);
            }
        }
        return (String[])tokens.toArray(new String[tokens.size()]);
    }
    /**
     * 拆分字符串为数组
     * @param s 字符串
     * @param delim 分隔符
     * @return 拆分后产生的字符串数组
     */
    public static String[] split(String s, String delim) {
        if (s == null) {
            return new String[0];
        }
        if (delim == null) {
            return new String[] { s };
        }
        List l = new LinkedList();
        int pos = 0;
        int delPos = 0;
        while ((delPos = s.indexOf(delim, pos)) != -1) {
            l.add(s.substring(pos, delPos));
            pos = delPos + delim.length();
        }
        if (pos <= s.length())
        {
            l.add(s.substring(pos));
        }
        return (String[])l.toArray(new String[l.size()]);
    }
    /**
     * 将字符串拆分为整数数组
     * @param s 字符串
     * @param delim 分隔符
     * @return
     */
    public static int[] splitToIntArray(String s, String delim) {
        String[] stringValueArray = split(s, delim);
        int[] intValueArray = new int[stringValueArray.length];
        for (int i = 0; i < intValueArray.length; i++) {
            intValueArray[i] = Integer.parseInt(stringValueArray[i]);
        }
        return intValueArray;
    }
    /**
     * 将字符串拆分为TreeSet集合
     * @param s 字符串
     * @param delim 分隔符
     * @return
     */
    public static Set splitToSet(String s, String delim) {
        Set set = new TreeSet();
        String[] tokens = split(s, delim);
        for (int i = 0; i < tokens.length; i++) {
            set.add(tokens[i]);
        }
        return set;
    }
    /**
     * 将csv字符串拆分为字符串数组
     * @param s csv字符串
     * @return
     */
    public static String[] csvStringToArray(String s) {
        return split(s, ",");
    }
    /**
     * 将csv字符串拆分为TreeSet集合
     * @param s 字符串
     * @return
     */
    public static Set csvStringToSet(String s) {
        Set set = new TreeSet();
        String[] tokens = csvStringToArray(s);
        for (int i = 0; i < tokens.length; i++) {
            set.add(tokens[i]);
        }
        return set;
    }
    /**
     * 将数组中的元素转换成字符串并拼接成csv字符串
     * @param arr
     * @return
     */
    public static String joinCsvString(Object[] arr) {
        return join(arr, ",");
    }
    /**
     * 将Collection中的元素转换成字符串并拼接成csv字符串
     * @param c
     * @return
     */
    public static String joinCsvString(Collection c) {
        return join(c, ",");
    }
    /**
     * 将map中的键值对转换为key=value形式的字符串并拼接成csv字符串
     * @param map
     * @return
     */
    public static String joinCsvString(Map map) {
        return join(map, ",");
    }
    /**
     * 将数组中的元素转换为字符串并拼接成一个新的字符串
     * @param arr 数组
     * @param delim 分隔符
     * @return
     */
    public static String join(Object[] arr, String delim) {
        if (arr == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                sb.append(delim);
            }
            sb.append(arr[i]);
        }
        return sb.toString();
    }
    /**
     * 将Collection中的元素转换为字符串并拼接成一个新的字符串
     * @param c Collection
     * @param delim 分隔符
     * @return
     */
    public static String join(Collection c, String delim) {
        if (c == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        Iterator it = c.iterator();
        int i = 0;
        Object objValue = null;
        while (it.hasNext()) {
            if (i++ > 0) {
                sb.append(delim);
            }
            objValue = it.next();
            sb.append(objValue == null ? " " : objValue);
        }
        return sb.toString();
    }
    /**
     * 将map中的键值对转换为key=value形式的字符串并拼接成一个新的字符串
     * @param map map
     * @param delim 分隔符
     * @return
     */
    public static String join(Map map, String delim) {
        if (map == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        Iterator itrKeys = map.keySet().iterator();
        int i = 0;
        String strKey = "";
        String strValue = "";
        while (itrKeys.hasNext()) {
            strKey = (String)itrKeys.next();
            strValue = (String)map.get(strKey);
            if (i++ > 0) {
                sb.append(delim);
            }
            sb.append(strKey + "=" + strValue);
        }
        return sb.toString();
    }
    /**
     * 将list中的元素转换为字符串并拼接成一个新的字符串
     * @param list list
     * @param tag 分隔符
     * @return
     */
    public static String joinTag(List list, String tag) {
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            buff.append(list.get(i) + tag);
        }
        String ids = buff.toString();
        if ((ids != null) && (ids.length() > 1))
            return ids.substring(0, ids.length() - 1);
        return "";
    }
    /**
     * 将字符串拆分成键值对
     * @param s 字符串
     * @param delim 分隔符
     * @return
     */
    public static Map splitToMap(String s, String delim) {
        Map map = new HashMap();
        String[] tokens = split(s, delim);
        String str = null;
        for (int j = 0; j < tokens.length; j++) {
            str = tokens[j];
            String[] arryTokens = split(str, "=");
            map.put(arryTokens[0], arryTokens.length > 1 ? arryTokens[1] : null);
        }
        return map;
    }
    /**
     * 将字符串追加到字符串数组中
     * @param arr 字符串数组
     * @param s 字符串
     * @return 新生成的字符串数组
     */
    public static String[] addToArray(String[] arr, String s) {
        String[] newArr = new String[arr.length + 1];
        System.arraycopy(arr, 0, newArr, 0, arr.length);
        newArr[arr.length] = s;
        return newArr;
    }
    /**
     * 截取字符串中最后一个'.'之后的部分
     * @param qualifiedName 字符串
     * @return
     */
    public static String unqualify(String qualifiedName) {
        return unqualify(qualifiedName, '.');
    }
    /**
     * 截取字符串中最后一个分隔字符之后的部分
     * @param qualifiedName 字符串
     * @param separator 分隔字符
     * @return
     */
    public static String unqualify(String qualifiedName, char separator) {
        return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
    }
    /**
     * 将字符串的首字母大写
     * @param str
     * @return
     */
    public static String capitalizeFirst(String str) {
        return changeFirstCharacterCase(true, str);
    }
    /**
     * 将字符串的首字母小写
     * @param str
     * @return
     */
    public static String uncapitalizeFirst(String str) {
        return changeFirstCharacterCase(false, str);
    }

    private static String changeFirstCharacterCase(boolean capitalize, String str) {
        int strLen;
        if ((str == null) || ((strLen = str.length()) == 0))
            return str;
        StringBuffer buf = new StringBuffer(strLen);
        if (capitalize)
            buf.append(Character.toUpperCase(str.charAt(0)));
        else {
            buf.append(Character.toLowerCase(str.charAt(0)));
        }
        buf.append(str.substring(1));
        return buf.toString();
    }
    /**
     * 修正路径字符串，将\\替换为/，./和../替换为""
     * @param path
     * @return
     */
    public static String cleanPath(String path) {
        String p = replace(path, WIN_CHANGE_PATH, CHANGE_PATH);
        String[] pArray = split(p, CHANGE_PATH);
        List pList = new LinkedList();
        int tops = 0;
        for (int i = pArray.length - 1; i >= 0; i--) {
            if (CURRENT_PATH.equals(pArray[i]))
                continue;
            if (TOP_PATH.equals(pArray[i])) {
                tops++;
            }
            else if (tops > 0)
                tops--;
            else {
                pList.add(0, pArray[i]);
            }
        }
        return join(pList, CHANGE_PATH);
    }
    /**
	 * 从Blob大对象中获得StringBuffer
	 * 
	 * @param blob
	 * @return
	 */
    public static StringBuffer getStrByBlob(Blob blob) throws IOException, SQLException {
        StringBuffer buf = new StringBuffer();
        BufferedReader in = new BufferedReader(new InputStreamReader(blob.getBinaryStream()));
        String tmp = "";
        while ((in != null) && ((tmp = in.readLine()) != null)) {
            buf.append(tmp);
        }
        in = null;
        tmp = null;
        return buf;
    }
    /**
	 * 从Clob大对象中获得StringBuffer
	 * 
	 * @param clob
	 * @return
	 */
    public static StringBuffer getStrByClob(Clob clob) throws IOException, SQLException {
        StringBuffer buf = new StringBuffer();
        BufferedReader in = new BufferedReader(new InputStreamReader(clob.getAsciiStream()));
        String tmp = "";
        while ((in != null) && ((tmp = in.readLine()) != null)) {
            buf.append(tmp);
        }
        in = null;
        tmp = null;
        return buf;
    }
    /**
	 * 压缩字符串
	 * 
	 * @param str
	 *            源字符串
	 * @return
	 */
    public static String compress(String str) throws Exception {
        ByteArrayOutputStream data_ = null;
        String base64Src = "";
        data_ = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(new GZIPOutputStream(data_));
        out.write(str.getBytes());
        out.close();
        data_.close();
        base64Src = new String(Base64Utils.encode(data_.toByteArray()));
        return base64Src;
    }
    /**
	 * 解压缩字符串
	 * 
	 * @param str
	 * @return
	 */
    public static String deCompress(String str) throws Exception {
        String object_ = null;
        byte[] byteSrc64 = Base64Utils.decode(str.getBytes());
        ByteArrayInputStream bi = new ByteArrayInputStream(byteSrc64);
        GZIPInputStream gzIn = new GZIPInputStream(new ByteArrayInputStream(byteSrc64));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;
        while ((len = gzIn.read(buf)) >= 0) {
            baos.write(buf, 0, len);
        }
        object_ = new String(baos.toByteArray());
        baos.close();
        gzIn.close();
        return object_;
    }
    /**
     * 字符串转base64编码
     * @param s
     * @return
     */
    public static String encodedBase64(String s) {
        if (s == null) {
            return null;
        }
        return Base64Utils.encode(s);
    }
    /**
     * base64编码转字符串
     * @param s
     * @param code
     * @return
     */
    public static String decodedBase64(String s, String code) {
        if (s == null)
            return null;
        try
        {
            byte[] b = Base64Utils.decode(s.getBytes());
            if (code != null) return new String(b, code);
            return new String(b); 
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }
    /**
     * 应用程序到数据库中文转码
     * @param value
     * @return 返回ISO8859-1字符
     */
    public static String app2DbEncode(String value) {
        if ((value == null) || (value.equals("null"))) {
            value = "";
        }
        try {
            return new String(value.getBytes("ISO8859-1"));
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return value + "";
    }
    /**
     * 数据库到应用程序中文转码
     * @param value
     * @return 返回ISO8859-1字符
     */
    public static String db2AppEncode(String value) {
        if ((value == null) || (value.equals("null"))) {
            value = "";
        }
        try {
            return new String(value.getBytes("ISO8859-1")); 
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return value + "";
    }

    public static String unicodeToGB(String strIn) {
        if ((strIn == null) || (strIn.equals(""))) {
            return strIn;
        }
        try {
            String strOut = new String(strIn.getBytes("GBK"), "ISO8859_1");
            return strOut;
        } catch (UnsupportedEncodingException e) {
            logger.info("Unsupported Encoding at CharsetConvert.unicodeToGB()");
        }
        return strIn;
    }

    public static String escape(String src) {
        if (src == null) return src;
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length() * 6);
        for (int i = 0; i < src.length(); i++) {
            char j = src.charAt(i);
            if ((Character.isDigit(j)) || (Character.isLowerCase(j)) || (Character.isUpperCase(j))) { tmp.append(j);
            } else if (j < 'Ā') {
                tmp.append("%");
                if (j < '\020') tmp.append("0");
                tmp.append(Integer.toString(j, 16));
            } else {
                tmp.append("%u");
                tmp.append(Integer.toString(j, 16));
            }
        }
        return tmp.toString();
    }

    public static String unescape(String src) {
        if (src == null) return src;
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length());
        int lastPos = 0; int pos = 0;
        while (lastPos < src.length()) {
            pos = src.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (src.charAt(pos + 1) == 'u') {
                    char ch = (char)Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6; continue;
                }
                char ch = (char)Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
                tmp.append(ch);
                lastPos = pos + 3; continue;
            }

            if (pos == -1) {
                tmp.append(src.substring(lastPos));
                lastPos = src.length(); continue;
            }
            tmp.append(src.substring(lastPos, pos));
            lastPos = pos;
        }
        return tmp.toString();
    }

    public static String GBToUnicode(String strIn) {
        if ((strIn == null) || (strIn.equals(""))) {
            return strIn;
        }

        try
        {
            String strOut = new String(strIn.getBytes("ISO8859_1"), "GBK");
            return strOut;
        } catch (UnsupportedEncodingException e) {
            logger.info("Unsupported Encoding at CharsetConvert.GBToUnicode()");
        }return strIn;
    }

    public static String htmlEncode(String str) {
        if (!isNotNull(str))
            return "";
        str = str.replaceAll("&", "&amp;");
        str = str.replaceAll(">", "&gt;");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll("\"", "&quot;");
        str = str.replaceAll("'", "&apos;");
        return str;
    }

    private static String deleteStart(String str, String start) {
        String kaitou = str;
        while ((kaitou.length() > 0) && (kaitou.indexOf(start) == 0)) {
            kaitou = kaitou.substring(start.length());
        }
        return kaitou;
    }

    private static String deleteEnd(String str, String End)
    {
        String gaihou = str;
        while ((gaihou.length() >= End.length()) && (gaihou.endsWith(End))) {
            gaihou = gaihou.substring(0, gaihou.length() - End.length());
        }
        return gaihou;
    }

    public static String deleteHtmlTag(String str) {
        String genggai = str;
        genggai = deleteStart(genggai, "&nbsp;");
        genggai = deleteStart(genggai, "<p>");
        genggai = deleteEnd(genggai, "&nbsp;");
        genggai = deleteEnd(genggai, "</p>");
        return genggai;
    }

    /**
     * 多个相同的字符串叠加
     * @param count 叠加数量
     * @param tag 字符串
     * @return 叠加后产生的新字符串
     */
    public static String joinTag(int count, String tag) {
        String temp = "";
        for (int i = 0; i < count; i++) {
            temp = temp + tag;
        }
        return temp;
    }
    /**
     * 获取URL中的参数值
     * @param url URL
     * @param name 参数名
     * @return 参数值
     */
    public static String getURLParameter(String url, String name) {
        String js_get = url;
        int start = -1;
        int len = 0;
        int ___start = js_get.indexOf(name + '=');
        int __start = js_get.indexOf("?" + name + '=');
        int _start = js_get.indexOf("&" + name + '=');
        if (___start == 0) {
            start = ___start;
            len = start + name.length() + 1;
        } else if (__start > -1) {
            start = __start;
            len = start + name.length() + 2;
        } else if (_start > -1) {
            start = _start;
            len = start + name.length() + 2;
        }
        if (start == -1) return "";
        int end = js_get.indexOf('&', len);
        if (end == -1) end = js_get.length();
        return js_get.substring(len, end);
    }
    /**
     * 给URL添加参数
     * @param url 原URL
     * @param parameterName 参数名
     * @param parameterValue 参数值
     * @return 添加参数后的url
     */
    public static String addURLParameter(String url, String parameterName, String parameterValue) {
        if (url.indexOf("?") > -1)
            url = url + "&" + parameterName + "=" + parameterValue;
        else {
            url = url + "?" + parameterName + "=" + parameterValue;
        }
        return url;
    }

    /**
     * Utf8URL编码
     * @param s
     * @return
     */
    public static String utf8URLencode(String text) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if ((c >= 0) && (c <= 'ÿ')) {
                result.append(c);
            } else {
                byte[] b = new byte[0];
                try {
                    b = Character.toString(c).getBytes("UTF-8");
                } catch (Exception ex) {
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0) k += 256;
                    result.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return result.toString();
    }
    /**
 	 * LK [2009.08.03 16:31]
 	 * 把经IE端js encodeURI 转换过的URIstring编码转换成UTF-8
 	 * @return
 	 */
    public static String decodeURI2UTF8(String str) {
        String ret = "";
        if (isNotNull(str))
            try {
                ret = new String(str.toString().getBytes("ISO8859-1"), "UTF-8");
            }
            catch (UnsupportedEncodingException e) {
            }
        return ret;
    }
    /**
     * Utf8URL解码
     * @param text
     * @return
     */
    public static String utf8URLdecode(String text) {
        String result = "";
        int p = 0;
        if ((text != null) && (text.length() > 0)) {
            text = text.toLowerCase();
            p = text.indexOf("%e");
            if (p == -1) return text;
            while (p != -1) {
                result = result + text.substring(0, p);
                text = text.substring(p, text.length());
                if ((text == "") || (text.length() < 9)) return result;
                result = result + _codeToWord(text.substring(0, 9));
                text = text.substring(9, text.length());
                p = text.indexOf("%e");
            }
        }
        return result + text;
    }

    private static String _codeToWord(String text) {
        String result;
        if (_utf8codeCheck(text)) {
            byte[] code = new byte[3];
            code[0] = (byte)(Integer.parseInt(text.substring(1, 3), 16) - 256);
            code[1] = (byte)(Integer.parseInt(text.substring(4, 6), 16) - 256);
            code[2] = (byte)(Integer.parseInt(text.substring(7, 9), 16) - 256);
            try {
                result = new String(code, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                result = null;
            }
        } else {
            result = text;
        }
        return result;
    }

    private static boolean _utf8codeCheck(String text) {
        String sign = "";
        if (text.startsWith("%e")) {
            int i = 0; for (int p = 0; p != -1; i++) {
                p = text.indexOf("%", p);
                if (p != -1)
                    p++;
                sign = sign + p;
            }
        }
        return sign.equals("147-1");
    }

    public static boolean _isUtf8Url(String text)
    {
        text = text.toLowerCase();
        int p = text.indexOf("%");
        if ((p != -1) && (text.length() - p > 9)) {
            text = text.substring(p, p + 9);
        }
        return _utf8codeCheck(text);
    }
    /**
     * 产生一个长度为8的随机字符串，由大小写字符和数字组成
     * @return
     */
    public static String generate8Uuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[(x % 62)]);
        }
        return shortBuffer.toString();
    }
}
