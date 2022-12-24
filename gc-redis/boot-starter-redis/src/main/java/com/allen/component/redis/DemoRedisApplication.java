package com.allen.component.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class DemoRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoRedisApplication.class, args);
    }

}
