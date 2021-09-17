package com.allen.redisson.controller;

import com.allen.redisson.annotation.MethodLimit;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xuguocai on 2021/4/14 10:21
 */
@CrossOrigin
@RestController
@RequestMapping("/allen")
public class TestController {

    /**
     * 注解方式实现 限流 操作
     * @return
     */
    @MethodLimit(key = "testLimit",period = 3,count = 300)
    @RequestMapping(value = "/testLimit", method = RequestMethod.GET)
    public String testLimit() {
        System.out.println("----------------------------");
        return "test";
    }

}
