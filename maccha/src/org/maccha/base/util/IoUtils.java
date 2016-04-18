package org.maccha.base.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.FileChannel;

public class IoUtils
{
  public static final int DEFAULT_BUFFER_SIZE = 1024;

  public static int copy(Reader reader, Writer writer)
    throws IOException
  {
    return copy(reader, writer, 1024);
  }

  public static int copy(Reader reader, Writer writer, int bufferSize)
    throws IOException
  {
    char[] buffer = new char[bufferSize];
    int count = 0;
    int readSize;
    while ((readSize = reader.read(buffer, 0, bufferSize)) >= 0) {
      writer.write(buffer, 0, readSize);
      count += readSize;
    }
    writer.flush();

    return count;
  }

  public static int copy(InputStream in, OutputStream out)
    throws IOException
  {
    return copy(in, out, 1024);
  }

  public static int copy(InputStream in, OutputStream out, int bufferSize)
    throws IOException
  {
    byte[] buffer = new byte[bufferSize];
    int count = 0;
    for (int n = -1; (n = in.read(buffer)) != -1; ) {
      out.write(buffer, 0, n);
      count += n;
    }
    out.flush();

    return count;
  }

  public static long copy(FileInputStream in, FileOutputStream out)
    throws IOException
  {
    FileChannel inChannel = in.getChannel();
    FileChannel outChannel = out.getChannel();

    return inChannel.transferTo(0L, inChannel.size(), outChannel);
  }

  public static String getString(InputStream in, String charset)
    throws IOException
  {
    StringBuilder content = new StringBuilder();

    BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
    String line = null;
    while ((line = reader.readLine()) != null) {
      content.append(line);
    }

    return content.toString();
  }
}
