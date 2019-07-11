package com.temporary.center.ls_common;

/**
 * PING AN INSURANCE (GROUP) COMPANY OF CHINA ,LTD.
 * Copyright (c) 1988-2014 All Rights Reserved.
 *
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;


/**
 * 
 * 日志工具类
 * <功能详细描述>
 * 
 * @author  卿剑
 * @version  [版本号, 2014-12-31]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class LogUtil {

    /**
     * logger
     */
    protected Logger log = null;

    private LogUtil(String logName) {
        log = LoggerFactory.getLogger(logName);
    }

    private LogUtil(Class<?> clazz) {
        log = LoggerFactory.getLogger(clazz);
    }
    
    private LogUtil(Logger log) {
        this.log=log;
    }
    
    /**
     * 
     * 
     * @param logName
     * @return
     */
    public static LogUtil getLogUtil(String logName) {
        return new LogUtil( logName);
    }
    
    /**
     * 
     * 
     * @param clazz
     * @return
     */
    public static LogUtil getLogUtil(Class<?> clazz) {
        return new LogUtil(clazz);
    }
    
    public static LogUtil getLogUtil(Logger log) {
        return new LogUtil(log);
    }

    /**
     * debug日志
     * 
     * @param message
     * @param args
     */
    public void debug(String message, Object... args) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(message, args));
        }
    }

    /**
     * debug日志
     * 
     * @param message
     * @param args
     */
    public void debug(Throwable e, String message, Object... args) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(message, args), e);
        }
    }

    /**
     * info级别日志
     * 
     * @param message
     * @param args
     */
    public void info(String message, Object... args) {

        if (log.isInfoEnabled()) {
            log.info(MessageFormat.format(message, args));
        }
    }

    /**
     * info级别日志
     * 
     * @param message
     * @param args
     */
    public void info(Throwable e, String message, Object... args) {

        if (log.isInfoEnabled()) {
            log.info(MessageFormat.format(message, args), e);
        }
    }

    /**
     * warn级别日志
     * 
     * @param message
     * @param args
     */
    public void warn(String message, Object... args) {

        if (log.isWarnEnabled()) {
            log.warn(MessageFormat.format(message, args));
        }
    }

    /**
     * warn级别日志
     * 
     * @param message
     * @param args
     */
    public void warn(Throwable e, String message, Object... args) {

        if (log.isWarnEnabled()) {
            log.warn(MessageFormat.format(message, args), e);
        }
    }

    /**
     * error级别日志
     * 
     * @param message
     * @param args
     */
    public void error(Throwable e, String message, Object... args) {

        if (log.isErrorEnabled()) {
            log.error(MessageFormat.format(message, args), e);
        }
    }

    /**
     * error级别日志
     * 
     * @param message
     * @param args
     */
    public void error(String message, Object... args) {

        if (log.isErrorEnabled()) {
            log.error(MessageFormat.format(message, args));
        }
    }

}
