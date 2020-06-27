/**
 * 
 */
package com.mybank.fundtrans.test;

import java.util.Date;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mybank.fundtrans.domain.User;

/**
 * @author Administrator
 *
 */
public class SpringTransactionTest {

	/**
	 * 
	 */
	public SpringTransactionTest() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//获取ioc容器
		ClassPathXmlApplicationContext factory=new ClassPathXmlApplicationContext("applicationContext5.xml");
		
		User user=(User)factory.getBean("user");//可用来获取所有已经配置aa的javaBean的实例
		user.setId(12);
		user.setName("springDao");
		user.setPassWord("springDao");
		user.setCreateTime(new Date());
		user.setRealName("springDao");
		
		//UserDaoSpringImplWithTransaction springTransaction=(UserDaoSpringImplWithTransaction)factory.getBean("userDaoSpringImplWithTransaction");//可用来获取所有已经配置aa的javaBean的实例
		UserDaoSpringImpl springTransaction=(UserDaoSpringImpl)factory.getBean("transactionProxy");
		springTransaction.insert(user);
		System.out.println("run here");
	}
}
