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

public abstract class BaseWebServiceAction implements IWebServiceAction {
	public static final String SPLIT = "__";
	private HashMap _rowstate = new HashMap();
	
	public abstract void execute(DataMessage request, DataMessage response);
	
	/**
	 * 将Map数据设置到指定Bean对象中,规则:
	 *  1. 当SPLIT_DELIM 为null时 ,方法会从 Map 中的取出和PO属性值相同 的 key 对应的值，设置到对象属性中
	 *  2. 当SPLIT_DELIM 不为null时 ,例如：bean.name ,bean 为PO类的最后部分第一个字母小写[如java.util.Iterator中的'iterator']，name:为Bean的属性名称
	 * @param bean PO 类
	 * @param paramMap 参数键值对
	 * @param parameterProcessor 参数处理接口
	 * @return PO对象
	 */
	public Object getModel(Class bean,Map paramMap,String split,ParameterProcessor parameterProcessor) {
		return ObjectUtils.map2PO(bean, paramMap, split, parameterProcessor);
	}	
	/**
	 * 将Map数据设置到指定Bean对象中,规则:
	 *  1. 当SPLIT_DELIM 为null时 ,方法会从 Map 中的取出和PO属性值相同 的 key 对应的值，设置到对象属性中
	 *  2. 当SPLIT_DELIM 不为null时 ,例如：bean.name ,bean 为PO类的最后部分第一个字母小写[如java.util.Iterator中的'iterator']，name:为Bean的属性名称
	 * @param bean PO 类
	 * @param paramMap 参数键值对
	 * @param parameterProcessor 参数处理接口
	 * @return PO对象
	 */
	public Object getModel(Class bean,Map paramMap,ParameterProcessor parameterProcessor) {
		return ObjectUtils.map2PO(bean, paramMap, SPLIT, parameterProcessor);		
	}
	/**
	 * 将Map数据设置到指定Bean对象中,规则:
	 * @param bean PO 类
	 * @param paramMap 参数键值对
	 * @param parameterProcessor 参数处理接口
	 * @return PO对象
	 */
	public Object getModelNoSplit(Class bean,Map paramMap,ParameterProcessor parameterProcessor) {
		return ObjectUtils.map2PO(bean, paramMap, null, parameterProcessor);		
	}	
	/**
	 * 处理前台参数，并将参数转化为Bean对象列表
	 */
	public Object getModelNoSplit(Class bean,Map paramMap) {
		return getModelNoSplit(bean,paramMap,null) ;
	}	
	/**
	 * 处理前台参数，并将参数转化为Bean对象列表
	 */
	public Object getModel(Class bean,Map paramMap) {
		return getModel(bean,paramMap,null) ;
	}

	/**
	 * Entity 对象转换po对象
	 * @param entity
	 * @param objCls
	 * @return
	 */
	public Set getModelSet(Class bean,DataSet datasets) {
		return this.getModelSet(bean,datasets,null,null) ;
	}
	public Set getModelSet(Class bean,DataSet datasets,String rowState) {
		return this.getModelSet(bean,datasets,rowState,null) ;
	}	
	/**
	 * Entity 对象转换po对象
	 * @param entity
	 * @param objCls
	 * @return
	 */
	public Set getModelSet(Class bean,DataSet datasets,ParameterProcessor parameterProcessor) {
		return this.getModelSet(bean,datasets,null,parameterProcessor) ;
	}	
	/**
	 * Entity 对象转换po对象
	 * @param entity
	 * @param objCls
	 * @return
	 */
	public Set getModelSet(Class bean,DataSet datasets,String editState,ParameterProcessor parameterProcessor) {
		HashSet hashObj = new HashSet();
		if(datasets == null) return hashObj ;
		int intCount = datasets.getEntityCount();
		Object _id = null ;
		for(int i = 0 ; i < intCount ;i++){
			if(editState == null || datasets.getEntity(i).getEditState().equals(editState)){
				Object obj = this.getModel(bean,datasets.getEntity(i),parameterProcessor);
				if(obj != null){
					hashObj.add(obj);
					try{
						_id = PropertyUtils.getProperty(obj,"id");
						_rowstate.put(_id, datasets.getEntity(i).getEditState());
					}catch(Exception ex){}
				}
			}			
		}
		return hashObj;
	}
	public Object getModel(Class objCls,Entity entity) {
		return this.getModel(objCls, entity, null);
	}
	/**
	 * 处理前台参数，并将参数转化为Bean对象列表
	 */
	public Object getModel(Class objCls,Entity entity,ParameterProcessor parameterProcessor) {
		return ObjectUtils.map2PO(objCls,entity.getPropertyMap(),null,parameterProcessor) ;
	}
	public HashMap getRowState(){
		return this._rowstate ;
	}
	/**
	 * 转换日期格式为 Y.M.D
	 * @param date
	 * @return
	 */
	public String formatDate(Date date) {
		return DateUtils.format(date, DateUtils.YEAR_MONTH_DAY_PATTERN_DOT);
	}
	
	/**
	 * 获得基础业务服务实例
	 * 
	 * @return
	 */
	protected IBaseBizService getBaseBizService() {
		return (IBaseBizService) SpringManager
				.getComponent(IBaseBizService.BASEBIZSERVICE);
	}
}
