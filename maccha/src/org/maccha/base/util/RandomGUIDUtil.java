package org.maccha.base.util;

import java.util.Date;
import java.util.UUID;

public class RandomGUIDUtil
{
  private static Date date = new Date();
  private static StringBuilder buf = new StringBuilder();
  private static int seq = 0;
  private static final int ROTATION = 9999;

  public static synchronized long getUniqueId()
  {
    if (seq > 9999) seq = 1;
    buf.delete(0, buf.length());
    date.setTime(System.currentTimeMillis());
    String str = String.format("%1$tY%1$tm%1$td%1$tk%1$tM%1$tS%2$05d", new Object[] { date, Integer.valueOf(seq++) });
    return Long.parseLong(str);
  }
  public static String randomUUID() {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }
}
