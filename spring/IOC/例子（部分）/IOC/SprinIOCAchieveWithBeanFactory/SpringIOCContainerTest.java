package com.mybank.fundtrans.test;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class SpringIOCContainerTest {

	public SpringIOCContainerTest() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	 	//获取BeanFactory容器
	 	Resource resource=(Resource) new ClassPathResource("applicationContext2.xml");
		//BeanFactory接口实现类之一
		XmlBeanFactory factory=new XmlBeanFactory(resource);
	
		User user=(User)factory.getBean("user");//从工厂获取bean对象引用
		System.out.println(user.toString());
		//System.out.println("Is it run here");
	}
}
