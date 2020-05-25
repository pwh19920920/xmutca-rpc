package com.xmutca.rpc.spring;

import com.xmutca.rpc.spring.common.SpringContextHolder;
import com.xmutca.rpc.spring.common.XmutcaRpcProperties;
import com.xmutca.rpc.spring.consumer.ReferencePostProcessor;
import com.xmutca.rpc.spring.provider.ProviderCommand;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2020/5/25
 */
@Configuration
@EnableConfigurationProperties(XmutcaRpcProperties.class)
public class XmutcaRpcConfiguration {

    @Bean
    public ReferencePostProcessor getReferenceBeanPostProcessor(XmutcaRpcProperties xmutcaRpcProperties) {
        return new ReferencePostProcessor(xmutcaRpcProperties);
    }

    @Bean
    public SpringContextHolder getSpringContextHolder() {
        return new SpringContextHolder();
    }

    @Bean
    public ProviderCommand getProviderCommand(XmutcaRpcProperties xmutcaRpcProperties) {
        return new ProviderCommand(xmutcaRpcProperties);
    }
}
