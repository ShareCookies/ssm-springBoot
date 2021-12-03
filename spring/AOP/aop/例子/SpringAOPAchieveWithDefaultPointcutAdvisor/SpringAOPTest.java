/**
 * 
 */
package com.mybank.fundtrans.test;

import javax.swing.Spring;

import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mybank.fundtrans.dao.UserDaoForSpringAndHibernate;


/**
 * @author Administrator
 *
 */
public class SpringAOPTest {

	/**
	 * 
	 */
	public SpringAOPTest() {
		// TODO Auto-generated constructor stub
		
	}
	public static void main(String[] args) {
		//new DefaultPointcutAdvisor();
		MyPointCut myPointCut=new MyPointCut();//实例化切点类，即在该类中规定哪些方法为切入点，还可指定该切入点只适用于某些目标对象。
		SpringAdvice springAdvice=new SpringAdvice();//实例化通知
		
		DefaultPointcutAdvisor advisor=new DefaultPointcutAdvisor(myPointCut, springAdvice);//实例化切入点配置器，并传递参数:通知类,切点类
		
		
		//代理工厂
		SpringTarget target=new SpringTarget();//实例化目标对像
		ProxyFactory proxyFactory=new ProxyFactory();//创建代理工厂实例
		//为代理工厂目标对象和切面（顾问）
		proxyFactory.setTarget(target);
		proxyFactory.addAdvisor(advisor);
		
		SpringTarget targetProxy=(SpringTarget)proxyFactory.getProxy();//获取添加过切面的目标对象
		targetProxy.execute();
		//targetProxy.test();//该方法没有设为切入点，所以不会运行时不会执行通知
	}

	
}
