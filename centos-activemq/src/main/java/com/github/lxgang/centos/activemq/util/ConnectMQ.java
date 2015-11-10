package com.github.lxgang.centos.activemq.util;

import java.util.concurrent.Callable;

import javax.jms.Connection;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ConnectMQ implements Callable<Connection> {
	private String userName;
	private String password;
	private String brokerURL;

	public ConnectMQ(String userName, String password, String brokerURL) {
		this.userName = userName;
		this.password = password;
		this.brokerURL = brokerURL;
	}

	public Connection call() throws Exception {
		System.out.println("启动线程，对brokerURL： " + brokerURL + "进行连接！");
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(userName, password, brokerURL);
		Connection connection = factory.createConnection();
		connection.start();
		System.out.println("brokerURL连接完成，返回connection ： " + connection);
		return connection;
	}
}
