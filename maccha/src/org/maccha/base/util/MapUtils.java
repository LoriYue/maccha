package org.maccha.base.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
/**
 * <p>Title: MapUtils</p>
 * <p>Description: map通用工具类</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: 北京东方信软信息技术有限公司</p>
 * @author not attributable
 * @version 1.0
 */

public class MapUtils{

    private Map map = new HashMap();
    public MapUtils(Map map){
        this.map = map;
    }

    /**
     * 返回参数值
     * @param name 参数名字
     * @return     int
     */
    public int getInt(String name) throws Exception{
        return Integer.parseInt(getString(name));
    }

    /**
     * 返回参数值
     * @param name 参数名字
     * @return     long
     */
    public long getLong(String name) throws Exception{
        return Long.parseLong(this.getString(name));
    }

    /**
     * 返回参数值
     * @param name 参数名字
     * @return     String
     */
    public String getString(String name){
        String tmpstr = (String)map.get(name);
        if(tmpstr == null){
            tmpstr = "";
        }
        return tmpstr.trim();
    }

    /**
     * 设置字符串
     * @param name  名字
     * @param value 值
     */
    public void setString(String name,String value){
        if(value == null || "null".equals(value)){
            value = "";
        }
        this.setObject(name,value);
    }

    /**
     * 设置对象
     * @param name  名字
     * @param value 值
     */
    public void setObject(String name,Object obj){
        if(name == null || obj == null){
            return;
        }
        map.put(name,obj);
    }

    /**
     * 返回对象参数值
     * @param name 参数名字
     * @return     Object
     */
    public Object getObject(String name){
        return map.get(name);
    }

    /**
     * 返回参数值
     * @param name 参数名字
     * @return     boolean
     */
    public boolean getBoolean(String name){
        return Boolean.getBoolean(this.getString(name));
    }

    /**
     * 获得所有的键名数组
     * @return
     */
    public Object[] getNames(){
        return this.map.keySet().toArray();
    }

    /**
     * 获得所有的值数组
     * @return
     */
    public Object[] getValues(){
        return this.map.values().toArray();
    }

    public static Object MapToModel(Class cls,Map _map) {
      return ProxyUtils.getObject(cls, _map);
    }

    public static Map ModelToMap(Object obj) {
      Map _map=new Hashtable();
      return _map;
      //return ProxyUtils.getObject(cls, this.map);
    }

    public static String MapToXml(Map _map) {
      String xml="";
      return xml;
      //return ProxyUtils.getObject(cls, this.map);
    }

    public static Map XmlToMap(String xml) {
      Map _map=new Hashtable();
      return _map;
      //return ProxyUtils.getObject(cls, this.map);
    }

    /**
     Map到Map数组（Map[]）
     **/
    public static Map[] toMapArray(Map map){

        Map[] mapArray = new Hashtable[map.size()];
        Object[] keyArray = map.keySet().toArray();
        for(int i = 0; keyArray != null && i < keyArray.length; i++){
            Object key = keyArray[i];
            if(key == null){
                continue;
            }
            Object value = map.get(key);
            Hashtable att = new Hashtable();
            att.put("name",key);
            att.put("value",value);
            mapArray[i] = att;
        }
        return mapArray;
    }

    /**
     Map数组（Map[]）到Map
     例如：
     一个Map数组  paramLobj 数据如下，
               regionid="001",regionname="bj",other="xx1";
               regionid="002",regionname="sh",other="xx2";
               regionid="003",regionname="gzh",other="xx3";
               regionid="004",regionname="chd",other="xx4";
               regionid="005",regionname="lzh",other="xx5";
               regionid="006",regionname="tj",other="xx6";
         调用  >>>>>mapArrayToMap(paramLobj,"regionid","regionname")>>>>>> 返回 paramOLobj 数据如下，
                      001="bj",002="sh",003="gzh",004="chd",005="lzh",006="tj"
         调用  >>>>>mapArrayToMap(paramLobj,"regionid","other")>>>>>> 返回 paramOLobj 数据如下，
         001="xx1",002="xx2",003="xx3",004="xx4",005="xx5",006="xx6"
     @param Map[] Map数组数据源
     @param String 键名（来自于Map数据源的某一键名字）
     @param String 键值（来自于Map数据源的某一键名字）
     **/
    public static Map mapArrayToMap(Map[] maps,String name_key,String value_key){
        Hashtable map = new Hashtable();
        Object key = null;
        Object value = null;
        for(int i = 0; maps != null && i < maps.length; i++){
            key = maps[i].get(name_key);
            value = maps[i].get(value_key);
            if(key == null){
                continue;
            }
            if(value == null){
                value = "";
            }
            map.put(key,value);
        }
        maps = null;
        name_key = null;
        value_key = null;
        key = null;
        value = null;
        return map;
    }

    /**
     * 分类存储对象
     * @param name
     * @param value
     */
    public void addObject(Object name,Object value){

        final String PREFIX = "ADDOBJECT000000000";
        name = PREFIX + name;

        if(name == null || value == null){
            return;
        }
        HashSet set = null;
        try{
            set = (HashSet)map.get(name);
        } catch(Exception e){
        }
        if(set == null){
            set = new HashSet();
        }
        set.add(value);
        map.put(name,set);
    }

    /**
     * 获得指定分类的存储对象集合
     * @param name
     * @return
     */
    public Set getSet(Object name){
        final String PREFIX = "ADDOBJECT000000000";
        name = PREFIX + name;

        Object obj = map.get(name);
        if(obj == null){
            obj = new HashSet();
        }
        return(Set)obj;
    }

    /**
     * 按键名分类累加
     * @param name
     * @param val
     */
    public void addNum(String name,String val){
        final String PREFIX = "ADDNUM000000000";
        name = PREFIX + name;

        String str = getString(name);
        if(str != null && str.trim().length() > 0){
            str = MathUtils.sum(str,val);
            setString(name,str);
        } else{
            setString(name,val);
        }
    }

    /**
     * 获得分类累加值
     * @param name
     */
    public String getNum(String name){
        final String PREFIX = "ADDNUM000000000";
        return getString(name);
    }

    /**
     * 分类累计布尔状态
     * @param name
     * @return
     */
    public boolean isHas(String name){
        return isHas("check" + name,"CHACK_FLAG");
    }

    private boolean isHas(String name,String val){
        String str = getString(name);
        if(str != null && str.trim().length() > 0){
            return true;
        } else{
            setString(name,val);
            return false;
        }
    }

    /**
     * 字符串累加
     * @param name
     * @param val
     * @param tag
     */
    public void addString(String name,String val,String tag){
        final String PREFIX = "ADDSTRING000000000";
        name = PREFIX + name;
        String str = getString(name);
        if(str != null && str.trim().length() > 0){
            str = str + tag + val;
            setString(name,str);
        } else{
            setString(name,val);
        }
    }

    /**
     * 指定一个 key,value键 将Map[]数组 变成Map
     * @param props      Map[]数组
     * @param name_key   指定在返回的Map中 作key键   {key=value,key=value}
     * @param value_key  指定在返回的Map中 作value键 {key=value,key=value}
     * @return
     */
    public static Map getMap(Map[] props,String name_key,String value_key){
        Map prop = new Hashtable();
        for(int i = 0; props != null && i < props.length; i++){
            Object key = props[i].get(name_key);
            if(key == null){
                key = props[i].get(name_key.toUpperCase());
            }
            String value = (String)props[i].get(value_key);
            if(value == null){
                value = (String)props[i].get(value_key.toUpperCase());
            }
            if(key == null){
                continue;
            }
            if(value == null){
                value = "";
            }
            prop.put(key,value);
        }
        return prop;
    }

    /**
     *获得指定key键值的数组
     *@param Map[] 数据
     *@param key 要变成数组的键
     *@return String[] 数组
     **/
    public static String[] getStringArray(Map[] tmp,String key){
        String[] list = new String[tmp.length];
        for(int i = 0; i < list.length; i++){
            String value = (String)tmp[i].get(key);
            if(value == null){
                continue;
            }
            list[i] = value;
        }
        return list;
    }
    
    public static  Map toMap(String name,Object value){
		return toMap(new String[]{name},new Object[]{value});
	}
    
    public static  Map toMap(String[] names,Object[] values){
		Map _queryArgMap=new HashMap();
		for(int i=0;names!=null&&i<names.length;i++){
		_queryArgMap.put(names[i], values[i]);
		}
		return _queryArgMap;
	}

}
