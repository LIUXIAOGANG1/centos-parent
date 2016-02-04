package com.github.lxgang.centos.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {
	private Jedis jedis;
	
	public RedisUtil(String redisIp, Integer redisPort, String redisPassword) {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(5000);
		config.setMaxIdle(5000);
		config.setMaxWait(100000);
		config.setTestOnBorrow(true);
		// slave链接
		// 构造池
		JedisPool pool = new JedisPool(config, redisIp, redisPort, 10 * 1000, redisPassword);
		jedis = pool.getResource();
		pool.returnResource(jedis);
	}

	/**
	 * 判断主键是否存在
	 * 
	 */
	public boolean isExitsKey(String key, String field) {
		//HEXISTS key field查看哈希表key中，给定域field是否存在。
		return jedis.hexists(key, field);
	}

	public Long addKeyValue(String key, String field, String value) {
		//HSET key field value将哈希表key中的域field的值设为value。
		return jedis.hset(key, field, value);
	}

	public Long addKeyValue(String key, String field, String value, int seconds) {
		//EXPIRE key seconds 为给定key设置生存时间。当key过期时，它会被自动删除。
		jedis.expire(key, seconds);
		
		return jedis.hset(key, field, value);
	}

	public void delByKye(String key, String fields) {
		//HDEL key field [field ...]删除哈希表key中的一个或多个指定域。 
		jedis.hdel(key, fields);
	}

	public void delAllBylKye(String key) {
		//DEL 移除给定的一个或多个key。如果key不存在，则忽略该命令。 
		jedis.del(key);
	}

	public Long getKeySize(String key) {
		//HLEN key 返回哈希表key中域的数量。 
		return jedis.hlen(key);
	}

	public String getValuebykey(String key, String field) {
		//HGET key field返回哈希表key中给定域field的值。
		return jedis.hget(key, field);
	}

	public void cleanKey() {
		//清空所有的key 
		jedis.flushAll();
	}

	public String getValueByIndex(String key, long index) {
		return jedis.lindex(key, index);
	}
}
