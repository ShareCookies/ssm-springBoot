package com.china.hcg.mq.simplemessagemodel;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @autor hecaigui
 * @date 2020-12-22
 * @description
 */
public class ConnectionUtil {
	/**
	 * 建立与RabbitMQ的连接
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception {
		//定义连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		//设置服务地址
		factory.setHost("192.168.0.40");
		//端口
		factory.setPort(5672);
		//设置账号信息，用户名、密码、vhost
		factory.setVirtualHost("/ex");//设置虚拟机，一个mq服务可以设置多个虚拟机，每个虚拟机就相当于一个独立的mq
		factory.setUsername("guest");
		factory.setPassword("guest");
		// 通过工厂获取连接
		Connection connection = factory.newConnection();
		return connection;
	}
}