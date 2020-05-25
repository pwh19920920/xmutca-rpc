package com.xmutca.rpc.core.provider;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2020/5/22
 */
@Getter
@Setter
@Builder
public class ProviderEntry {

    /**
     * 接口
     */
    private String interfaceName;

    /**
     * 实现
     */
    private Object provider;
}
