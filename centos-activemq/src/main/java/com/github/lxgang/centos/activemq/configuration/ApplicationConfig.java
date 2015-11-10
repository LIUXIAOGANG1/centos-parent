package com.github.lxgang.centos.activemq.configuration;

import javax.jms.Connection;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${com.github.activemq.brokerURL}")
	private String brokerURL;

	@Value("${com.github.activemq.userName}")
	private String userName;

	@Value("${com.github.activemq.password}")
	private String password;
	
	@Bean
	public Session session() {
		Session session = null;
		try {
			ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(userName, password, brokerURL);
			Connection connection = factory.createConnection();
			connection.start();
			// 创建Session，参数解释：
			// 第一个参数是否使用事务:当消息发送者向消息提供者（即消息代理）发送消息时，消息发送者等待消息代理的确认，没有回应则抛出异常，消息发送程序负责处理这个错误。
			// 第二个参数消息的确认模式：
			// AUTO_ACKNOWLEDGE ： 指定消息提供者在每次收到消息时自动发送确认。消息只向目标发送一次，但传输过程中可能因为错误而丢失消息。
			// CLIENT_ACKNOWLEDGE ： 由消息接收者确认收到消息，通过调用消息的acknowledge()方法（会通知消息提供者收到了消息）
			// DUPS_OK_ACKNOWLEDGE ： 指定消息提供者在消息接收者没有确认发送时重新发送消息（这种确认模式不在乎接收者收到重复的消息）。
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return session;
	}
}
