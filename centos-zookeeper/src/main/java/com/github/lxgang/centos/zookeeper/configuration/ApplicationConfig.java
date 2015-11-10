package com.github.lxgang.centos.zookeeper.configuration;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
	@Value("${zookeeper.connectString}")
	private String connectString;

	@Value("${zookeeper.seesionTimeout}")
	private String seesionTimeout;

	@Bean
	public ZooKeeper zooKeeper() {

		ZooKeeper zooKeeper = null;
		try {
			int timeout = Integer.parseInt(seesionTimeout);
			zooKeeper = new ZooKeeper(connectString, timeout, new Watcher() {
				public void process(WatchedEvent event) {
					
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return zooKeeper;
	}
}
