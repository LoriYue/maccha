package org.maccha.base.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.ZipInputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

public class ZipUtil {
	public static final String XML_READ_ENCODING = "GB18030";
	public static final String XML_WRITE_ENCODING = "GB18030";
	public static final int COMPRESSBUFFERSIZE = 1024;
	/**
	 * Compression method for the deflate algorithm (the only one currently
	 * supported).
	 */
	public static final int DEFLATED = Deflater.DEFLATED;
	/**
	 * Compression level for no compression.
	 */
	public static final int NO_COMPRESSION = Deflater.NO_COMPRESSION;
	/**
	 * Compression level for fastest compression.
	 */
	public static final int BEST_SPEED = Deflater.BEST_SPEED;
	/**
	 * Compression level for best compression.
	 */
	public static final int BEST_COMPRESSION = Deflater.BEST_COMPRESSION;
	/**
	 * Default compression level.
	 */
	public static final int DEFAULT_COMPRESSION = Deflater.DEFAULT_COMPRESSION;

	//文件解压
	public static void fileDecompress(String inputFile, String outputFile) throws IOException {
		FileOutputStream outputStream = null;
		FileInputStream inputStream = null;
		ZipInputStream zipData = null;
		try {
			outputStream = new FileOutputStream(outputFile);
			inputStream = new FileInputStream(inputFile);
			zipData = new ZipInputStream(inputStream);
			java.util.zip.ZipEntry entry = zipData.getNextEntry();
			int length = 0;
			byte[] buffer = new byte[COMPRESSBUFFERSIZE];
			while ((length = zipData.read(buffer)) != -1) {
				outputStream.write(buffer, 0, length);
			}
			outputStream.flush();
		} catch (IOException ioe) {
			throw ioe;
		} finally {
			closeInputStream(zipData);
			closeOutputStream(outputStream);
			closeInputStream(inputStream);
		}
	}
	/** 
	 * @deprecated 解压处理 
	 * @param zipFileName 
	 * 要进行压缩处理的文件(d:/aa.zip") 
	 * @param outputDirectory 
	 * 压缩处理后的文件路径(d:/) 
	 */
	private static void unZipFile(ZipFile zipFile, String outputDirectory) throws Exception {

		try {
			Enumeration e = zipFile.getEntries();
			org.apache.tools.zip.ZipEntry zipEntry = null;
			while (e.hasMoreElements()) {
				zipEntry = (org.apache.tools.zip.ZipEntry) e.nextElement();
				System.out.println("unziping " + zipEntry.getName());
				if (zipEntry.isDirectory()) {
					String name = zipEntry.getName();
					name = name.substring(0, name.length() - 1);
					File f = new File(outputDirectory + File.separator + name);
					f.mkdirs();
				} else {
					File f1 = new File(outputDirectory);
					File f = new File(outputDirectory + File.separator+ zipEntry.getName());
					System.out.println(f.getParent());
					File f2 = new File(f.getParent());
					if (!f2.exists())
						f2.mkdirs();
					f.createNewFile();
					InputStream in = null;
					FileOutputStream out = null;
					try {
						in = zipFile.getInputStream(zipEntry);
						out = new FileOutputStream(f);
						int c;
						byte[] by = new byte[1024];
						while ((c = in.read(by)) != -1) {
							out.write(by, 0, c);
						}
					} catch (Exception ex) {
						throw ex;
					} finally {
						if (out != null)out.close();
						if (in != null)in.close();
					}
				}
			}
		} catch (Exception ex) {
			throw ex ;
		}
	}
	/** 
	 * @deprecated 解压处理 
	 * @param zipFileName 
	 * 要进行压缩处理的文件(d:/aa.zip") 
	 * @param outputDirectory 
	 * 压缩处理后的文件路径(d:/) 
	 */
	public static void unZipFile(String zipFileName, String outputDirectory)throws Exception {
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(zipFileName);
			unZipFile(zipFile,outputDirectory);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (zipFile != null)
				zipFile.close();
		}
	}
	/** 
	 * @deprecated 解压处理 
	 * @param zipFileName 
	 * 要进行压缩处理的文件(d:/aa.zip") 
	 * @param outputDirectory 
	 * 压缩处理后的文件路径(d:/) 
	 */
	public static void unZipFile(File zipFileName, String outputDirectory)
			throws Exception {
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(zipFileName);
			unZipFile(zipFile,outputDirectory);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (zipFile != null)
				zipFile.close();
		}
	}
	//文件压缩
	public static void fileCompress(String inputFile, String outputFile)
			throws IOException {
		fileCompress(inputFile, outputFile, BEST_COMPRESSION);
	}

	//文件压缩
	public static void fileCompress(String inputFile, String outputFile,int levle) throws IOException {
		FileOutputStream outputStream = null;
		FileInputStream inputStream = null;
		ZipOutputStream zipData = null;
		try {
			File file = new File(inputFile);
			outputStream = new FileOutputStream(outputFile);
			inputStream = new FileInputStream(inputFile);
			zipData = new ZipOutputStream(outputStream);
			zipData.putNextEntry(new ZipEntry(file.getAbsolutePath()));
			zipData.setLevel(levle);
			int length = 0;
			byte[] buffer = new byte[COMPRESSBUFFERSIZE];
			while ((length = inputStream.read(buffer)) != -1) {
				zipData.write(buffer, 0, length);
			}
			zipData.flush();
		} catch (IOException ioe) {
			throw ioe;
		} finally {
			closeOutputStream(zipData);
			closeOutputStream(outputStream);
			closeInputStream(inputStream);
		}
	}

	private static void closeInputStream(InputStream inputStream) {
		try {
			if (inputStream != null) {
				inputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void closeOutputStream(OutputStream outputStream) {
		try {
			if (outputStream != null) {
				outputStream.close();
			}
		} catch (Exception e) {
		}
	}

	public static InputStream dataDecompress1(InputStream inputData) throws IOException {
		return new ByteArrayInputStream(dataDecompress(inputData));
	}

	public static InputStream dataDecompress1(InputStream inputData, int size) throws IOException {
		return new ByteArrayInputStream(dataDecompress(getBytes(inputData, size)));
	}

	private static byte[] getBytes(InputStream inputData, int size) throws IOException {
		byte[] data = new byte[size];
		inputData.read(data);
		return data;
	}

	public static byte[] dataDecompress(InputStream inputData) throws IOException {
		int count = 0;
		byte[] b = new byte[2048];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			while ((count = inputData.read(b, 0, 2048)) != -1) {
				baos.write(b, 0, count);
			}
			return dataDecompress(baos.toByteArray());
		} catch (IOException ioe) {
			throw ioe;
		} finally {
			closeOutputStream(baos);
			closeInputStream(inputData);
		}
	}

	public static byte[] dataDecompress(byte[] inputData) throws IOException {
		ByteArrayOutputStream bouts = new ByteArrayOutputStream();
		byte[] bout = new byte[COMPRESSBUFFERSIZE];
		Inflater decompresser = new Inflater();
		decompresser.setInput(inputData, 0, inputData.length);
		int length = 0;
		try {
			while ((length = decompresser.inflate(bout)) > 0) {
				bouts.write(bout, 0, length);
			}
			decompresser.end();
			return bouts.toByteArray();
		} catch (java.util.zip.DataFormatException dfe) {
			throw new IOException(dfe.getMessage());
		} finally {
			closeOutputStream(bouts);
		}
	}

	public static byte[] dataCompress(String outputData) throws IOException {
		return dataCompress(outputData.getBytes(XML_WRITE_ENCODING));
	}

	public static byte[] dataCompress(byte[] outputData) throws IOException {
		return dataCompress(outputData, BEST_COMPRESSION);
	}

	public static byte[] dataCompress(byte[] outputData, int level) throws IOException {
		ByteArrayOutputStream bouts = new ByteArrayOutputStream();
		byte[] bout = new byte[COMPRESSBUFFERSIZE];
		int length = 0;
		Deflater compresser = new Deflater(level);
		compresser.setInput(outputData);
		compresser.finish();
		try {
			while ((length = compresser.deflate(bout)) > 0) {
				bouts.write(bout, 0, length);
			}
			return bouts.toByteArray();
		} finally {
			closeOutputStream(bouts);
		}
	}
	public static void zipFile(String zipFileName, File inputFile) throws IOException {
		ZipOutputStream out = null ;
		try {
			out = new ZipOutputStream(new FileOutputStream(zipFileName));
			zipFile(out, inputFile, inputFile.getName());
		} finally {
			closeOutputStream(out);
		}
	}

	private static void zipFile(ZipOutputStream out, File f, String base) throws IOException {
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			out.putNextEntry(new ZipEntry(base + "/"));
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < fl.length; i++) {
				zipFile(out, fl[i], base + fl[i].getName());
			}
		} else {
			FileInputStream in = null ;
			try{
				out.putNextEntry(new ZipEntry(base));
				in = new FileInputStream(f);
				int b;
				while ((b = in.read()) != -1) {
					out.write(b);
				}
			}finally{
				if(in != null) in.close();
			}
		}
	}
}
