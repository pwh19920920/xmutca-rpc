package com.xmutca.rpc.core.provider;

import com.xmutca.rpc.core.common.ExtensionGroup;
import com.xmutca.rpc.core.common.ExtensionLoader;
import org.assertj.core.util.Lists;

import java.util.Collection;
import java.util.List;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2020/5/22
 */
public class ProviderWrapper {

    private ProviderWrapper() {}

    /**
     * 加载接口实现
     * @param scanPackage
     * @return
     */
    public static List<ProviderEntry> load(String scanPackage) {
        // 初始化chain的过程
        Collection<ProviderLoader> loaders = ExtensionLoader.getExtensionLoader(ProviderLoader.class).getAllExtensionOfSorted(ExtensionGroup.PROVIDER);
        if (loaders.isEmpty()) {
            return Lists.emptyList();
        }

        ProviderLoader loader = loaders.iterator().next();
        return loader.load(scanPackage);
    }
}
