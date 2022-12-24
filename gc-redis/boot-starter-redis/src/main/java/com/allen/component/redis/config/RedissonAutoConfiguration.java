package com.allen.component.redis.config;

import com.allen.component.redis.helper.RedissonLockHelper;
import com.allen.component.redis.repository.RedisRepository;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 *    基于Redisson的分布式锁实现
 *    支持redis的单机模式，哨兵模式，集群模式
 *    能够自动读取redis的配置，根据关键字段进行相应模式的自动装配
 * @author Allen
 * @date 2022/10/18 9:36
 **/
@Configuration
@ConditionalOnClass(Config.class)
public class RedissonAutoConfiguration {

	private final RedissonProperties redssionProperties;

	public RedissonAutoConfiguration(RedissonProperties redssionProperties) {
		this.redssionProperties = redssionProperties;
	}

	@Bean
	public RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		template.setEnableTransactionSupport(true);
		template.setConnectionFactory(factory);
		return template;
	}

	/**
	 * 哨兵模式自动装配
	 *
	 * @return
	 */
	@Bean
	@ConditionalOnProperty(name = "spring.redis.master-name")
	RedissonClient redissonSentinel() {
		String[] nodes = redssionProperties.getSentinelAddresses();
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = "redis://" + nodes[i];
		}
		Config config = new Config();
		SentinelServersConfig serverConfig = config.useSentinelServers()
			.addSentinelAddress(nodes)
			.setMasterName(redssionProperties.getMasterName())
			.setTimeout(redssionProperties.getTimeout())
			.setMasterConnectionPoolSize(redssionProperties.getMasterConnectionPoolSize())
			.setSlaveConnectionPoolSize(redssionProperties.getSlaveConnectionPoolSize());
		if (StringUtils.isNotBlank(redssionProperties.getPassword())) {
			serverConfig.setPassword(redssionProperties.getPassword());
		}
		return Redisson.create(config);
	}

	/**
	 * 单机模式自动装配
	 *
	 * @return
	 */
	@Bean
	@ConditionalOnProperty(name = "spring.data.redis.host")
	RedissonClient redissonSingle() {
		Config config = new Config();
		SingleServerConfig serverConfig = config.useSingleServer()
			.setAddress("redis://" + redssionProperties.getHost() + ":" + redssionProperties.getPort())
			.setTimeout(redssionProperties.getTimeout())
			.setConnectionPoolSize(redssionProperties.getConnectionPoolSize())
			.setConnectionMinimumIdleSize(redssionProperties.getConnectionMinimumIdleSize());
		if (StringUtils.isNotBlank(redssionProperties.getPassword())) {
			serverConfig.setPassword(redssionProperties.getPassword());
		}
		return Redisson.create(config);
	}

	/**
	 * 集群装配模式
	 *
	 * @return
	 */
	@Bean
	@ConditionalOnProperty(name = "spring.redis.cluster.nodes")
	RedissonClient redissonCluster() {
		String[] nodes = redssionProperties.getCluster().get("nodes").split(",");
		for (int i = 0; i < nodes.length; i++) {
			nodes[i] = "redis://" + nodes[i];
		}
		Config config = new Config();
		ClusterServersConfig serverConfig = config.useClusterServers()
			.setScanInterval(2000)
			.addNodeAddress(nodes)
			.setTimeout(redssionProperties.getTimeout())
			.setMasterConnectionPoolSize(redssionProperties.getMasterConnectionPoolSize())
			.setSlaveConnectionPoolSize(redssionProperties.getSlaveConnectionPoolSize())
			.setPassword(redssionProperties.getPassword());
		if (StringUtils.isNotBlank(redssionProperties.getPassword())) {
			serverConfig.setPassword(redssionProperties.getPassword());
		}
		return Redisson.create(config);
	}

	@Bean
	public RedissonLockHelper redissonDistributedLocker(RedissonClient redissonClient) {
		return new RedissonLockHelper(redissonClient);
	}

//	@Bean
//	public RedisRepository redisRepository(RedisTemplate<String, Object> redisTemplate, RedisConnectionFactory redisConnectionFactory) {
//		return new RedisRepository(redisTemplate,redisConnectionFactory);
//	}

}
