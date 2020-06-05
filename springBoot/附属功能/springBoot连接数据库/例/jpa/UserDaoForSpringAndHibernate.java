/**
 * 
 */
package com.mybank.fundtrans.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.mybank.fundtrans.domain.User;
import com.mybank.fundtrans.util.HibernateUtil;

/**
 * @author Administrator
 *
 */
public class UserDaoForSpringAndHibernate {
	
	//往该属性中注入hibernate的会话工厂
	//应用的整个生命周期只保存一个生命周期实例即可！！！
	private SessionFactory sessionFactory;
	
	
	/**
	 * 
	 */
	public UserDaoForSpringAndHibernate() {
		// TODO Auto-generated constructor stub
	}
	public void findByName() {
		Session session = this.getSession();  
	    User userHibernate=(User)session.get(User.class, new Integer("2"));//通过session进行查询获取数据
	    System.out.println(userHibernate.getId());
	    System.out.println(userHibernate.getName());
		
	}
	/*
	 * 获取session对象
	 */
	protected Session getSession() {
		return sessionFactory.openSession(); //开启Session。注：open开的session要关闭
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	
}
