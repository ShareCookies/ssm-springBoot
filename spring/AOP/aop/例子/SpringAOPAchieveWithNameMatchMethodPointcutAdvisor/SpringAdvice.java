/**
 * 
 */
package com.mybank.fundtrans.test;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import sun.management.snmp.util.MibLogger;

/**
 * @author Administrator
 *
 */
public class SpringAdvice implements MethodInterceptor{

	/**
	 * 
	 */
	public SpringAdvice() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object invoke(MethodInvocation arg0) throws Throwable {
		// TODO Auto-generated method stub
	
		System.out.println("执行前置通知");//执行前置通知
		Object val=arg0.proceed();
		System.out.println("执行后置通知");//执行后置通知
		
		return val;//返回对象有啥用？
		//return null;
	}



}
