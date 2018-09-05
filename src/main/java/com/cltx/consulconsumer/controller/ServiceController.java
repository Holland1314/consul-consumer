package com.cltx.consulconsumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by shileichao on 2018/9/5.
 */
@RestController
public class ServiceController {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * 获取所有服务
     * @return
     */
    @RequestMapping("/services")
    public Object services(){
        return discoveryClient.getInstances("service-producer");
    }

    @RequestMapping("/discover")
    public Object discover(){
        return loadBalancerClient.choose("service-producer").getUri().toString();

    }

    @RequestMapping("call")
    public String call(){
        ServiceInstance instance = loadBalancerClient.choose("service-producer");
        System.out.println("服务地址： "+instance.getUri());
        System.out.println("服务名称： "+instance.getServiceId());
        String result = new RestTemplate().getForObject(instance.getUri().toString() + "hello", String.class);
        System.out.println(result);
        return result;
    }
}
