/**
 * 
 */
package com.mybank.fundtrans.test;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;


//import org.springframework.cglib.proxy.MethodInterceptor;区别
//spring在3.0及其以上版本就不再完整的将依赖打包

/**
 * @author Administrator
 *
 */
public class SpringLogger implements MethodInterceptor {

	/**
	 * 
	 */
	public SpringLogger() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object invoke(MethodInvocation arg0) throws Throwable {
		// TODO Auto-generated method stub
		before();//执行前置通知
		arg0.proceed();//继续执行目标对象的方法
		return null;
	}
	private void before() {
		System.out.println("程序开始了");
	}
}
