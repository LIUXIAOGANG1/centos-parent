package com.github.lxgang.centos.memcached.instance;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.whalin.MemCached.MemCachedClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
public class MemCachedTest {
	private static Logger logger = LoggerFactory.getLogger(MemCachedTest.class);
	
	@Resource
	private MemCachedClient memCachedClient;
	
	@Test
	public void test(){
		String key = "key1";
		if(memCachedClient.keyExists(key)){
			memCachedClient.delete(key);
		}
		
		memCachedClient.add(key, "18");
		logger.info(String.format("键 %s 对应的值为 : %s", key, memCachedClient.get(key)));
		
		memCachedClient.replace(key, "5");
		logger.info(String.format("键 %s 对应的值为 : %s", key, memCachedClient.get(key)));
	}
} 
