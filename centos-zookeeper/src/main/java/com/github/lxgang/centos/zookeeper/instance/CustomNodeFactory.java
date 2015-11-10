package com.github.lxgang.centos.zookeeper.instance;


import javax.annotation.Resource;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.stereotype.Service;

@Service
public class CustomNodeFactory {
	
	@Resource
	private ZooKeeper zooKeeper;
	
	public CustomNode getInstance(final String path) {
		CustomNode customNode = new CustomNode(zooKeeper, path);
		return customNode;
	}
}
