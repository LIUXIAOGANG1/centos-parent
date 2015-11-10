package com.github.lxgang.centos.activemq.instance;

import javax.annotation.Resource;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AMQOperatorFactory {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	private Session session;
	
	public AMQOperator getInstance(String queue) {
		AMQOperator amqProducer = new AMQOperator(session, queue);
		return amqProducer;
	}
}
