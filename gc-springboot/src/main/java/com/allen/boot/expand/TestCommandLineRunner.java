package com.allen.boot.expand;

import org.springframework.boot.CommandLineRunner;

/**
 * @author xuguocai
 * @date 2022/8/16 17:31
 *
 * run(String... args)，触发时机为整个项目启动完毕后，自动执行。如果有多个CommandLineRunner，可以利用@Order来进行排序。
 *
 * 使用场景：用户扩展此接口，进行启动项目之后一些业务的预处理。
 */
public class TestCommandLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("[TestCommandLineRunner]");
    }
}
