package org.maccha.base.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collection;

public class FileUtils
{
  private static final char UNIX_SEPARATOR = '/';
  private static final char WINDOWS_SEPARATOR = '\\';

  public static File touch(String fullFilePath)
    throws IOException
  {
    if (fullFilePath == null) {
      return null;
    }
    File file = new File(fullFilePath);

    file.getParentFile().mkdirs();
    if (!file.exists()) file.createNewFile();
    return file;
  }

  public static File mkdir(String dirPath)
  {
    if (dirPath == null) {
      return null;
    }
    File dir = new File(dirPath);
    dir.mkdirs();
    return dir;
  }

  public static File createTempFile(String prefix, String suffix, File dir, boolean isReCreat)
    throws IOException
  {
    int exceptionsCount = 0;
    while (true)
      try {
        File file = File.createTempFile(prefix, suffix, dir).getCanonicalFile();
        if (isReCreat) {
          file.delete();
          file.createNewFile();
        }
        return file;
      } catch (IOException ioex) {
        exceptionsCount++; if (exceptionsCount >= 50)
          throw ioex;
      }
  }

  public static void copy(File src, File dest, boolean isOverride)
    throws IOException
  {
    if (!src.exists()) {
      throw new FileNotFoundException("File not exist: " + src);
    }
    if (!src.isFile()) {
      throw new IOException("Not a file:" + src);
    }
    if (equals(src, dest)) {
      throw new IOException("Files '" + src + "' and '" + dest + "' are equal");
    }

    if ((dest.exists()) && 
      (dest.isDirectory())) {
      dest = new File(dest, src.getName());
    }

    FileInputStream input = new FileInputStream(src);
    FileOutputStream output = new FileOutputStream(dest, !isOverride);
    try {
      IoUtils.copy(input, output);
    } finally {
      close(output);
      close(input);
    }

    if ((isOverride) && (src.length() != dest.length()))
      throw new IOException("Copy file failed of '" + src + "' to '" + dest + "' due to different sizes");
  }

  public static void move(File src, File dest, boolean isOverride)
    throws IOException
  {
    if (!src.exists()) {
      throw new FileNotFoundException("File already exist: " + src);
    }
    if ((dest.exists()) && 
      (isOverride)) dest.delete();

    if ((src.isDirectory()) && (dest.isFile())) {
      throw new IOException(StringUtils.format("Can not move directory [{}] to file [{}]", new Object[] { src, dest }));
    }

    if ((src.isFile()) && (dest.isDirectory())) {
      dest = new File(dest, src.getName());
    }

    try
    {
      copy(src, dest, isOverride);
      src.delete();
    } catch (Exception e) {
      throw new IOException(StringUtils.format("Move [{}] to [{}] failed!", new Object[] { src, dest }), e);
    }
  }

  public static boolean isExist(String path)
  {
    return new File(path).exists();
  }

  public static void close(Closeable closeable)
  {
    if (closeable == null) return; try
    {
      closeable.close();
    }
    catch (IOException e)
    {
    }
  }

  public static boolean equals(File file1, File file2)
  {
    try
    {
      file1 = file1.getCanonicalFile();
      file2 = file2.getCanonicalFile();
    } catch (IOException ignore) {
      return false;
    }
    return file1.equals(file2);
  }

  public static BufferedWriter getBufferedWriter(String path, String charset, boolean isAppend)
    throws IOException
  {
    return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(touch(path), isAppend), charset));
  }

  public static PrintWriter getPrintWriter(String path, String charset, boolean isAppend)
    throws IOException
  {
    return new PrintWriter(getBufferedWriter(path, charset, isAppend));
  }

  public static OutputStream getOutputStream(String path)
    throws IOException
  {
    return new FileOutputStream(touch(path));
  }

  public static void cleanDir(String dirPath)
  {
    File dir = new File(dirPath);
    if ((dir.exists()) && (dir.isDirectory())) {
      File[] files = dir.listFiles();
      for (File file : files) {
        if (file.isDirectory()) cleanDir(file.getAbsolutePath());
        file.delete();
      }
    }
  }

  public static BufferedReader getReader(String path, String charset)
    throws IOException
  {
    return new BufferedReader(new InputStreamReader(new FileInputStream(path), charset));
  }

  public static <T extends Collection<String>> T readLines(String path, String charset, T collection)
    throws IOException
  {
    BufferedReader reader = null;
    try {
      reader = getReader(path, charset);
      while (true) {
        line = reader.readLine();
        if (line == null) break;
        collection.add(line);
      }
      String line = collection;
      return line; } finally { close(reader); } throw localObject;
  }

  public static <T> T load(ReaderHandler<T> readerHandler, String path, String charset)
    throws IOException
  {
    BufferedReader reader = null;
    Object result = null;
    try {
      reader = getReader(path, charset);
      result = readerHandler.handle(reader);
    } catch (IOException e) {
      throw new IOException(e);
    } finally {
      close(reader);
    }
    return result;
  }

  public static String getExtension(String fileName)
  {
    if (fileName == null) {
      return null;
    }
    int index = fileName.lastIndexOf(".");
    if (index == -1) {
      return "";
    }
    String ext = fileName.substring(index + 1);

    return (ext.contains(String.valueOf('/'))) || (ext.contains(String.valueOf('\\'))) ? "" : ext;
  }

  public static int indexOfLastSeparator(String filePath)
  {
    if (filePath == null) {
      return -1;
    }
    int lastUnixPos = filePath.lastIndexOf('/');
    int lastWindowsPos = filePath.lastIndexOf('\\');
    return lastUnixPos >= lastWindowsPos ? lastUnixPos : lastWindowsPos;
  }

  public static void writeString(String content, String path, String charset)
    throws IOException
  {
    PrintWriter writer = null;
    try {
      writer = getPrintWriter(path, charset, false);
      writer.print(content);
    } finally {
      close(writer);
    }
  }

  public static String readString(String path, String charset)
    throws IOException
  {
    return new String(readBytes(new File(path)), charset);
  }

  public static void writeBytes(byte[] data, String path)
    throws IOException
  {
    writeBytes(touch(path), data);
  }

  public static void writeBytes(File dest, byte[] data)
    throws IOException
  {
    writeBytes(dest, data, 0, data.length, false);
  }

  public static void writeBytes(File dest, byte[] data, int off, int len, boolean append)
    throws IOException
  {
    if ((dest.exists() == true) && 
      (!dest.isFile())) {
      throw new IOException("Not a file: " + dest);
    }

    FileOutputStream out = null;
    try {
      out = new FileOutputStream(dest, append);
      out.write(data, off, len);
    } finally {
      close(out);
    }
  }

  public static byte[] readBytes(File file)
    throws IOException
  {
    if (!file.exists()) {
      throw new FileNotFoundException("File not exist: " + file);
    }
    if (!file.isFile()) {
      throw new IOException("Not a file:" + file);
    }

    long len = file.length();
    if (len >= 2147483647L) {
      throw new IOException("File is larger then max array size");
    }

    byte[] bytes = new byte[(int)len];
    FileInputStream in = null;
    try {
      in = new FileInputStream(file);
      in.read(bytes);
    } finally {
      close(in);
    }

    return bytes;
  }

  public static void writeStream(File dest, InputStream in)
    throws IOException
  {
    FileOutputStream out = null;
    try {
      out = new FileOutputStream(dest);
      IoUtils.copy(in, out);
    } finally {
      close(out);
    }
  }

  public static void writeStream(String fullFilePath, InputStream in)
    throws IOException
  {
    writeStream(touch(fullFilePath), in);
  }

  public static abstract interface ReaderHandler<T>
  {
    public abstract T handle(BufferedReader paramBufferedReader)
      throws IOException;
  }
}
