package com.xmutca.rpc.core.common;

import java.lang.annotation.*;

/**
 * 拓展
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ExtensionWrapper {

    /**
     * 排序信息，可以不提供。
     */
    int order() default 0;

    /**
     * 分组类型，默认为消费者 + 提供者
     * @return
     */
    ExtensionGroup group() default ExtensionGroup.ALL;
}
