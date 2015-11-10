package com.github.lxgang.centos.zookeeper.instance;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lxgang.centos.zookeeper.service.Node;


public class CustomNode implements Node<String> {
	private Logger logger = LoggerFactory.getLogger(CustomNode.class);
	
	private String path;
	private String content;
	private ZooKeeper zooKeeper;

	CustomNode(ZooKeeper zooKeeper, String path) {
		this.zooKeeper = zooKeeper;
		this.path = path;
	}

	@Override
	public String getPath() {
		return this.path;
	}

	@Override
	public String getContent() {
		try {
			zooKeeper.exists(path, watcher); // 所要监控的主结点
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return this.content;
	}

	@Override
	public void setContent(String content) throws KeeperException, InterruptedException {
		zooKeeper.exists(path, watcher);	// 所要监控的主结点
		zooKeeper.setData(path, content.getBytes(), 0);
	}

	@Override
	public boolean isExist() throws KeeperException, InterruptedException {
		Stat stat = zooKeeper.exists(path, watcher);
		return stat != null;
	}

	@Override
	public void create() throws KeeperException, InterruptedException {
		zooKeeper.exists(path, watcher);	// 所要监控的主结点
		zooKeeper.create(path, "1".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}

	@Override
	public void delete() throws InterruptedException, KeeperException {
		zooKeeper.delete(path, -1);
	}

	@Override
	protected void finalize() throws Throwable {
		this.delete();
	}

	Watcher watcher = new Watcher() {
		public void process(WatchedEvent event) {
			// 主结点的数据发生改变时
			if (event.getType() == EventType.NodeDataChanged) {
				try {
					Stat stat = new Stat();
					stat = zooKeeper.exists(path, watcher);
					byte[] result = zooKeeper.getData(path, true, stat);
					logger.info(String.format("节点 %s 发生变化, 更改后的数据为 %s.", event.getPath(), new String(result)));
					
					content = new String(result);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	};

	@Override
	public void close() throws InterruptedException {
		zooKeeper.close();
	}
}