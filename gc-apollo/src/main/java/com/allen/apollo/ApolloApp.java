package com.allen.apollo;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Created by xuguocai on 2021/4/7 9:05
 */
@EnableApolloConfig
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ApolloApp {

    public static void main(String[] args) {
        SpringApplication.run(ApolloApp.class, args);
    }

}
