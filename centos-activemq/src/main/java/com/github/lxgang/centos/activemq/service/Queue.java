package com.github.lxgang.centos.activemq.service;

import javax.jms.JMSException;

public interface Queue {
	public void send2MQ(Object obj, boolean deliveryMode) throws JMSException;
}
