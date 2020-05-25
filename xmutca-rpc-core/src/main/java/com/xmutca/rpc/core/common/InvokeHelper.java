package com.xmutca.rpc.core.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-09
 */
public final class InvokeHelper {

    private InvokeHelper() {

    }

    /**
     * 反射执行方法 - 静态方法
     * @param clazz
     * @param methodName
     * @param parameterTypes
     * @param arguments
     * @param <T>
     * @return
     */
    public static <T> T invoke(Class<?> clazz, String methodName, Class<?>[] parameterTypes, Object[] arguments) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = clazz.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return (T) method.invoke(clazz, arguments);
    }

    /**
     * 反射执行方法 - 普通方法
     * @param clazz
     * @param methodName
     * @param parameterTypes
     * @param arguments
     * @param <T>
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static <T> T invoke(Object object, String methodName, Class<?>[] parameterTypes, Object[] arguments) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = object.getClass().getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return (T) method.invoke(object, arguments);
    }
}
