package com.github.lxgang.centos.memcached.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

@Configuration
public class ApplicationConfig {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${com.github.memcached.server}")
	private String server;


	@Bean
	public MemCachedClient memCachedClient() {
		MemCachedClient memCachedClient = new MemCachedClient();
		try {
			String[] servers = {server};
			// 创建一个连接池
			SockIOPool pool = SockIOPool.getInstance();
			// 设置缓存服务器
			pool.setServers(servers);
			// 设置初始化连接数，最小连接数，最大连接数以及最大处理时间
			pool.setInitConn(50);
			pool.setMinConn(50);
			pool.setMaxConn(500);
			pool.setMaxIdle(1000 * 60 * 60);
			// 设置主线程睡眠时间，每3秒苏醒一次，维持连接池大小
			// maintSleep 千万不要设置成30，访问量一大就出问题，单位是毫秒，推荐30000毫秒。
			pool.setMaintSleep(3000);
			// 关闭套接字缓存
			pool.setNagle(false);
			// 连接建立后的超时时间
			pool.setSocketTO(3000);
			// 连接建立时的超时时间
			pool.setSocketConnectTO(0);
			// 初始化连接池
			pool.initialize();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return memCachedClient;
	}
}
