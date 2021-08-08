package com.allen.jvm.controller;

import com.allen.jvm.po.SysUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Allen 2021/8/8 8:26
 **/
@RestController
@RequestMapping(value = "/test")
public class TestJvm {

    @GetMapping("test")
    public String test(){
        for (int i=0;i<1000;i++){
            SysUser user = new SysUser();
            user.setId(i);
            user.setName("这是一个测试案例，测试jvm");
        }
        return "success";
    }

}
