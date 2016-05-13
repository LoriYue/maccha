package org.maccha.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 1、采用字节的形式读取rtf模板内容 2、将可变的内容字符串转为rtf编码 3、替换原文中的可变部分，形成新的rtf文档 模板建立时注意：
 * 1.模板中的宏变量格式为"$变量名称$",变量名称最好从文本编辑器中复制到rtf模板里面，在进行字体编辑
 * 2.模板中的宏变量字体需要设置为“宋体“，否则中文无法显示
 * 
 * @author jihaifeng
 * 
 */
public class RTFUtils {
	/**
	 * 字符串转换为rtf编码
	 * 
	 * @param content
	 * @return
	 */
	public String strToRtf(String content) throws Exception {
		// content=string2unicode(content);

		char[] digital = "0123456789ABCDEF".toCharArray();
		StringBuffer sb = new StringBuffer("");
		byte[] bs = content.getBytes("GBK");
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append("\\'");
			sb.append(digital[bit]);
			bit = bs[i] & 0x0f;
			sb.append(digital[bit]);
		}
		return sb.toString();
	}

	/**
	 * 替换文档的可变部分
	 * 
	 * @param content
	 * @param replacecontent
	 * @param flag
	 * @return
	 */
	public String replaceRTF(String content, String markersign,
			String replacecontent) {
		String target = "";
		try {
			String rc = strToRtf(replacecontent);
			System.out.println(":::::::::::::::::::::::::::::::::::rc = " + rc);
			// String target = content.replace("$" + markersign + "$", rc);
			target = StringUtils.replace(content, "$" + markersign + "$", rc);
		} catch (Exception e) {
		}
		return target;
	}

	/**
	 * 建立文件路径
	 * 
	 * @param flag
	 * @return
	 */
	public String createSavePath(String path) {
		File fDirecotry = new File(path);
		if (!fDirecotry.exists()) {
			fDirecotry.mkdirs();
		}
		return path;
	}

	/**
	 * 半角转为全角
	 */
	public String ToSBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 32) {
				c[i] = (char) 12288;
				continue;
			}
			if (c[i] < 127) {
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}

	/**
	 * 替换rtf中的宏变量,并生成替换后新的rtf文件
	 * 
	 * @param tempfilePath 模板路径（绝对路径）
	 * @param tagFilepath 新的rtf文件路径(文件夹)
	 * @param markersignDate 宏变量键值对
	 * @return 新的rtf文件绝对路径
	 */
	public String rtf(String tempfilePath, String tagFilepath, HashMap markersignData) {
		/* 构建生成文件名 */
		String targetFilename = RandomGUIDUtil.getUniqueId() + ".rtf";
		/* 字节形式读取模板文件内容,将结果转为字符串 */
		String sourcecontent = "";
		InputStream ins = null;
		try {
			ins = new FileInputStream(tempfilePath);
			byte[] b = new byte[1638400];
			if (ins == null) {
				System.out.println("源模板文件不存在");
			}
			int bytesRead = 0;
			while (true) {
				// return final read bytes counts
				bytesRead = ins.read(b, 0, 1638400);
				// end of InputStream
				if (bytesRead == -1) {
					System.out.println("读取模板文件结束");
					break;
				}
				// convert to string using bytes
				sourcecontent += new String(b, 0, bytesRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				ins.close();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		/* 修改变化部分 */
		String targetcontent = "";
		Iterator keys = markersignData.keySet().iterator();
		String oldText = "";
		Object newValue;
		int keysfirst = 0;
		while (keys.hasNext()) {
			oldText = (String) keys.next();
			newValue = markersignData.get(oldText);
			if (newValue == null)
				newValue = " ";
			String newText = (String) newValue;
			if (keysfirst == 0)
				targetcontent = replaceRTF(sourcecontent, oldText, newText);
			else
				targetcontent = replaceRTF(targetcontent, oldText, newText);
			keysfirst++;
		}
		/* 结果输出保存到文件 */
		FileWriter fw = null;
		PrintWriter out = null;
		String strTargetFilePath = createSavePath(tagFilepath) + File.separator
				+ targetFilename;
		try {
			fw = new FileWriter(strTargetFilePath, true);
			out = new PrintWriter(fw);
			if (targetcontent.equals("") || targetcontent == "") {
				out.println(sourcecontent);
			} else {
				out.println(targetcontent);
			}
			System.out.println("生成文件：" + strTargetFilePath + "成功！");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				out.close();
				fw.close();
			} catch (Exception ex) {
			}
		}
		return targetFilename;
	}
}
