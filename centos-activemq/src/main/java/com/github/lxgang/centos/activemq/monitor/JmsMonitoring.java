package com.github.lxgang.centos.activemq.monitor;

import java.io.IOException;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JmsMonitoring {
	
	public static Logger logger = LoggerFactory.getLogger(JmsMonitoring.class);

	public static void main(String[] args) throws IOException, MalformedObjectNameException {
		JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://192.168.1.175:2011/jmxrmi");
		JMXConnector connector = JMXConnectorFactory.connect(url, null);
		connector.connect();
		MBeanServerConnection connection = connector.getMBeanServerConnection();

		// 需要注意的是，这里的org.apache.activemq必须和上面配置的名称相同
		ObjectName name = new ObjectName("org.apache.activemq:type=Broker,brokerName=localhost");
		BrokerViewMBean mBean = (BrokerViewMBean) MBeanServerInvocationHandler.newProxyInstance(connection, name,
				BrokerViewMBean.class, true);
		
		for (ObjectName queueName : mBean.getQueues()) {
			QueueViewMBean queueMBean = (QueueViewMBean) MBeanServerInvocationHandler.newProxyInstance(connection,
					queueName, QueueViewMBean.class, true);
			logger.info("\n------------------------------\n");

			// 消息队列名称
			logger.info("States for queue --- " + queueMBean.getName());

			// 队列中剩余的消息数
			logger.info("Size --- " + queueMBean.getQueueSize());

			// 消费者数
			logger.info("Number of consumers --- " + queueMBean.getConsumerCount());

			// 出队数
			logger.info("Number of dequeue ---" + queueMBean.getDequeueCount());
		}
	}
}
