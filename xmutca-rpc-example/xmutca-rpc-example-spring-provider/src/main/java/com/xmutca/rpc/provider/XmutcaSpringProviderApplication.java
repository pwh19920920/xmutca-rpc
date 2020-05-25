package com.xmutca.rpc.provider;

import com.xmutca.rpc.spring.EnableXmutcaRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author qudian
 */
@EnableXmutcaRpc
@SpringBootApplication
public class XmutcaSpringProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(XmutcaSpringProviderApplication.class, args);
    }
}