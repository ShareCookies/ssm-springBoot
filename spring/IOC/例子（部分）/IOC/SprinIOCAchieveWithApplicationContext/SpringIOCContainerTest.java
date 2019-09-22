package com.mybank.fundtrans.test;


import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SpringIOCContainerTest {

	public SpringIOCContainerTest() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	 	//获取ApplicatonContext容器
	 	//ApplicatonContext接口三个实现类之一
	 	ClassPathXmlApplicationContext factory=new ClassPathXmlApplicationContext("applicationContext2.xml");
	
		User user=(User)factory.getBean("user");//从工厂获取bean对象引用
		System.out.println(user.toString());
		//System.out.println("Is it run here");
	}
}
