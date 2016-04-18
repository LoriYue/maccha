package org.maccha.httpservice.action;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.beanutils.PropertyUtils;
import org.maccha.dao.service.IBaseBizService;
import org.maccha.spring.SpringManager;
import org.maccha.base.util.DateUtils;
import org.maccha.base.util.ObjectUtils;
import org.maccha.base.util.ParameterProcessor;
import org.maccha.httpservice.DataMessage;
import org.maccha.httpservice.DataSet;
import org.maccha.httpservice.Entity;
import org.maccha.httpservice.IWebServiceAction;

public abstract class BaseWebServiceAction
  implements IWebServiceAction
{
  public static final String SPLIT = "__";
  private HashMap _rowstate = new HashMap();

  public abstract void execute(DataMessage paramDataMessage1, DataMessage paramDataMessage2);

  public Object getModel(Class bean, Map paramMap, String split, ParameterProcessor parameterProcessor)
  {
    return ObjectUtils.map2PO(bean, paramMap, split, parameterProcessor);
  }

  public Object getModel(Class bean, Map paramMap, ParameterProcessor parameterProcessor)
  {
    return ObjectUtils.map2PO(bean, paramMap, "__", parameterProcessor);
  }

  public Object getModelNoSplit(Class bean, Map paramMap, ParameterProcessor parameterProcessor)
  {
    return ObjectUtils.map2PO(bean, paramMap, null, parameterProcessor);
  }

  public Object getModelNoSplit(Class bean, Map paramMap)
  {
    return getModelNoSplit(bean, paramMap, null);
  }

  public Object getModel(Class bean, Map paramMap)
  {
    return getModel(bean, paramMap, null);
  }

  public Set getModelSet(Class bean, DataSet datasets)
  {
    return getModelSet(bean, datasets, null, null);
  }
  public Set getModelSet(Class bean, DataSet datasets, String rowState) {
    return getModelSet(bean, datasets, rowState, null);
  }

  public Set getModelSet(Class bean, DataSet datasets, ParameterProcessor parameterProcessor)
  {
    return getModelSet(bean, datasets, null, parameterProcessor);
  }

  public Set getModelSet(Class bean, DataSet datasets, String editState, ParameterProcessor parameterProcessor)
  {
    HashSet hashObj = new HashSet();
    if (datasets == null) return hashObj;
    int intCount = datasets.getEntityCount();
    Object _id = null;
    for (int i = 0; i < intCount; i++)
      if ((editState == null) || (datasets.getEntity(i).getEditState().equals(editState))) {
        Object obj = getModel(bean, datasets.getEntity(i), parameterProcessor);
        if (obj != null) {
          hashObj.add(obj);
          try {
            _id = PropertyUtils.getProperty(obj, "id");
            this._rowstate.put(_id, datasets.getEntity(i).getEditState());
          } catch (Exception ex) {
          }
        }
      }
    return hashObj;
  }
  public Object getModel(Class objCls, Entity entity) {
    return getModel(objCls, entity, null);
  }

  public Object getModel(Class objCls, Entity entity, ParameterProcessor parameterProcessor)
  {
    return ObjectUtils.map2PO(objCls, entity.getPropertyMap(), null, parameterProcessor);
  }
  public HashMap getRowState() {
    return this._rowstate;
  }

  public String formatDate(Date date)
  {
    return DateUtils.format(date, "yyyy.MM.dd");
  }

  protected IBaseBizService getBaseBizService()
  {
    return (IBaseBizService)SpringManager.getComponent(IBaseBizService.BASEBIZSERVICE);
  }
}
