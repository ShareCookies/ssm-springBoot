/**
 * 
 */
package com.mybank.fundtrans.test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.mybank.fundtrans.dao.UserDao;
import com.mybank.fundtrans.domain.User;



/**
 * @author Administrator
 *
 */
public class UserDaoSpringImpl {


	
	private DataSource dataSource;
	
	
	public void insert(User user) {
		// TODO Auto-generated method stub
		Connection connection;
		Statement statement;
	
/*			该部分代码都得try catch起来如何实现事务回滚了？
 			connection=dataSource.getConnection();
			statement=connection.createStatement();
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
			*/

			Date date=new Date(user.getCreateTime().getTime());
		    JdbcTemplate template = new JdbcTemplate(dataSource);

	        template.execute("INSERT INTO user(id, name, password, createTime, realname) "
					+ "VALUES('"+user.getId()+"','"+user.getName()+"','"+user.getPassWord()+
					"','"+date+"','"+user.getRealName()+"')");
	      //为了测试事务是否会回滚而设置的案例
	        int a=0;
			a=9/a;

	        template.execute("INSERT INTO user(id, name, password, createTime, realname) "
					+ "VALUES('"+11+"','"+"11"+"','"+user.getPassWord()+
					"','"+date+"','"+user.getRealName()+"')");
			System.out.println("操作执行成功");

	}

	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
