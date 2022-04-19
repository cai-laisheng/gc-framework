package com.allen.spider.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;

/**
 * @author xuguocai  @date 2022/4/17 17:07 定时器默认为单线程，所以如果项目中使用多个定时器要配置线程池
 */

@Slf4j
@Configuration
@EnableScheduling //开启定时器注解
public class ScheduleConfig {

    @Scheduled(cron = "0 0/10 * * * ?")
    public void spiderData() {
        log.info("========定时器执行开始========");

        log.info("========定时器执行结束========");
    }

    /**
     * 解决druid 日志报错：discard long time none received connection:xxx
     * */
    @PostConstruct
    public void setProperties(){
        System.setProperty("druid.mysql.usePingMethod","false");
    }

}
