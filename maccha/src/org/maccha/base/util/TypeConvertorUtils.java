package org.maccha.base.util;

import org.maccha.base.exception.SysException;
import org.maccha.base.util.convertor.ConvertorTypesUtility;
import org.maccha.base.util.convertor.TypeConvertor;

public class TypeConvertorUtils {
    public TypeConvertorUtils() {
    }
    
    public static Object convert( Object obj,Class fromType,Class toType){
        Object objReturn = null ;
        if(obj == null||toType == null) return null ;
        try{
            if(!toType.equals(obj.getClass())){
                TypeConvertor typeConvertor = ConvertorTypesUtility.getConvertor(fromType ,toType) ;
                objReturn = typeConvertor.convert(obj,null) ;
                return objReturn;
            }else{
                return obj ;
            }
        }catch(Exception ex){
        	SysException.handleMessageException(ex.getMessage());
            ex.printStackTrace();
        }
        if(objReturn == null)
            objReturn =  obj ;
        return objReturn ;
    }
    public static Object convert( Object obj,Class toType){
        Object objReturn = null ;
        if(obj == null||toType == null) return null ;
        try{
            if(!toType.equals(obj.getClass())){
                TypeConvertor typeConvertor = ConvertorTypesUtility.getConvertor(obj.getClass() ,toType) ;
                objReturn = typeConvertor.convert(obj,null) ;
                return objReturn;
            }else{
            	return obj ;
            }           
        }catch(Exception ex){
        	SysException.handleMessageException(ex.getMessage());
            ex.printStackTrace();
        }
        if(objReturn == null)
            objReturn =  obj ;
        return objReturn ;
    }
    public static Object convert( Object obj,Class toType,String param){
        Object objReturn = null ;
        if(obj == null||toType == null) return null ;
        try{
            if(!toType.equals(obj.getClass())){
                TypeConvertor typeConvertor = ConvertorTypesUtility.getConvertor(obj.getClass() ,toType) ;
                objReturn = typeConvertor.convert(obj,param) ;
                return objReturn;
            }else{
            	return obj ;
            }           
        }catch(Exception ex){
        	SysException.handleMessageException(ex.getMessage());
            ex.printStackTrace();
        }
        if(objReturn == null)objReturn =  obj ;
        return objReturn ;
    }    
    public static Class getType (Class bean ,String field){    	
    	try{
    		//org.apache.commons.lang
    		String strMethodName = "get"+StringUtils.capitalizeFirst(field);
    		return bean.getMethod(strMethodName, new Class[] {}).getReturnType();
    	}catch(Exception ex){    		
    		//SysException.handleMessageException(ex.getMessage());
    	}
    	return null ;
    }

    private String aa ;
    public String getAa(){
    	return null ;
    }
}
