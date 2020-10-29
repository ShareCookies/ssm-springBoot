package com.mybank.fundtrans.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mybank.fundtrans.test.UserDaoForSpringAndHibernate;


public class SpringWithHibernateTest {

	public SpringWithHibernateTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//获取ioc容器
		ClassPathXmlApplicationContext factory=new ClassPathXmlApplicationContext("applicationContext3.xml");
		
		//获取bean，该bean实现了以hibernate对数据库的访问
		UserDaoForSpringAndHibernate userDao=(UserDaoForSpringAndHibernate)factory.getBean("userDaoForSpringAndHibernate");//可用来获取所有已经配置aa的javaBean的实例
		userDao.findByName();
		//System.out.println("run here");
	}

}
