package com.github.lxgang.centos.activemq.instance;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext.xml" })
public class AMQOperatorTest {
	private static Logger logger = LoggerFactory.getLogger(AMQOperatorTest.class);

	@Resource
	private AMQOperatorFactory amqProducerFactory;

	@Test
	public void test() {
		AMQOperator amqProducer = amqProducerFactory.getInstance("test");
		try {
			amqProducer.send2MQ("1234", true);

			Message message = amqProducer.receive(1000);
			if (message != null) {
				logger.info(String.format("获得mq队列中数据 ： %s .", message));
			}
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
		}
	}
}
