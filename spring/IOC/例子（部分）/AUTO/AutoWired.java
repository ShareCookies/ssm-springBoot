/**
 * 
 */
package com.mybank.fundtrans.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoaderListener;

import com.mybank.fundtrans.domain.User;

/**
 * @author Administrator
 *
 */
public class AutoWired{

	/**
	 * 
	 */
	//bean自动装配。用容器获取autoWired的Bean即可测试是否获得是否自动装配成功
	@Autowired
	private User user;
	
	public AutoWired() {
		// TODO Auto-generated constructor stub
	}
	public void outAutoWiredProperty(){
		System.out.println(user);
	}

}
