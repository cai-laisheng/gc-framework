package com.allen.sys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * EnableDiscoveryClient  可加可不加，配置文件配置，引入相关包就可以了
 */
//@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"com.allen.sys"})
@MapperScan(value = "com.allen.sys.mapper")
public class AdminApp {

	public static void main(String[] args) {
		SpringApplication.run(AdminApp.class, args);
	}

}
