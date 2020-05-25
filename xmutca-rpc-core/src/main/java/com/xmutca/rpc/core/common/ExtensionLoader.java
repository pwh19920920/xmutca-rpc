package com.xmutca.rpc.core.common;

import com.google.common.io.Resources;
import com.xmutca.rpc.core.exception.ClassNotFountException;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * SPI机制简单实现
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-10-30
 */
public class ExtensionLoader<T> {

    private static final String SERVICES_DIRECTORY = "META-INF/services/";
    private static final String RPC_DIRECTORY = "META-INF/rpc/";
    private static final int MIN_LENGTH = 2;
    private static final String DEFAULT_SPLIT_EQ = "=";
    private static final Integer DEFAULT_ORDERING = 1000;

    private static final Map<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();
    private static final ConcurrentMap<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<>();

    private final Map<String, Holder<T>> cachedInstances = new ConcurrentHashMap<>();
    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();
    private final Holder<Map<ExtensionGroup, Collection<T>>> cachedAllInstance = new Holder<>();

    private final Class<?> type;

    private ExtensionLoader(Class<?> type) {
        this.type = type;
    }

    /**
     * 获取loader
     * @param type
     * @param <T>
     * @return
     */
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        ExtensionLoader<T> loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        if (loader == null) {
            EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<T>(type));
            loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        }
        return loader;
    }

    /**
     * Get extension
     * @param name
     * @return
     */
    public T getExtension(String name) {
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException("Extension name == null");
        }

        Holder<T> holder = cachedInstances.get(name);
        if (holder == null) {
            cachedInstances.putIfAbsent(name, new Holder<T>());
            holder = cachedInstances.get(name);
        }

        T instance = holder.get();
        if (instance != null) {
            return instance;
        }

        synchronized (holder) {
            instance = holder.get();
            if (instance != null) {
                return instance;
            }

            instance = createExtension(name);
            holder.set(instance);
        }
        return instance;
    }

    /**
     * 创建实例
     * @param name
     * @return
     */
    public T newInstance(String name) {
        Class<?> clazz = getExtensionClasses().get(name);
        if (clazz == null) {
            throw new ClassNotFountException(String.format("ExtensionLoader load [%s][%s]", type, name));
        }

        try {
            return (T) clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException ex) {
            throw new IllegalStateException("instance extension fail", ex);
        }
    }

    /**
     * 创建实例
     * @param name
     * @return
     */
    public T newInstanceForMethod(String name, String method) {
        Class<?> clazz = getExtensionClasses().get(name);
        if (clazz == null) {
            throw new ClassNotFountException(String.format("ExtensionLoader load [%s][%s]", type, name));
        }

        try {
            return InvokeHelper.invoke(clazz, method, new Class[]{}, new Object[]{});
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
            throw new IllegalStateException("instance extension fail", ex);
        }
    }

    /**
     * 创建扩展
     * @param name
     * @return
     */
    private T createExtension(String name) {
        Class<?> clazz = getExtensionClasses().get(name);
        if (clazz == null) {
            throw new ClassNotFountException(String.format("ExtensionLoader load [%s][%s]", type, name));
        }

        try {
            T instance = (T) EXTENSION_INSTANCES.get(clazz);
            if (instance == null) {
                EXTENSION_INSTANCES.putIfAbsent(clazz, (T) clazz.newInstance());
                instance = (T) EXTENSION_INSTANCES.get(clazz);
            }
            return instance;
        } catch (Exception t) {
            throw new IllegalStateException(String.format("Extension instance [%s][%s] could not be instantiated", type, name), t);
        }
    }

    /**
     * 获取class类
     * @return
     */
    private Map<String, Class<?>> getExtensionClasses() {
        Map<String, Class<?>> classes = cachedClasses.get();
        if (classes != null) {
            return classes;
        }

        synchronized (cachedClasses) {
            classes = cachedClasses.get();
            if (classes != null) {
                return classes;
            }

            classes = loadExtensionClasses();
            cachedClasses.set(classes);
        }
        return classes;
    }

    /**
     * 真正加载类
     * @return
     */
    private Map<String, Class<?>> loadExtensionClasses() {
        Map<String, Class<?>> extensionClasses = new HashMap<>(16);
        loadDirectory(extensionClasses, SERVICES_DIRECTORY);
        loadDirectory(extensionClasses, RPC_DIRECTORY);
        return extensionClasses;
    }

    /**
     * 加载目录
     * @param extensionClasses
     * @param directory
     */
    private void loadDirectory(Map<String, Class<?>> extensionClasses, String directory) {
        try {
            Enumeration<URL> urls = ExtensionLoader.class.getClassLoader().getResources(directory + type.getName());
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                List<String> lines = Resources.readLines(url, StandardCharsets.UTF_8);
                for (String line : lines) {
                    loadClass(extensionClasses, line);
                }
            }
        } catch (Exception ex) {
            throw new IllegalStateException(String.format("Extension instance [%s] could not be load", type), ex);
        }
    }

    /**
     * 加载class
     * @param extensionClasses
     * @param line
     * @throws ClassNotFoundException
     */
    private void loadClass(Map<String, Class<?>> extensionClasses, String line) throws ClassNotFoundException {
        if (StringUtils.isBlank(line)) {
            return;
        }

        String[] arr = line.split(DEFAULT_SPLIT_EQ);
        if (arr.length < MIN_LENGTH) {
            return;
        }

        Class<?> clazz = Class.forName(arr[1]);
        extensionClasses.putIfAbsent(arr[0], clazz);
    }

    /**
     * 获取排序的拓展列表
     * @return
     */
    public Collection<T> getAllExtensionOfSorted(ExtensionGroup group) {
        Map<ExtensionGroup, Collection<T>> allInstances = cachedAllInstance.get();
        if (null != allInstances) {
            return allInstances.get(group);
        }

        synchronized (cachedAllInstance) {
            allInstances = cachedAllInstance.get();
            if (null != allInstances) {
                return allInstances.get(group);
            }

            allInstances = loadAllExtensionOfSorted();
            cachedAllInstance.set(allInstances);
        }
        return cachedAllInstance.get().get(group);
    }

    /**
     * 排序加载全部拓展
     * @return
     */
    private Map<ExtensionGroup, Collection<T>> loadAllExtensionOfSorted() {
        Map<ExtensionGroup, Collection<T>> instances = new ConcurrentHashMap<>(16);
        Map<String, Class<?>> allClasses = getExtensionClasses();
        Map<String, T> providerSortMap = new TreeMap<>();
        Map<String, T> consumerSortMap = new TreeMap<>();
        for (Map.Entry<String, Class<?>> entry : allClasses.entrySet() ) {
            T extension = getExtension(entry.getKey());
            ExtensionWrapper extensionWrapper = entry.getValue().getAnnotation(ExtensionWrapper.class);
            Integer ordering = DEFAULT_ORDERING;
            ExtensionGroup group = ExtensionGroup.ALL;
            if (null != extensionWrapper) {
                ordering = extensionWrapper.order();
                group = extensionWrapper.group();
            }

            BigDecimal order = new BigDecimal(ordering).setScale(10);
            String sortKey = order + entry.getKey();
            if (group == ExtensionGroup.CONSUMER) {
                consumerSortMap.put(sortKey, extension);
                continue;
            }

            if (group == ExtensionGroup.PROVIDER) {
                providerSortMap.put(sortKey, extension);
                continue;
            }

            consumerSortMap.put(sortKey, extension);
            providerSortMap.put(sortKey, extension);
        }

        instances.put(ExtensionGroup.CONSUMER, consumerSortMap.values());
        instances.put(ExtensionGroup.PROVIDER, providerSortMap.values());
        return instances;
    }
}
