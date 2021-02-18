package com.china.hcg.mq.simplemessagemodelforspringboot;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @description 创建消息生产者Sender
 */
//@Component
public class Sender {
	//通过注入AmqpTemplate接口的实例来实现消息的发送，AmqpTemplate接口定义了一套针对AMQP协议的基础操作,在Spring Boot中会根据配置来注入其具体实现。
	@Autowired
	private AmqpTemplate rabbitTemplate;

	//在该生产者，我们会产生一个字符串，并发送到名为hello的队列中。
	public void send() {
		String context = "hello " + new Date();
		System.err.println("===============Sender : " + context);
		this.rabbitTemplate.convertAndSend("hello", context);
	}
}
