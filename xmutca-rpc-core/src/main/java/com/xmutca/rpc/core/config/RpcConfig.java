package com.xmutca.rpc.core.config;

import com.xmutca.rpc.core.common.Constants;
import lombok.Getter;
import lombok.Setter;

/**
 * 配置对象
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-10-30
 */
@Getter
@Setter
public class RpcConfig {

    /**
     * 超时时间
     */
    private int timeout = Constants.DEFAULT_TIMEOUT;
}
