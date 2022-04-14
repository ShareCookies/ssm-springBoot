package com.china.hcg.spring.SpringEventListener;

import com.china.hcg.spring.SpringEventListener.HelloEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
@RestController
public class TestController  {
    @Resource
    ApplicationContext applicationContext;

    /**
     * 验证tokenid
     *
     * @param tokenid
     * @return
     */
    @GetMapping("/eventListenerTest")
    public void checkTokenHsf(String tokenid) {
        applicationContext.publishEvent(new HelloEvent(this,"lgb"));
        System.err.println("发布事件的线程");
    }

}
