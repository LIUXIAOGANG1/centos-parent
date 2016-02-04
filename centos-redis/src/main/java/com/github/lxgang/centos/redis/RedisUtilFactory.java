package com.github.lxgang.centos.redis;

public class RedisUtilFactory {
	private String redisIp;
	private Integer redisPort;
	private String redisPassword;
	
	public RedisUtil getInstance() {
		RedisUtil redisUtil = new RedisUtil(redisIp, redisPort, redisPassword);
		return redisUtil;
	}

	public void setRedisIp(String redisIp) {
		this.redisIp = redisIp;
	}

	public void setRedisPort(Integer redisPort) {
		this.redisPort = redisPort;
	}

	public void setRedisPassword(String redisPassword) {
		this.redisPassword = redisPassword;
	}
}
