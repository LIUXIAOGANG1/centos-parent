package com.github.lxgang.centos.activemq.cronJob;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;

import com.github.lxgang.centos.activemq.util.ActiveMQUtils;
import com.github.lxgang.centos.activemq.util.ConnectMQ;
import com.github.lxgang.centos.activemq.util.TestBean;

public class MQCronJob {
	private static String brokerURL = "";

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException, java.text.ParseException {
		System.out.println("-----------------------------------开始测试MQ状态-----------------------------------");

		// 如果没有输入参数，提示输入参数
		if (args == null || args.length < 1 || (args.length == 1 && StringUtils.isBlank(args[0]))) {
			System.out.println("-----------------------------------请输入相应参数查询-----------------------------------");
			System.exit(0);
		}

		Options options = new Options();
		// 帮助
		options.addOption("h", false, "帮助");

		// 服务器地址
		options.addOption("ip", true, "MQ服务器地址");

		// MQ端口
		options.addOption("port", true, "MQ端口");

		// MQ账号
		options.addOption("user", true, "MQ账号");

		// 账号密码
		options.addOption("password", true, "账号密码");

		// 测试队列名称
		options.addOption("Qname", true, "测试队列名称");

		CommandLineParser parser = new PosixParser();
		CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e1) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("CheckMQ -ip 192.168.1.1 -port 61616 -user root -password root -Qname TestQueue",
					options);
			System.exit(0);
		}

		if (cmd.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("CheckMQ -ip 192.168.1.1 -port 61616 -user root -password root -Qname TestQueue",
					options);
			System.exit(0);
		}

		String ip = "";
		if (cmd.hasOption("ip")) {
			try {
				ip = cmd.getOptionValue("ip");
			} catch (Exception e) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("-ip 192.168.1.1", options);
				return;
			}
		}

		String port = "61616";
		if (cmd.hasOption("port")) {
			try {
				port = cmd.getOptionValue("port");
			} catch (Exception e) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("-port 61616", options);
				return;
			}
		}

		String user = null;
		if (cmd.hasOption("user")) {
			try {
				user = cmd.getOptionValue("user");
			} catch (Exception e) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("-user root", options);
				return;
			}
		}

		String password = null;
		if (cmd.hasOption("password")) {
			try {
				password = cmd.getOptionValue("password");
			} catch (Exception e) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("-password root", options);
				return;
			}
		}

		String Qname = null;
		if (cmd.hasOption("Qname")) {
			try {
				Qname = cmd.getOptionValue("Qname");
			} catch (Exception e) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("-Qname TestQueue", options);
				return;
			}
		}

		brokerURL = "failover:(nio://" + ip + ":" + port + ")?initialReconnectDelay=100";

		try {
			// 连接MQ
			Connection connection = null;

			// 通过线程连接，防止连接超时。
			Callable<Connection> connectMQ = new ConnectMQ(user, password, brokerURL);
			FutureTask<Connection> ft = new FutureTask<Connection>(connectMQ);
			new Thread(ft).start();

			System.out.println("准备休眠10秒！");
			Thread.sleep(10 * 1000);
			System.out.println("10秒休眠自唤醒！");

			if (!ft.isDone()) {
				System.out.println(brokerURL + "连接超时。");
				//TODO 发送报警
				System.exit(0);
			}

			connection = ft.get();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// 发送消息
			ActiveMQUtils.send2MQ(session, Qname);

			// 休眠5秒，为从MQ获得消息做准备
			System.out.println("准备休眠5秒，为向MQ中发送消息留够充足时间！");
			Thread.sleep(5 * 1000);

			// 准备从MQ中获取消息
			Destination destination = session.createQueue(Qname);
			MessageConsumer consumer = session.createConsumer(destination);

			boolean flag = false;

			while (true) {
				// 接收消息，参数：接收消息的超时时间，为0的话则不超时，receive返回下一个消息，但是超时了或者消费者被关闭，返回null
				Message msg = consumer.receive(1000);
				if (msg != null) {
					flag = true;
					if (msg instanceof ActiveMQObjectMessage) {
						TestBean testbean = (TestBean) ((ActiveMQObjectMessage) msg).getObject();
						System.out.println(testbean.getAge());
						System.out.println(testbean.getName());
					}
					if (msg instanceof TextMessage) {
						TextMessage textMessage = (TextMessage) msg;
						System.out.println("Received: " + textMessage.getText());
					}
				} else if (!flag) {
					session.close();
					connection.close();
					//TODO 发送报警
					System.exit(0);
				} else {
					session.close();
					connection.close();
					System.out.println(
							"-----------------------------------本次MQ状态测试完毕-----------------------------------");
					System.exit(0);
				}
			}
		} catch (Exception e) {
			// TODO 异常发送短信
			System.out.println("-----------------------------------mq异常-----------------------------------");
			System.exit(0);
		}
	}
}
