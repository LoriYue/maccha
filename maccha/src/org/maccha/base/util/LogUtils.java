package org.maccha.base.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class LogUtils {
	private static final Level INFO = Level.INFO;
	private static final Level DEBUG = Level.DEBUG;
	private static final Level WARN = Level.WARN;
	private static final Level ERROR = Level.ERROR;
	public static Logger logger = Logger.getLogger("org.simpro.util");
	
	/**
	 * 按照日志配置信息写日志
	 *
	 * @param	 logMessage	 日志消息
	 * @param	 errorLevel	 日志级别
	 * @param	 e	         例外
	 */
	private static void printLog(String logMessage, Level errorLevel,Throwable t) {
		try {
			StringBuffer sbLog = new StringBuffer();

			sbLog.append("【线程编号：");
			sbLog.append(Thread.currentThread().getName());
			sbLog.append("】");
			if (errorLevel == null || errorLevel == INFO) {
				errorLevel = INFO;
				sbLog.append("【信息】");
			} else if (errorLevel == DEBUG) {
				sbLog.append("【调试】");
			} else if (errorLevel == WARN) {
				sbLog.append("【警告】");
			} else if (errorLevel == ERROR) {
				sbLog.append("【错误】");
			}
		    sbLog.append(logMessage);
			logger.log(errorLevel, sbLog.toString(), t);
			
		} catch (Exception ex) {
			logger.error("记录日志时发生以下错误：" + ex.getMessage());
		}
	}
	/**
	 * 按照日志配置信息写日志
	 * 
	 * @param logMessage
	 *            日志消息
	 * @param errorLevel
	 *            日志级别
	 */
	private static void printLog(String logMessage, Level errorLevel) {
		printLog(logMessage, errorLevel, null);
	}

	/**
	 * 错误日志
	 * @param object
	 */
	public static void error(Object object) {
		if(object!=null)
		printLog(object.toString(), ERROR);
	}

	/**
	 * 错误日志
	 * @param object
	 * @param t
	 */
	public static void error(Object object, Throwable t) {
		if(object!=null)
		printLog(object.toString(), ERROR, t);
	}
	
	public static void error(Throwable t) {
		printLog("", ERROR, t);
	}
	
	/**
	 * 调试错误日志
	 * @param object
	 */
	public static void debug(Object object) {
		if(object!=null)
		printLog(object.toString(), DEBUG);
	}

	/**
	 * 调试错误日志
	 * @param object
	 * @param t
	 */
	public static void debug(Object object, Throwable t) {
		if(object!=null)
		printLog(object.toString(), DEBUG, t);
	}

	public static void debug(Throwable t) {
		printLog("", DEBUG, t);
	}
	/**
	 * 信息日志
	 * @param object
	 */
	public static void info(Object object) {
		if(object!=null)
		printLog(object.toString(), INFO);
	}

	/**
	 * 信息日志
	 * @param object
	 * @param t
	 */
	public static void info(Object object, Throwable t) {
		if(object!=null)
		printLog(object.toString(), INFO, t);
	}
	public static void info(Throwable t) {
		printLog("", INFO, t);
	}
	/**
	 * 警告日志
	 * @param object
	 */
	public static void warn(Object object) {
		if(object!=null)
		printLog(object.toString(), WARN);
	}

	/**
	 * 警告日志
	 * @param object
	 * @param t
	 */
	public static void warn(Object object, Throwable t) {
		if(object!=null)
		printLog(object.toString(), WARN, t);
	}

	public static void warn(Throwable t) {
		printLog("", WARN, t);
	}
	
	/**
	 * isInfoEnabled
	 * @param object
	 */
	public static boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	/**
	 * isDebugEnabled
	 * @param object
	 */
	public static boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}
	
	/**
	 * print StackTrace
	 * @return
	 */
	public static String stackTraceToString(Exception e) {	
		StringBuffer buffOut = new StringBuffer();
		buffOut.append("\n").append(e.getMessage()).append("\n");
        StackTraceElement[] trace = e.getStackTrace();
        for (int i = 0; i < trace.length; i++)
        	buffOut.append("\tat ").append(trace[i]).append("\n");
        Throwable ourCause = e.getCause();
        if (ourCause != null){
            logStackTraceAsCause(trace, ourCause,buffOut);
        }
        return buffOut.toString();
	}
	private static void logStackTraceAsCause(StackTraceElement[] causedTrace,Throwable e,StringBuffer buffOut) {
	    StackTraceElement[] trace = e.getStackTrace();
	    buffOut.append("\nCaused by: ").append(e.getMessage()).append("\n");
	    int m = trace.length - 1, n = causedTrace.length - 1;
	    while (m >= 0 && n >= 0 && trace[m].equals(causedTrace[n])) {
	        m--;
	        n--;
	    }
	    int framesInCommon = trace.length - 1 - m;
	    
	    for (int i = 0; i <= m; i++)
	    	buffOut.append("\tat ").append(trace[i]).append("\n");
	    if (framesInCommon != 0)
	    	buffOut.append("\t... ").append(framesInCommon).append(" more \n");
	    // Recurse if we have a cause
	    Throwable ourCause = e.getCause();
	    if (ourCause != null)
	        logStackTraceAsCause(trace, ourCause,buffOut);
	}
}
