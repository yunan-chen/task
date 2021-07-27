package com.siemens.ct.task.util;

import java.lang.reflect.Method;

public class RetryUtil {

    private static ThreadLocal<Integer> retryCountInThread = new ThreadLocal<>();

    /**
     * @param retryCount
     */
    public static RetryUtil setRetryTimes(Integer retryCount) {
        if (retryCountInThread.get() == null)
            retryCountInThread.set(retryCount);
        return new RetryUtil();
    }

    /**
     * @param args
     */
    public Object retry(Object... args) {
        try {
            Integer retryCount = retryCountInThread.get();
            if (retryCount <= 0) {
                retryCountInThread.remove();
                return null;
            }
            retryCountInThread.set(--retryCount);
            String upperClassName = Thread.currentThread().getStackTrace()[2].getClassName();
            String upperMethodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            Class clazz = Class.forName(upperClassName);
            Object targetObject = clazz.newInstance();
            Method targetMethod = null;
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().equals(upperMethodName)) {
                    targetMethod = method;
                    break;
                }
            }
            if (targetMethod == null)
                return null;
            targetMethod.setAccessible(true);
            return targetMethod.invoke(targetObject, args);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
