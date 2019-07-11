package com.temporary.center.ls_common;

/*
 * 文 件 名:  ContextUtils.java
 * 版    权:  Shenzhen JL-Tour International Travel Service Co.,LTD. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人: qj
 * 修改时间:  2015年4月22日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  
 */

import org.springframework.context.ApplicationContext;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  qj
 * @version  [版本号, 2015年4月22日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ContextUtils {
    
    private static ApplicationContext applicationContext;
    
    private static LogUtil log = LogUtil.getLogUtil(ContextUtils.class);
    
    public static void setApplicationContext(
            ApplicationContext applicationContext) {
        synchronized (ContextUtils.class) {
            log.debug("setApplicationContext, notifyAll");
            ContextUtils.applicationContext = applicationContext;
            ContextUtils.class.notifyAll();
        }
    }
    
    public static ApplicationContext getApplicationContext() {
        synchronized (ContextUtils.class) {
            int count_no=0;
            while (applicationContext == null && count_no<=3) {
                try {
                    log.debug("getApplicationContext, wait...");
                    ContextUtils.class.wait(5000);
                    if (applicationContext == null) {
                        log.warn("Have been waiting for ApplicationContext to be set for 5000",
                                new Exception());
                    }
                    count_no++;
                }
                catch (InterruptedException ex) {
                    log.debug("getApplicationContext, wait interrupted");
                }
            }
            return applicationContext;
        }
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T)getApplicationContext().getBean(name);
    }
    
}
