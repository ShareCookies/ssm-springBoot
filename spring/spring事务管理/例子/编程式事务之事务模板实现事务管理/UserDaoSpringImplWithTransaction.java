/**
 * 
 */
package com.mybank.fundtrans.test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.mybank.fundtrans.domain.User;
import com.sun.media.jfxmediaimpl.platform.Platform;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;

/**
 * @author Administrator
 *
 */
public class UserDaoSpringImplWithTransaction {

	
	private DataSource dataSource;
	
	PlatformTransactionManager transactionManager;//注入事物管理器
	TransactionTemplate transactionTemplate;//注入TransactionTemplate模版
	/**
	 * 
	 */
	public UserDaoSpringImplWithTransaction() {
		// TODO Auto-generated constructor stub
	}
	public void insert(User user) {
		// TODO Auto-generated method stub
		transactionTemplate.execute(new TransactionCallback<Object>() {
			@Override
			public Object doInTransaction(TransactionStatus status) {
				// TODO Auto-generated method stub
/*				Connection connection;
				//Statement statement;
				try {
					connection=dataSource.getConnection();
					Statement statement=connection.createStatement();
					Date date=new Date(user.getCreateTime().getTime());
					//System.out.println(date);
					statement.execute("INSERT INTO user(id, name, password, createTime, realname) "
							+ "VALUES('"+user.getId()+"','"+user.getName()+"','"+user.getPassWord()+
							"','"+date+"','"+user.getRealName()+"')");
					
					//为了测试事务是否会回滚而设置的案例
					int a=0;
					a=9/a;
					statement.execute("INSERT INTO user(id, name, password, createTime, realname) "
							+ "VALUES('"+11+"','"+"11"+"','"+user.getPassWord()+
							"','"+date+"','"+user.getRealName()+"')");
					
					//transactionManager.commit(status);
					System.out.println("操作执行成功");
				} catch (SQLException e) {
					// TODO: handle exception
					
					//事物回滚失败？因为事务的回滚与提交权不在我们身上？我们不能catch住错误，那样事物模版就捕捉不到错误无法回滚了。那如何catch错误了？
					status.setRollbackOnly();
					//transactionManager.rollback(status);
					System.out.println("操作执行失败，事物回滚");
					//e.printStackTrace();
				}*/
				
				
				Date date=new Date(user.getCreateTime().getTime());
			    JdbcTemplate template = new JdbcTemplate(dataSource);

		        template.execute("INSERT INTO user(id, name, password, createTime, realname) "
						+ "VALUES('"+user.getId()+"','"+user.getName()+"','"+user.getPassWord()+
						"','"+date+"','"+user.getRealName()+"')");
		      //为了测试事务是否会回滚而设置的案例
/*		        int a=0;
				a=9/a;*/

		        template.execute("INSERT INTO user(id, name, password, createTime, realname) "
						+ "VALUES('"+12+"','"+"12"+"','"+user.getPassWord()+
						"','"+date+"','"+user.getRealName()+"')");
				System.out.println("操作执行成功");
				
				return null;
			}
		});
	}
	
	
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}
	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}
	

}
