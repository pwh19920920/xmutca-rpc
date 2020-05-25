package com.xmutca.rpc.core.provider;

import java.lang.annotation.*;

/**
 * 提供者注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Provider {

    /**
     * 声明实现的接口
     * @return
     */
    Class<?> interfaceClass();
}
