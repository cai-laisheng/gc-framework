package com.allen.boot.controller;

import com.allen.boot.properties.AllenProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 自定义属性配置 自定义配置获取
 *
 * Created by xuguocai on 2020/11/30 15:17
 */
@RequestMapping("/allen")
@RestController
public class BootController {
    private static final Logger log = LoggerFactory.getLogger(BootController.class);

    private AllenProperties allenProperties;

    public BootController(AllenProperties allenProperties){
        this.allenProperties = allenProperties;
    }

    @GetMapping("/1")
    public AllenProperties myProperties1() {
        log.info("=================================================================================================");
        log.info(allenProperties.toString());
        log.info("=================================================================================================");
        return allenProperties;
    }

    /**
     * 表示启用重试机制(value表示哪些异常需要触发重试，maxAttempts设置最大重试次数，delay表示重试的延迟时间，multiplier表示上一次延时时间是这一次的倍数)
     * @return
     */
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 2000, multiplier = 1.5))
    @GetMapping("/test")
    public String test() {
        log.info("---------");
        log.info(allenProperties.toString());
        log.info("-----");
        return "allenProperties";
    }
}
