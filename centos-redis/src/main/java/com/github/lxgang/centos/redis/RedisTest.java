package com.github.lxgang.centos.redis;

import redis.clients.jedis.Jedis;

public class RedisTest {
	public static void main(String args[]) {
		Jedis test = new Jedis("192.168.1.175", 6379);
		test.set("hw", "hello world");
		String hello = test.get("hw");
		System.out.println(hello);
		test.close();
	}
}
