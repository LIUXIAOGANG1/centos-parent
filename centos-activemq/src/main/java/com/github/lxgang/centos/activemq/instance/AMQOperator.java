package com.github.lxgang.centos.activemq.instance;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.lxgang.centos.activemq.bean.BeanParent;
import com.github.lxgang.centos.activemq.service.Queue;

public class AMQOperator implements Queue {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private Session session;
	private String queue;

	AMQOperator(Session session, String queue) {
		this.session = session;
		this.queue = queue;
	}

	@Override
	public void send2MQ(Object obj, boolean deliveryMode) throws JMSException {
		Destination destination = session.createQueue(queue);
		MessageProducer producer = session.createProducer(destination);
		int deliverymode = deliveryMode ? DeliveryMode.PERSISTENT : DeliveryMode.NON_PERSISTENT;

		producer.setDeliveryMode(deliverymode);

		if (obj instanceof String) {
			TextMessage message = session.createTextMessage((String) obj);
			producer.send(message);
		}

		if (obj instanceof BeanParent) {
			ObjectMessage objectMessage = session.createObjectMessage((BeanParent) obj);
			producer.send(objectMessage);
		}
	}

	@Override
	public Message receive(long timeout) throws JMSException {
		// 创建主题
		Destination destination = session.createQueue(queue);
		// 创建订阅
		MessageConsumer consumer = session.createConsumer(destination);
		
		return consumer.receive(timeout);
	}
	
	public Session getSession() {
		return session;
	}

	public String getQueue() {
		return queue;
	}
}
