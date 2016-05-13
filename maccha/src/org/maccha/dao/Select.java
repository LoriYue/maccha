package org.maccha.dao;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import org.maccha.base.exception.SysException;
import org.maccha.base.util.StringUtils;

public class Select {
	private ArrayList selectNames = new ArrayList();
	private ArrayList unSelectNames = new ArrayList();
	private ArrayList distinctSelectNames = new ArrayList();
	private boolean selectAll = false;
	static private final String THIS = "this";
	static public final int ARRAY_MODE = 0;
	static public final int MAP_MODE = 1;
	static public final int CLASSMODEL_MODE = 2;
	/**
	 * 查询模式
	 */
	private int mode = ARRAY_MODE;
	private String[] propertyNames = new String[0];
	private Class classModel = null;
	Select(int _mode) {
		this.mode = _mode;
	}
	/**
	 * 查询结果以数组类型返回
	 * 
	 * @return Select
	 */
	public static Select getArraySelect() {
		Select select = new Select(ARRAY_MODE);
		return select;
	}
	/**
	 * 查询结果以类对象模型返回
	 * 
	 * @return
	 */
	public static Select getClassModelSelect() {
		Select select = new Select(CLASSMODEL_MODE);
		select.selectAll();
		return select;
	}
	/**
	 * 查询结果以Map 类型返回
	 * 
	 * @return Select
	 */
	public static Select getMapSelect() {
		Select select = new Select(MAP_MODE);
		return select;
	}
	/**
	 * 查询所有属性
	 * 
	 * @return
	 */
	public Select selectAll() {
		this.selectAll = true;
		return this;
	}
	/**
	 * 查询除了指定属性以外的所有属性
	 * 
	 * @param selectName
	 * @return
	 */
	public Select unSelect(String selectName) {
		this.selectAll();
		this.unSelectNames.add(selectName);
		return this;
	}
	/**
	 * 查询除了指定属性以外的所有属性
	 * 
	 * @param selectNames
	 * @return
	 */
	public Select unSelect(String[] selectNames) {
		this.selectAll();
		for (int i = 0; selectNames != null && i < selectNames.length; i++)
			this.unSelectNames.add(selectNames[i]);
		return this;
	}
	/**
	 * 查询除了指定属性
	 * 
	 * @param selectName
	 * @return
	 */
	public Select select(String selectName) {

		this.selectNames.add(selectName);
		return this;
	}
	/**
	 * 查询除了指定属性
	 * 
	 * @param selectNames
	 * @return
	 */
	public Select select(String[] selectNames) {
		for (int i = 0; selectNames != null && i < selectNames.length; i++)
			this.selectNames.add(selectNames[i]);
		return this;
	}
	/**
	 * 去除属性的重复值
	 * 
	 * @param selectName
	 * @return
	 */
	public Select distinct(String selectName) {
		if (!this.distinctSelectNames.contains(selectName))
			this.distinctSelectNames.add(selectName);
		return this;
	}
	private ArrayList getSelectNames() {
		return this.selectNames;
	}
	private ArrayList getUnSelectNames() {
		return this.unSelectNames;
	}
	private ArrayList getDistinctSelectNames() {
		return this.distinctSelectNames;
	}
	private boolean isSelectAll() {
		return this.selectAll;
	}
	// 当前对象名
	private final String THIS_SELECTNAME = "this";
	/**
	 * 根据属性生成别名
	 * 
	 * @param selectName
	 * @return
	 */
	private String genSelectAlias(String selectName) {
		if(selectName == null)return selectName ;
		String selectAlias ="" ;
		selectAlias = selectName.replaceAll("\\.", "__");
		return selectAlias;
	}
	/**
	 * 格式化查询属性
	 * 
	 * @param selectName
	 * @param alias
	 * @param _type 不带别名 0 带别名
	 * @return
	 */
	private String formatSelectName(String selectName, String selectAlias,
			String ObjectAlias, int _type) {
		// 不包括特殊处理符号
		if (selectName.indexOf("(") <= -1 && selectName.indexOf(THIS_SELECTNAME) <= -1) {
			if (_type == 1)
				selectName = ObjectAlias + "." + selectName;
			else
				selectName = ObjectAlias + "." + selectName + " as " + selectAlias;
			//log.info(selectName + " 不包括特殊处理符号..............");
			return selectName;
		}

		// 查询属性是否带有函数运算,包含"()",不包含this字符串
		// 别名替换this字符串
		//log.info(selectName + " 别名替换..............");
		selectName = selectName.replaceAll(THIS_SELECTNAME, ObjectAlias);
		//selectName = selectName + " as " + selectAlias;
		return selectName;
	}
	/**
	 * 设置查询的起始类模型
	 * 
	 * @param _classModel
	 */
	public void setClassModel(Class _classModel) {
		classModel = _classModel;
	}
	public String getTotalSelectExpr(String alias) {
		String selectStr = "";
		Object[] distinctSelectNames = null;
		try {
			distinctSelectNames = getDistinctSelectNames().toArray();
		} catch (Exception e) {
			distinctSelectNames = null;
		}
		if (distinctSelectNames == null || distinctSelectNames.length <= 0)
			return selectStr;
		for (int i = 0; distinctSelectNames != null && i < distinctSelectNames.length; i++) {
			String selectAlias = genSelectAlias((String) distinctSelectNames[i]);
			if(selectAlias == null)continue ;
			distinctSelectNames[i] = "distinct " + formatSelectName((String) distinctSelectNames[i],selectAlias, alias, 1);
			if (selectStr.trim().length() == 0)
				selectStr += distinctSelectNames[i];
			else
				selectStr += "," + distinctSelectNames[i];
		}
		if (selectStr.trim().length() > 0)
			selectStr = "select count(" + selectStr + ")";
		return selectStr;
	}

	private Object[] getFinalSelectNames() {
		Object[] finalSelectNames = null;
		Object[] distinctSelectNames = null;
		ArrayList _unSelectNames = this.getUnSelectNames();
		ArrayList _selectNames = this.getSelectNames();
		ArrayList _distinctSelectNames = this.getDistinctSelectNames();
		ArrayList _allSelectNames = new ArrayList();
		if (classModel != null && selectAll) {
			Method[] methods = classModel.getMethods();
			for (int i = 0; methods != null && i < methods.length; i++) {
				String fieldName = methods[i].getName();
				if (fieldName.indexOf("get") != 0)
					continue;
				if ("getClass".equals(fieldName))
					continue;
				if (Set.class.getName().equals(
						methods[i].getReturnType().getName()))
					continue;
				fieldName = fieldName.replaceAll("get", "");
				fieldName = StringUtils.uncapitalizeFirst(fieldName);
				_allSelectNames.add(fieldName);
			}
			_selectNames = _allSelectNames;
		}
		finalSelectNames = _selectNames.toArray();
		for (int j = 0; finalSelectNames != null && j < finalSelectNames.length; j++) {
			Object _selectName = finalSelectNames[j];

			if (_unSelectNames != null && _unSelectNames.contains(_selectName))
				_selectNames.remove(_selectName);

			if (_distinctSelectNames != null
					&& _distinctSelectNames.contains(_selectName))
				_selectNames.remove(_selectName);
		}
		finalSelectNames = _selectNames.toArray();
		return finalSelectNames;
	}

	/**
	 * 获得查询模式
	 * 
	 * @return
	 */
	public int getMode() {
		return this.mode;
	}
	/**
	 * 获得查询属性数组
	 * 
	 * @return
	 */
	public String[] getPropertyNames() {
		return this.propertyNames;
	}
	public Class[] getPropertyTypes() {
		String[] fieldNames = getPropertyNames();
		Class[] types = new Class[fieldNames.length];
		Method[] methods = this.classModel.getMethods();
		for (int k = 0; k < fieldNames.length; k++) {
			String _methodName = "get" + fieldNames[k];
			for (int i = 0; i < methods.length; i++) {
				String methodName = methods[i].getName();
				if (_methodName.equalsIgnoreCase(methodName))
					types[k] = methods[i].getReturnType();
			}
		}
		return types;
	}
	/**
	 * 获得select部分的表达式
	 * 
	 * @param alias
	 * @return
	 */
	public String getSelectExpr(String alias) {
		Object[] finalSelectNames = null;
		Object[] distinctSelectNames = null;
		ArrayList _distinctSelectNames = this.getDistinctSelectNames();
		finalSelectNames = getFinalSelectNames();
		if (_distinctSelectNames == null)
			_distinctSelectNames = new ArrayList();
		distinctSelectNames = _distinctSelectNames.toArray();
		if (finalSelectNames == null || finalSelectNames.length <= 0) {
			SysException.handleException("查询属性不能为空,selectNames="+ finalSelectNames);
		}
		String selectStr = "";
		// 先加入distinct查询属性
		propertyNames = new String[distinctSelectNames.length + finalSelectNames.length];
		int n = 0;
		for (int i = 0; i < distinctSelectNames.length; i++) {
			propertyNames[n] = this.genSelectAlias((String) distinctSelectNames[i]);
			if(((String) distinctSelectNames[i]).indexOf(" as ") != -1 || ((String) distinctSelectNames[i]).indexOf(" AS ") != -1)
				distinctSelectNames[i] = "distinct " + formatSelectName((String) distinctSelectNames[i], propertyNames[n], alias, 1) + " ";
			else
				distinctSelectNames[i] = "distinct " + formatSelectName((String) distinctSelectNames[i], propertyNames[n], alias, 0) + " ";
			if (selectStr.trim().length() == 0)
				selectStr += distinctSelectNames[i];
			else
				selectStr += "," + distinctSelectNames[i];
			n++;
		}
		for (int i = 0; i < finalSelectNames.length; i++) {
			propertyNames[n] = this.genSelectAlias((String) finalSelectNames[i]);
			if(((String) finalSelectNames[i]).indexOf(" as ") != -1 || ((String) finalSelectNames[i]).indexOf(" AS ") != -1){
				finalSelectNames[i] = formatSelectName((String) finalSelectNames[i], propertyNames[n], alias, 1);
			}else{
				finalSelectNames[i] = formatSelectName((String) finalSelectNames[i], propertyNames[n], alias, 0);
			}
			if (selectStr.trim().length() == 0)
				selectStr += finalSelectNames[i];
			else
				selectStr += "," + finalSelectNames[i];
			n++;
		}
		if (this.mode == Select.MAP_MODE)
			selectStr = "new map( " + selectStr + " )";
		if (selectStr.trim().length() != 0)
			selectStr = "select " + selectStr;
		return selectStr;
	}
}
