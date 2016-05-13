package org.maccha.httpservice.util;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import java.sql.Clob;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.maccha.base.util.ClassUtils;
import org.maccha.base.util.DateUtils;
import org.maccha.base.util.NumberFormatUtils;
import org.maccha.base.util.ObjectUtils;
import org.maccha.base.util.TypeConvertorUtils;

public class JsonUtils {
	private static Gson _gson = new Gson();
	/**
	 * 将entity对象数据解析到Entity对象
	 * @param parseEntity2Json
	 * @return Entity
	 * @throws Exception
	 */	
	public static String parseEntity2Json(Object _entity){
		List _FieldList = ObjectUtils.getFields(_entity);
		StringBuffer _strBuf = new StringBuffer("{");
		for (int i = 0; i < _FieldList.size(); i++) {
			String _propertyName = (String)_FieldList.get(i);
			if ("class".equals(_propertyName))continue;
			Object _valueObj = ClassUtils.get(_entity, _propertyName);
			if (_strBuf.length() > 1)_strBuf.append(',');
			if (_valueObj == null) {
				_strBuf.append(JsonUtils.jsonNode(_propertyName, ""));
			}else if(_valueObj instanceof String[]){
				String[] _strArrayValue = (String[])_valueObj ;
				if(_strArrayValue.length >0)
				_strBuf.append(JsonUtils.jsonNode(_propertyName,Joiner.on(",").join(_strArrayValue)));
				else _strBuf.append(JsonUtils.jsonNode(_propertyName, ""));
			}else{
				_strBuf.append(JsonUtils.jsonNode(_propertyName,_valueObj));
			}
		}
		_strBuf.append("}");
		
		return _strBuf.toString();			
	}
	
	public static String parseMap2Json(Map _propertyMap) {
		Iterator _keys = _propertyMap.keySet().iterator();
		StringBuffer _strBuf = new StringBuffer("{");
		while (_keys.hasNext()) {
			Object _key = _keys.next();
			Object _valueObj = _propertyMap.get(_key);
			if (_strBuf.length() > 1)
				_strBuf.append(',');
			if (_valueObj == null) {
				_strBuf.append(JsonUtils.jsonNode(_key.toString(), ""));
			} else if (_valueObj instanceof String[]) {
				String[] _strArrayValue = (String[]) _valueObj;
				if (_strArrayValue.length > 0)
					_strBuf.append(JsonUtils.jsonNode(_key.toString(), Joiner.on(",").join(_strArrayValue)));
				else
					_strBuf.append(JsonUtils.jsonNode(_key.toString(), ""));
			} else {
				_strBuf.append(JsonUtils.jsonNode(_key.toString(), _valueObj));
			}
		}
		_strBuf.append("}");
		return _strBuf.toString();
	}
	public static String jsonNode(String _name, Object _vlaue) {
		StringBuffer _strBuf = new StringBuffer();
		if(_vlaue instanceof Double){
			_vlaue = NumberFormatUtils.formatNumber((Double) _vlaue, "##.#####");
        }else if(_vlaue instanceof Float){
        	_vlaue = NumberFormatUtils.formatNumber((Float)_vlaue,"##.#####");
        }else if(_vlaue instanceof Date){
        	_vlaue = DateUtils.format((Date)_vlaue,DateUtils.YMDHMS_PATTERN);
        }else if (_vlaue instanceof Clob) {
            Clob _obj1= (Clob)_vlaue ;
            _vlaue = (String)TypeConvertorUtils.convert(_obj1,Clob.class, String.class);
        }else if (_vlaue instanceof java.sql.Timestamp) {
        	Object _obj = TypeConvertorUtils.convert(_vlaue,java.util.Date.class);
        	_vlaue = DateUtils.format((Date)_obj,DateUtils.YMDHMS_PATTERN);
        }else{
        	_vlaue = (_vlaue == null?"":_vlaue.toString());
        }
		_strBuf.append(_gson.toJson(_name));
		_strBuf.append(':');
		_strBuf.append(_gson.toJson(_vlaue));
		return _strBuf.toString();
	}
}
