package com.allen.component.redis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Allen
 * @date 2022/10/18 9:48 此类处理配置文件的配置项
 **/
@Configuration
@ConfigurationProperties(prefix="spring.data.redis")
public class RedissonProperties {
	private int timeout;

	private String host;

	private String port;

	private String password;

	private int database = 0;

	private int connectionPoolSize = 64;

	private int connectionMinimumIdleSize=10;

	private int slaveConnectionPoolSize = 250;

	private int masterConnectionPoolSize = 250;

	private String[] sentinelAddresses;

	private String masterName;

	private Map<String, String> cluster = new HashMap<>();

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getDatabase() {
		return database;
	}

	public void setDatabase(int database) {
		this.database = database;
	}

	public int getConnectionPoolSize() {
		return connectionPoolSize;
	}

	public void setConnectionPoolSize(int connectionPoolSize) {
		this.connectionPoolSize = connectionPoolSize;
	}

	public int getConnectionMinimumIdleSize() {
		return connectionMinimumIdleSize;
	}

	public void setConnectionMinimumIdleSize(int connectionMinimumIdleSize) {
		this.connectionMinimumIdleSize = connectionMinimumIdleSize;
	}

	public int getSlaveConnectionPoolSize() {
		return slaveConnectionPoolSize;
	}

	public void setSlaveConnectionPoolSize(int slaveConnectionPoolSize) {
		this.slaveConnectionPoolSize = slaveConnectionPoolSize;
	}

	public int getMasterConnectionPoolSize() {
		return masterConnectionPoolSize;
	}

	public void setMasterConnectionPoolSize(int masterConnectionPoolSize) {
		this.masterConnectionPoolSize = masterConnectionPoolSize;
	}

	public String[] getSentinelAddresses() {
		return sentinelAddresses;
	}

	public void setSentinelAddresses(String[] sentinelAddresses) {
		this.sentinelAddresses = sentinelAddresses;
	}

	public String getMasterName() {
		return masterName;
	}

	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}

	public Map<String, String> getCluster() {
		return cluster;
	}

	public void setCluster(Map<String, String> cluster) {
		this.cluster = cluster;
	}
}
