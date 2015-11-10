package com.github.lxgang.centos.activemq.util;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQObjectMessage;

public class ActiveMQUtils {
	public static Connection connectMQ(String userName, String password, String brokerURL) throws JMSException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(userName, password, brokerURL);
		Connection connection = factory.createConnection();
		connection.start();

		return connection;
	}
	
	public static void send2MQ(Session session, String Qname) throws JMSException{
		Destination destination = session.createQueue(Qname);
		MessageProducer producer = session.createProducer(destination);
		
		producer.setDeliveryMode(DeliveryMode.PERSISTENT);	//DeliveryMode.NON_PERSISTENT
		
		TestBean testBean = new TestBean();
		testBean.setName("张三");
		testBean.setAge(25);
		testBean.setSex("男");
		
		ObjectMessage objectMessage = session.createObjectMessage(testBean);
		producer.send(objectMessage);
	}
	
	public static void getMessage(Session session, String Qname) throws JMSException{
		Destination destination = session.createQueue(Qname);
		MessageConsumer consumer = session.createConsumer(destination);

		while (true) {
			// 接收消息，参数：接收消息的超时时间，为0的话则不超时，receive返回下一个消息，但是超时了或者消费者被关闭，返回null
			Message msg = consumer.receive(1000);
			if (msg != null && msg instanceof TestBean) {
				TestBean testbean = (TestBean) ((ActiveMQObjectMessage) msg).getObject();
				System.out.println(testbean.getAge());
				System.out.println(testbean.getName());
			}
		}
	}
}
