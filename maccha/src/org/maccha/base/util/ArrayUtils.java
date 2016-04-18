package org.maccha.base.util;

import java.util.Iterator;
import java.util.Vector;
/**
 *
 * <p>Description:　字符串，参数对象，数字等数据的
 * 数据收集，数组转换功能</p>
 */
public class ArrayUtils
{
  private Vector strV = new Vector();
  private Vector intV = new Vector();
  /**
   * 收集字符串
   * @param param 字符串
   */
  public void addString(String param) {
    if (param == null)
      return;
    this.strV.add(param);
  }
  /**
   * 收集字符串数组
   * @param array
   */
  public void addStringArray(String[] array) {
    if (array == null)
      return;
    for (int i = 0; i < array.length; i++)
      addString(array[i]);
  }
  /**
   * 从容器取出收集的String[]数组
   * @return
   */
  public String[] getStringArray() {
	  String[] tmp = new String[strV.size()];
	  for (int i = 0; i < tmp.length; i++) {
		  tmp[i] = (String) strV.get(i);
	  }
	  return tmp;
  }
  /**
   * 判断是否包含字符串
   * @param value 字符串
   * @return 是返回 true 否返回false
   */
  public boolean stringContains(String value) {
    return this.strV.contains(value);
  }
  /**
   * 删除字符串
   * @param value 字符串
   * @return 包含字符串则返回true，否则返回false
   */
  public boolean stringRemove(String value) {
    return this.strV.remove(value);
  }
  /**
   * 收集数字字符串
   * @param value 可转换为整数的字符串
   */
  public void addInt(String value) {
    this.intV.add(new Integer(value));
  }
  /**
   * 收集数字
   * @param value 整数
   */
  public void addInt(int value) {
    this.intV.add(new Integer(value));
  }
  /**
   * 从容器取出收集的int[]数组
   * @return
   */
  public int[] getIntArray() {
    int[] tmp = new int[this.intV.size()];
    for (int i = 0; i < tmp.length; i++)
      tmp[i] = ((Integer)this.intV.get(i)).intValue();
    return tmp;
  }
  /**
   * 清除数据
   */
  public void clear() {
    this.intV.clear();
    this.strV.clear();
  }
  /**
   * 将Object数组转换为String数组
   * @param objectArray
   * @return
   */
  public static String[] toStringArray(Object[] objectArray) {
    String[] tmp = new String[objectArray.length];
    for (int i = 0; i < tmp.length; i++) {
      tmp[i] = ("" + objectArray[i]);
    }
    return tmp;
  }
  /**
   * 将Object数组转换为int数组
   * @param objectArray
   * @return
   */
  public static int[] toIntArray(Object[] objectArray) {
    int[] tmp = new int[objectArray.length];
    for (int i = 0; i < tmp.length; i++) {
      tmp[i] = new Integer("" + objectArray[i]).intValue();
    }
    return tmp;
  }
  /**
   * 将Iterator转换为Object数组
   * @param it
   * @return
   */
  public static Object[] toObjectArray(Iterator it) {
    Vector vector = new Vector();
    for (int i = 0; (it != null) && (it.hasNext()); i++) {
      vector.add(it.next());
    }
    return vector.toArray();
  }
  /**
   * 将缓存空间中的字符串数组和整数数组转换为可打印的字符串
   */
  public String toString() {
    return this.strV + "  " + this.intV;
  }
}
