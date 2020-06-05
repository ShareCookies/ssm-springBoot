/**
 * 
 */
package com.mybank.fundtrans.test;

import java.util.Date;

import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * @author Administrator
 *
 */
public class SpringDaoTest {

	/**
	 * 
	 */
	public SpringDaoTest() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//获取ioc容器
		ClassPathXmlApplicationContext factory=new ClassPathXmlApplicationContext("applicationContext4.xml");
		
		com.mybank.fundtrans.domain.User user=(com.mybank.fundtrans.domain.User)factory.getBean("user");//可用来获取所有已经配置aa的javaBean的实例
		user.setId(12);
		user.setName("springDao");
		user.setPassWord("springDao");
		user.setCreateTime(new Date());
		user.setRealName("springDao");
		
		UserDaoSpringImpl userDaoSpringImpl=(UserDaoSpringImpl)factory.getBean("userDaoSpringImpl");//可用来获取所有已经配置aa的javaBean的实例
		userDaoSpringImpl.insert(user);
		
		System.out.println("run here");
	}

}
