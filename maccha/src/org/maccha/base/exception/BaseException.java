package org.maccha.base.exception;

import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.log4j.Logger;
import org.maccha.base.util.StringUtils;
import org.maccha.spring.MessageUtils;

/**
 * Created by Lori Yue on 16-3-3.
 */
public class BaseException extends NestableRuntimeException {
    private static Logger log = Logger.getLogger(BaseException.class);
    public static final String UNKNOWN = "UNKNOWN";
    private String key;
    private Object[] arguments;
    private String defaultMessage;

    public BaseException(String key, Object[] args, String defaultMessage, Throwable nested) {
        super(nested);
        this.key = key;
        this.arguments = args;
        this.defaultMessage = defaultMessage;
    }

    public BaseException(String _message, Throwable t) {
        this(null, null, _message, t);
    }

    public BaseException(Throwable t) {
        this(null, null, t.getMessage(), t);
    }

    public BaseException(String message) {
        this(null, null, message, null);
    }
    /**
     * 通过Spring的messageSourse获取异常消息
     */
    public String getMessage() {
        String strMessage = null;

        if (StringUtils.isNotNull(this.key)) {
            try {
                strMessage = MessageUtils.getMessage(this.key, this.arguments);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            if (StringUtils.isNotNull(strMessage)) return strMessage;
        } else if (StringUtils.isNotNull(this.defaultMessage)) {
            return this.defaultMessage;
        }
        return super.getMessage();
    }
    /**
     * 以可抛出的异常类的类名为内容输出ERROR级别的日志
     * @param t 可抛出的异常
     */
    public static void handleError(Throwable t) {
        log.error(t);
    }
    /**
     * 输出ERROR级别的日志,内容包括自定义message和抛出异常的信息
     * @param _message 自定义message
     */
    public static void handleError(String _message, Throwable t) {
        log.error(_message, t);
    }
    /**
     * 以可抛出的异常类的类名为内容输出WARN级别的日志
     * @param t 可抛出的异常
     */
    public static void handleWarn(Throwable t) {
        log.warn(t);
    }
    /**
     * 输出WARN级别的日志,内容包括自定义message和抛出异常的信息
     * @param _message 自定义message
     * @param t 抛出的异常
     */
    public static void handleWarn(String _message, Throwable t) {
        log.warn(_message, t);
    }
    /**
     * 输出WARN级别的日志
     * @param _message 自定义message
     */
    public static void handleWarn(String _message) {
        log.warn(_message);
    }
    /**
     * 以可抛出的异常类的类名为内容输出INFO级别的日志
     * @param t 可抛出的异常
     */
    public static void handleInfo(Throwable t) {
        log.info(t);
    }
    /**
     * 输出INFO级别的日志,内容包括自定义message和抛出异常的信息
     * @param _message 自定义message
     * @param t 抛出的异常
     */
    public static void handleInfo(String _message, Throwable t) {
        log.info(_message, t);
    }
    /**
     * 输出INFO级别的日志
     * @param _message 自定义message
     */
    public static void handleInfo(String _message) {
        log.info(_message);
    }

    public String getKey() {
        return key;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }
}
