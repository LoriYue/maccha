package org.maccha.dao;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import org.maccha.base.exception.SysException;
import org.maccha.base.util.StringUtils;

public class Select
{
  private ArrayList selectNames = new ArrayList();

  private ArrayList unSelectNames = new ArrayList();

  private ArrayList distinctSelectNames = new ArrayList();

  private boolean selectAll = false;
  private static final String THIS = "this";
  public static final int ARRAY_MODE = 0;
  public static final int MAP_MODE = 1;
  public static final int CLASSMODEL_MODE = 2;
  private int mode = 0;

  private String[] propertyNames = new String[0];

  private Class classModel = null;

  private final String THIS_SELECTNAME = "this";

  Select(int _mode)
  {
    this.mode = _mode;
  }

  public static Select getArraySelect()
  {
    Select select = new Select(0);
    return select;
  }

  public static Select getClassModelSelect()
  {
    Select select = new Select(2);
    select.selectAll();
    return select;
  }

  public static Select getMapSelect()
  {
    Select select = new Select(1);
    return select;
  }

  public Select selectAll()
  {
    this.selectAll = true;
    return this;
  }

  public Select unSelect(String selectName)
  {
    selectAll();
    this.unSelectNames.add(selectName);
    return this;
  }

  public Select unSelect(String[] selectNames)
  {
    selectAll();
    for (int i = 0; (selectNames != null) && (i < selectNames.length); i++)
      this.unSelectNames.add(selectNames[i]);
    return this;
  }

  public Select select(String selectName)
  {
    this.selectNames.add(selectName);
    return this;
  }

  public Select select(String[] selectNames)
  {
    for (int i = 0; (selectNames != null) && (i < selectNames.length); i++)
      this.selectNames.add(selectNames[i]);
    return this;
  }

  public Select distinct(String selectName)
  {
    if (!this.distinctSelectNames.contains(selectName)) {
      this.distinctSelectNames.add(selectName);
    }
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

  private String genSelectAlias(String selectName)
  {
    if (selectName == null) return selectName;
    String selectAlias = "";

    selectAlias = selectName.replaceAll("\\.", "__");
    return selectAlias;
  }

  private String formatSelectName(String selectName, String selectAlias, String ObjectAlias, int _type)
  {
    if ((selectName.indexOf("(") <= -1) && (selectName.indexOf("this") <= -1))
    {
      if (_type == 1)
        selectName = ObjectAlias + "." + selectName;
      else {
        selectName = ObjectAlias + "." + selectName + " as " + selectAlias;
      }

      return selectName;
    }

    selectName = selectName.replaceAll("this", ObjectAlias);

    return selectName;
  }

  public void setClassModel(Class _classModel)
  {
    this.classModel = _classModel;
  }

  public String getTotalSelectExpr(String alias) {
    String selectStr = "";
    Object[] distinctSelectNames = null;
    try
    {
      distinctSelectNames = getDistinctSelectNames().toArray();
    } catch (Exception e) {
      distinctSelectNames = null;
    }

    if ((distinctSelectNames == null) || (distinctSelectNames.length <= 0)) {
      return selectStr;
    }
    for (int i = 0; (distinctSelectNames != null) && (i < distinctSelectNames.length); i++) {
      String selectAlias = genSelectAlias((String)distinctSelectNames[i]);
      if (selectAlias == null)
        continue;
      distinctSelectNames[i] = ("distinct " + formatSelectName((String)distinctSelectNames[i], selectAlias, alias, 1));

      if (selectStr.trim().length() == 0)
        selectStr = selectStr + distinctSelectNames[i];
      else
        selectStr = selectStr + "," + distinctSelectNames[i];
    }
    if (selectStr.trim().length() > 0) {
      selectStr = "select count(" + selectStr + ")";
    }
    return selectStr;
  }

  private Object[] getFinalSelectNames()
  {
    Object[] finalSelectNames = null;
    Object[] distinctSelectNames = null;
    ArrayList _unSelectNames = getUnSelectNames();
    ArrayList _selectNames = getSelectNames();
    ArrayList _distinctSelectNames = getDistinctSelectNames();
    ArrayList _allSelectNames = new ArrayList();
    if ((this.classModel != null) && (this.selectAll)) {
      Method[] methods = this.classModel.getMethods();
      for (int i = 0; (methods != null) && (i < methods.length); i++) {
        String fieldName = methods[i].getName();
        if (fieldName.indexOf("get") != 0)
          continue;
        if ("getClass".equals(fieldName))
          continue;
        if (Set.class.getName().equals(methods[i].getReturnType().getName())) {
          continue;
        }
        fieldName = fieldName.replaceAll("get", "");
        fieldName = StringUtils.uncapitalizeFirst(fieldName);
        _allSelectNames.add(fieldName);
      }
      _selectNames = _allSelectNames;
    }

    finalSelectNames = _selectNames.toArray();
    for (int j = 0; (finalSelectNames != null) && (j < finalSelectNames.length); j++) {
      Object _selectName = finalSelectNames[j];

      if ((_unSelectNames != null) && (_unSelectNames.contains(_selectName))) {
        _selectNames.remove(_selectName);
      }
      if ((_distinctSelectNames == null) || (!_distinctSelectNames.contains(_selectName)))
        continue;
      _selectNames.remove(_selectName);
    }
    finalSelectNames = _selectNames.toArray();
    return finalSelectNames;
  }

  public int getMode()
  {
    return this.mode;
  }

  public String[] getPropertyNames()
  {
    return this.propertyNames;
  }

  public Class[] getPropertyTypes()
  {
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

  public String getSelectExpr(String alias)
  {
    Object[] finalSelectNames = null;
    Object[] distinctSelectNames = null;
    ArrayList _distinctSelectNames = getDistinctSelectNames();

    finalSelectNames = getFinalSelectNames();

    if (_distinctSelectNames == null) {
      _distinctSelectNames = new ArrayList();
    }
    distinctSelectNames = _distinctSelectNames.toArray();

    if ((finalSelectNames == null) || (finalSelectNames.length <= 0)) {
      SysException.handleException("查询属性不能为空,selectNames=" + finalSelectNames);
    }

    String selectStr = "";

    this.propertyNames = new String[distinctSelectNames.length + finalSelectNames.length];

    int n = 0;
    for (int i = 0; i < distinctSelectNames.length; i++)
    {
      this.propertyNames[n] = genSelectAlias((String)distinctSelectNames[i]);
      if ((((String)distinctSelectNames[i]).indexOf(" as ") != -1) || (((String)distinctSelectNames[i]).indexOf(" AS ") != -1))
        distinctSelectNames[i] = ("distinct " + formatSelectName((String)distinctSelectNames[i], this.propertyNames[n], alias, 1) + " ");
      else {
        distinctSelectNames[i] = ("distinct " + formatSelectName((String)distinctSelectNames[i], this.propertyNames[n], alias, 0) + " ");
      }

      if (selectStr.trim().length() == 0)
        selectStr = selectStr + distinctSelectNames[i];
      else {
        selectStr = selectStr + "," + distinctSelectNames[i];
      }
      n++;
    }

    for (int i = 0; i < finalSelectNames.length; i++)
    {
      this.propertyNames[n] = genSelectAlias((String)finalSelectNames[i]);

      if ((((String)finalSelectNames[i]).indexOf(" as ") != -1) || (((String)finalSelectNames[i]).indexOf(" AS ") != -1))
        finalSelectNames[i] = formatSelectName((String)finalSelectNames[i], this.propertyNames[n], alias, 1);
      else {
        finalSelectNames[i] = formatSelectName((String)finalSelectNames[i], this.propertyNames[n], alias, 0);
      }

      if (selectStr.trim().length() == 0)
        selectStr = selectStr + finalSelectNames[i];
      else
        selectStr = selectStr + "," + finalSelectNames[i];
      n++;
    }

    if (this.mode == 1) {
      selectStr = "new map( " + selectStr + " )";
    }
    if (selectStr.trim().length() != 0) {
      selectStr = "select " + selectStr;
    }
    return selectStr;
  }
}
