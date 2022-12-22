package com.allen.zookeeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ZookeeperApp {

	public static void main(String[] args) {
		SpringApplication.run(ZookeeperApp.class, args);
	}

}
