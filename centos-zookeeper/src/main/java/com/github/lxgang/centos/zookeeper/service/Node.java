package com.github.lxgang.centos.zookeeper.service;

import org.apache.zookeeper.KeeperException;

public interface Node<T> {

	public String getPath();
	
	public T getContent();
	
	public void setContent(T content) throws KeeperException, InterruptedException;
	
	public boolean isExist() throws KeeperException, InterruptedException;
	
	public void create() throws KeeperException, InterruptedException;
	
	public void delete() throws InterruptedException, KeeperException;
	
	public void close() throws InterruptedException;
}
