package com.allen.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 熔断后转发处理类
 * @author xuguocai 2020/7/2 13:32
 */
@RestController
public class FallbackController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/fallback")
    public String fallback() {
        return "fallback";
    }

    @GetMapping("/get")
    public List<String> get() {
        List<String> services = discoveryClient.getServices();
        return services;
    }

}
