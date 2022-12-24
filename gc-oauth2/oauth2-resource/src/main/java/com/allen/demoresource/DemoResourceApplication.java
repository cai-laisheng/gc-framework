package com.allen.demoresource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class DemoResourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoResourceApplication.class, args);
    }

}
