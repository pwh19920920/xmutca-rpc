package com.xmutca.rpc.core.consumer;

import com.xmutca.rpc.core.config.RpcClientConfig;

import java.lang.annotation.*;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2019-11-10
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Reference {

    /**
     * 系统元数据 - 应用名称
     */
    String serviceName();

    /**
     * 系统元数据 - 版本号
     */
    String version() default "v1.0.0";

    /**
     * 系统元数据 - 系统分组
     */
    String group() default "default";

    /**
     * 引用接口
     * @return
     */
    Class<?> interfaceClass();
}
