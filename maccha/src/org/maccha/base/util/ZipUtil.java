package org.maccha.base.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.ZipInputStream;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

public class ZipUtil
{
  public static final String XML_READ_ENCODING = "GB18030";
  public static final String XML_WRITE_ENCODING = "GB18030";
  public static final int COMPRESSBUFFERSIZE = 1024;
  public static final int DEFLATED = 8;
  public static final int NO_COMPRESSION = 0;
  public static final int BEST_SPEED = 1;
  public static final int BEST_COMPRESSION = 9;
  public static final int DEFAULT_COMPRESSION = -1;

  public static void fileDecompress(String inputFile, String outputFile)
    throws IOException
  {
    FileOutputStream outputStream = null;
    FileInputStream inputStream = null;
    ZipInputStream zipData = null;
    try
    {
      outputStream = new FileOutputStream(outputFile);
      inputStream = new FileInputStream(inputFile);
      zipData = new ZipInputStream(inputStream);
      java.util.zip.ZipEntry entry = zipData.getNextEntry();

      int length = 0;
      byte[] buffer = new byte[1024];

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

  /** @deprecated */
  private static void unZipFile(ZipFile zipFile, String outputDirectory)
    throws Exception
  {
    try
    {
      Enumeration e = zipFile.getEntries();
      org.apache.tools.zip.ZipEntry zipEntry = null;
      while (e.hasMoreElements()) {
        zipEntry = (org.apache.tools.zip.ZipEntry)e.nextElement();
        System.out.println("unziping " + zipEntry.getName());
        if (zipEntry.isDirectory()) {
          String name = zipEntry.getName();
          name = name.substring(0, name.length() - 1);
          File f = new File(outputDirectory + File.separator + name);
          f.mkdirs();
          continue;
        }File f1 = new File(outputDirectory);
        File f = new File(outputDirectory + File.separator + zipEntry.getName());
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

          byte[] by = new byte[1024];
          int c;
          while ((c = in.read(by)) != -1)
            out.write(by, 0, c);
        }
        catch (Exception ex) {
          throw ex;
        } finally {
          if (out != null) out.close();
          if (in != null) in.close(); 
        }
      }
    }
    catch (Exception ex)
    {
      throw ex;
    }
  }

  /** @deprecated */
  public static void unZipFile(String zipFileName, String outputDirectory)
    throws Exception
  {
    ZipFile zipFile = null;
    try {
      zipFile = new ZipFile(zipFileName);
      unZipFile(zipFile, outputDirectory);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      if (zipFile != null)
        zipFile.close();
    }
  }

  /** @deprecated */
  public static void unZipFile(File zipFileName, String outputDirectory)
    throws Exception
  {
    ZipFile zipFile = null;
    try {
      zipFile = new ZipFile(zipFileName);
      unZipFile(zipFile, outputDirectory);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      if (zipFile != null)
        zipFile.close();
    }
  }

  public static void fileCompress(String inputFile, String outputFile) throws IOException
  {
    fileCompress(inputFile, outputFile, 9);
  }

  public static void fileCompress(String inputFile, String outputFile, int levle) throws IOException
  {
    FileOutputStream outputStream = null;
    FileInputStream inputStream = null;
    ZipOutputStream zipData = null;
    try
    {
      File file = new File(inputFile);
      outputStream = new FileOutputStream(outputFile);
      inputStream = new FileInputStream(inputFile);
      zipData = new ZipOutputStream(outputStream);

      zipData.putNextEntry(new org.apache.tools.zip.ZipEntry(file.getAbsolutePath()));
      zipData.setLevel(levle);

      int length = 0;
      byte[] buffer = new byte[1024];

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
      if (inputStream != null)
        inputStream.close();
    }
    catch (Exception e) {
    }
  }

  private static void closeOutputStream(OutputStream outputStream) {
    try {
      if (outputStream != null)
        outputStream.close();
    }
    catch (Exception e)
    {
    }
  }

  public static InputStream dataDecompress1(InputStream inputData) throws IOException {
    return new ByteArrayInputStream(dataDecompress(inputData));
  }

  public static InputStream dataDecompress1(InputStream inputData, int size) throws IOException
  {
    return new ByteArrayInputStream(dataDecompress(getBytes(inputData, size)));
  }

  private static byte[] getBytes(InputStream inputData, int size)
    throws IOException
  {
    byte[] data = new byte[size];
    inputData.read(data);

    return data;
  }

  public static byte[] dataDecompress(InputStream inputData) throws IOException
  {
    int count = 0;
    byte[] b = new byte[2048];
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try
    {
      while ((count = inputData.read(b, 0, 2048)) != -1) {
        baos.write(b, 0, count);
      }

      byte[] arrayOfByte1 = dataDecompress(baos.toByteArray());
      return arrayOfByte1;
    }
    catch (IOException ioe)
    {
      throw ioe;
    } finally {
      closeOutputStream(baos);
      closeInputStream(inputData); } 
  }

  public static byte[] dataDecompress(byte[] inputData) throws IOException
  {
    ByteArrayOutputStream bouts = new ByteArrayOutputStream();
    byte[] bout = new byte[1024];
    Inflater decompresser = new Inflater();
    decompresser.setInput(inputData, 0, inputData.length);
    int length = 0;
    try
    {
      while ((length = decompresser.inflate(bout)) > 0) {
        bouts.write(bout, 0, length);
      }

      decompresser.end();

      byte[] arrayOfByte1 = bouts.toByteArray();
      return arrayOfByte1;
    }
    catch (DataFormatException dfe)
    {
      throw new IOException(dfe.getMessage());
    } finally {
      closeOutputStream(bouts); } 
  }

  public static byte[] dataCompress(String outputData) throws IOException
  {
    return dataCompress(outputData.getBytes("GB18030"));
  }

  public static byte[] dataCompress(byte[] outputData) throws IOException {
    return dataCompress(outputData, 9);
  }

  public static byte[] dataCompress(byte[] outputData, int level) throws IOException
  {
    ByteArrayOutputStream bouts = new ByteArrayOutputStream();
    byte[] bout = new byte[1024];
    int length = 0;
    Deflater compresser = new Deflater(level);
    compresser.setInput(outputData);
    compresser.finish();
    try
    {
      while ((length = compresser.deflate(bout)) > 0) {
        bouts.write(bout, 0, length);
      }

      byte[] arrayOfByte1 = bouts.toByteArray();
      return arrayOfByte1; } finally { closeOutputStream(bouts); } 
  }

  public static void zipFile(String zipFileName, File inputFile) throws IOException {
    ZipOutputStream out = null;
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
      out.putNextEntry(new org.apache.tools.zip.ZipEntry(base + "/"));
      base = base + "/";
      for (int i = 0; i < fl.length; i++)
        zipFile(out, fl[i], base + fl[i].getName());
    }
    else {
      FileInputStream in = null;
      try {
        out.putNextEntry(new org.apache.tools.zip.ZipEntry(base));
        in = new FileInputStream(f);
        int b;
        while ((b = in.read()) != -1)
          out.write(b);
      }
      finally {
        if (in != null) in.close(); 
      }
    }
  }
}
