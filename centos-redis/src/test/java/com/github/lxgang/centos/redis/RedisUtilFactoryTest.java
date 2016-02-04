package com.github.lxgang.centos.redis;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class RedisUtilFactoryTest {
	
	@Resource
	private RedisUtilFactory redisUtilFactory;
	
	@Test
	public void testRedis(){
		RedisUtil redisUtil = redisUtilFactory.getInstance();
		
		Date date=new Date();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		String time=format.format(date);
		System.out.println(time);
		redisUtil.addKeyValue(time, "18626872693", "1"); 
		redisUtil.addKeyValue(time, "18626872600", "2"); 
		
		String value = redisUtil.getValuebykey(time, "18626872693");
		assertEquals("1", value);
		value = redisUtil.getValuebykey(time, "18626872600");
		assertEquals("2", value);
		redisUtil.delAllBylKye(time);
		value = redisUtil.getValuebykey(time, "18626872600");
		assertNull(value);
	}
}
