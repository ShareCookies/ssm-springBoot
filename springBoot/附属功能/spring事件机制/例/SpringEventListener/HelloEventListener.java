package com.china.hcg.spring.SpringEventListener;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class HelloEventListener implements ApplicationListener<HelloEvent> {
	@Override
	public void onApplicationEvent(HelloEvent event) {
		// 获取事件的参数
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.err.println("事件监听器开始执行.事实证明就是事件机制同步执行的，这里休眠了5s，发布事件的地方也不能运行了");
	}
}