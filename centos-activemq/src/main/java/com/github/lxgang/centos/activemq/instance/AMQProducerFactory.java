package com.github.lxgang.centos.activemq.instance;

import javax.annotation.Resource;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AMQProducerFactory {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private Session session;
	
	public AMQProducer getInstance(String queue) {
		AMQProducer amqProducer = new AMQProducer(session, queue);
		return amqProducer;
	}
}
