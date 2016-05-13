package org.maccha.dao.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceCallMonitorAdvisor implements MethodInterceptor{
	private static Logger log = LoggerFactory.getLogger(ServiceCallMonitorAdvisor.class);
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object obj = null;
        log.info("..进入到了Service方法拦截器。。。。");
        log.info("..调用的service：");
        log.info(invocation.getThis()==null?"":invocation.getThis().toString());
        log.info("..调用的方法:");
        log.info(invocation.getMethod()==null?"":invocation.getMethod().toString());
        log.info("..参数是：");
        for (int i = 0;i < invocation.getArguments().length;i++){
            Object[] objs = invocation.getArguments();
            log.info(objs[i]==null?"":objs[i].toString());
        }
        obj = invocation.proceed();
        log.info("..返回结果是:");
        log.info(obj==null?"":obj.toString());
        log.info("..拦截器执行结束!!");
        return obj;
    }
}