package com.xmutca.rpc.consumer;

import com.xmutca.rpc.spring.EnableXmutcaRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author qudian
 */
@EnableXmutcaRpc
@SpringBootApplication
public class XmutcaSpringConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(XmutcaSpringConsumerApplication.class, args);
    }
}