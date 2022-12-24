package com.allen.component.redis;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @author Allen
 * @date 2022/10/18 14:00
 **/
@ComponentScan
@Configuration
public class RedisAutoConfig implements Ordered {

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

}
