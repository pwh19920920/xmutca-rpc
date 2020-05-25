package com.xmutca.rpc.spring;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 入口
 * @author pwh
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({XmutcaRpcConfiguration.class})
public @interface EnableXmutcaRpc {
}
