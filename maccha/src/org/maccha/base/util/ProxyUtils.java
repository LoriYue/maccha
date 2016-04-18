package org.maccha.base.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

public class ProxyUtils implements InvocationHandler {

	private Map map;
	private Class interface_cls;

	private ProxyUtils(Class cls, Map data) {
		this.map = data;
		this.interface_cls = cls;
	}
	/**
	 * 获取代理对象
	 * @param cls 要代理的接口类型
	 * @param data 参数键值对
	 * @return
	 */
	public static Object getObject(Class cls, Map data) {
		ProxyUtils proxyUtils = new ProxyUtils(cls, data);
		Class[] interfaces = { cls };
		ClassLoader classLoader = ProxyUtils.class.getClassLoader();
		return Proxy.newProxyInstance(classLoader, interfaces, proxyUtils);
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String methodName = method.getName();
		if (methodName.startsWith("get")) {
			String name = methodName.substring(methodName.indexOf("get") + 3);
			Object value = this.map.get(name);
			if (value == null) {
				if (method.getReturnType().equals(Boolean.TYPE))
					return new Boolean(false);
				if (method.getReturnType().equals(Integer.TYPE))
					return new Integer(0);
				if (method.getReturnType().equals(Long.TYPE))
					return new Long(0L);
				if (method.getReturnType().equals(Float.TYPE))
					return new Float(0.0F);
				if (method.getReturnType().equals(Double.TYPE)) {
					return new Double(0.0D);
				}
			}
			return value;
		}
		if (methodName.startsWith("set")) {
			String name = methodName.substring(methodName.indexOf("set") + 3);
			if (args[0] != null)
				this.map.put(name, args[0]);
			return null;
		}
		if ("toString".equals(methodName)) {
			return this.map.toString();
		}
		if ("hashCode".equals(methodName))
			return new Integer(this.map.hashCode());
		return null;
	}

}
