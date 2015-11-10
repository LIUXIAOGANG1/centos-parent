package com.github.lxgang.centos.activemq.service;

import javax.jms.JMSException;
import javax.jms.Message;

public interface Queue {
	public void send2MQ(Object obj, boolean deliveryMode) throws JMSException;
	
	public Message receive(long timeout) throws JMSException;
}
