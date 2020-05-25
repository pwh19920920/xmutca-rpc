package com.xmutca.rpc.core.provider;

import java.util.List;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2020/5/22
 */
public interface ProviderLoader {

    /**
     * 加载接口实现
     * @param scanPackage
     * @return
     */
    List<ProviderEntry> load(String scanPackage);
}
