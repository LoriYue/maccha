package org.maccha.base.exception;

public class BizConfirmException extends BizException {
	
	public BizConfirmException(String message, Throwable t) {
		super(message, t);
	}
	
	public BizConfirmException(Throwable t) {
		super(t);
	}

	public BizConfirmException(String key, Object[] args, String defaultMessage, Throwable nested) {
		super(key, args, defaultMessage, nested);
	}
	/**
	 * 抛出BizConfirmException类型的异常
	 * @param key 异常信息的key
	 * @param t CauseBY 异常
	 */
	public static void handleException(String key, Throwable t) {
		throw new BizConfirmException(key, null, null, t);
	}
	/**
     * 抛出BizConfirmException类型的异常
	 * @param key 异常信息的key
     */
	public static void handleException(String key) {
		throw new BizConfirmException(key, null, null, null);
	}
	/**
     * 抛出BizConfirmException类型的异常
     * @param message 异常信息
     * @param t CauseBY 异常
     */
	public static void handleMessageException(String message, Throwable t) {
		throw new BizConfirmException(null, null, message, t);
	}
	/**
     * 抛出BizConfirmException类型的异常
     * @param message 异常信息
     */
	public static void handleMessageException(String message) {
		throw new BizConfirmException(null, null, message, null);
	}
	/**
     * 抛出BizConfirmException类型的异常,并打印key和args对应的异常信息
     * @param key 异常信息的key
     * @param args 异常消息参数
     */
	public static void handleException(String key, Object[] args) {
		throw new BizConfirmException(key, args, null, null);
	}
	/**
     * 抛出BizConfirmException类型的异常
     * @param key 异常信息的ke
     * @param defaultMessage 默认异常信息
     * @param t CauseBY 异常
     */
	public static void handleException(String key, String defaultMessage,
			Throwable t) {
		throw new BizConfirmException(key, null, defaultMessage, t);
	}
}
