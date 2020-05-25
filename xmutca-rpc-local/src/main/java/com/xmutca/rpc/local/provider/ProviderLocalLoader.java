package com.xmutca.rpc.local.provider;

import com.google.common.collect.Lists;
import com.xmutca.rpc.core.provider.Provider;
import com.xmutca.rpc.core.provider.ProviderEntry;
import com.xmutca.rpc.core.provider.ProviderLoader;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.MethodParameterNamesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 本地方式加载，限制实现类必须有一个空构造
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2020/5/22
 */
@Slf4j
public class ProviderLocalLoader implements ProviderLoader {

    @Override
    public List<ProviderEntry> load(String scanPackage) {
        Reflections reflections = new Reflections(scanPackage, new MethodAnnotationsScanner(), new TypeAnnotationsScanner(), new SubTypesScanner(), new MethodParameterNamesScanner());
        Set<Class<?>> resources = reflections.getTypesAnnotatedWith(Provider.class);
        List<ProviderEntry> providers = new ArrayList<>(16);
        resources.parallelStream().forEach(clazz -> {
            Provider provider = clazz.getAnnotation(Provider.class);

            // checkImpl
            Class<?>[] interfaces = clazz.getInterfaces();
            if (!Lists.newArrayList(interfaces).contains(provider.interfaceClass())) {
                return;
            }

            // constructors
            Constructor[] constructors = clazz.getConstructors();

            try {
                ProviderEntry entry = ProviderEntry
                        .builder()
                        .interfaceName(provider.interfaceClass().getName())
                        .provider(constructors[0].newInstance())
                        .build();
                providers.add(entry);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                log.info("反射发生异常");
            }
        });
        return providers;
    }
}
