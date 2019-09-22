/**
 * 
 */
package com.mybank.fundtrans.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoaderListener;

import com.mybank.fundtrans.domain.User;

/**
 * @author Administrator
 *
 */
@WebServlet("/ServletTest")
public class ServletTest extends HttpServlet {

	/**
	 * 
	 */

	


	public ServletTest() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//super.doGet(req, resp);
		//简易获取bean的方式之一，使用监听器
		//User user=(User)ContextLoaderListener.getCurrentWebApplicationContext().getBean("user");
		AutoWired autoWired=(AutoWired)ContextLoaderListener.getCurrentWebApplicationContext().getBean("autoWired");
		autoWired.outAutoWiredProperty();
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
	

}
