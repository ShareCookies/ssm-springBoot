/**
 * 
 */
package com.mybank.fundtrans.test;

import java.lang.reflect.Method;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.StaticMethodMatcherPointcut;



/**
 * @author Administrator
 *
 */
public class MyPointCut extends StaticMethodMatcherPointcut {

	/**
	 * 
	 */
	public MyPointCut() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean matches(Method arg0, Class<?> arg1) {
		// TODO Auto-generated method stub
		return ("execute".equals(arg0.getName()));//规定execute()为切入点
		//return false;
	}
	
	@Override
	public ClassFilter getClassFilter() {
		// TODO Auto-generated method stub
		//return super.getClassFilter();
		return new ClassFilter() {
			@Override
			public boolean matches(Class<?> arg0) {
				// TODO Auto-generated method stub
				return (arg0==SpringTarget.class);//规定只匹配SpringTarget类。该方法可不实现
				//return (arg0==test.class);
			}
		};
	}
}
