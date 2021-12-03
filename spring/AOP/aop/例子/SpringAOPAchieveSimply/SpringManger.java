/**
 * 
 */
package com.mybank.fundtrans.test;

import org.springframework.aop.framework.ProxyFactory;

import com.sun.prism.impl.Disposer.Target;

import org.aopalliance.aop.Advice;;

/**
 * @author Administrator
 *
 */
public class SpringManger {


	/**
	 * 
	 */
	public SpringManger() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringTarget target=new SpringTarget();//创建目标对象
		
		ProxyFactory di=new ProxyFactory();//创建代理工厂
		di.addAdvice(new SpringLogger());//添加建议即添加通知，
		di.setTarget(target);//添加目标对象
		
		SpringTarget targetProxy=(SpringTarget)di.getProxy();//获得代理器，即获得已经被添加了通知的目标对象
		targetProxy.execute();//执行修改过目标对象方法
	}

}
