package com.xmutca.rpc.provider.facade;

import com.xmutca.rpc.core.provider.Provider;
import com.xmutca.rpc.example.api.HelloService;
import org.springframework.stereotype.Component;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2018-12-28
 */
@Component
@Provider(interfaceClass = HelloService.class)
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String msg) {
        return msg;
    }
}
