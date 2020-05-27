package com.xmutca.rpc.consumer;

import com.xmutca.rpc.core.consumer.Reference;
import com.xmutca.rpc.example.api.HelloService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version Revision: 0.0.1
 * @author: weihuang.peng
 * @Date: 2020/5/26
 */
@RestController
@RequestMapping
public class ControllerTest {

    @Reference(serviceName = "test", group = "order", interfaceClass = HelloService.class)
    private HelloService helloService;

    @RequestMapping("/test")
    public Object test() {
        return helloService.sayHello("xxx");
    }
}
