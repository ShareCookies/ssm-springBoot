package com.neo.remote;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by summer on 2017/5/11.
 * 使用feign代理远程对象
 */
//@FeignClient:name:远程服务名。就是远程服务提供者配置中的spring.application.name名称
@FeignClient(name= "spring-cloud-producer")
public interface HelloRemote {
    //接口中的方法和远程服务contoller中的方法名和参数,注解等保持一致。
    @RequestMapping(value = "/hello")
    public String hello(@RequestParam(value = "name") String name);


}
