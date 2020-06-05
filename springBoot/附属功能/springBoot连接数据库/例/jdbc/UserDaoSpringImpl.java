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

import com.mybank.fundtrans.dao.UserDao;
import com.mybank.fundtrans.domain.User;



/**
 * @author Administrator
 *
 */
public class UserDaoSpringImpl implements UserDao {



	private DataSource dataSource;
	
	/**
	 * 
	 */
	public UserDaoSpringImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.mybank.fundtrans.dao.UserDao#findAll()
	 */
	@Override
	public List findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.mybank.fundtrans.dao.UserDao#insert(com.mybank.fundtrans.domain.User)
	 */
	@Override
	public void insert(User user) {
		// TODO Auto-generated method stub
		Connection connection;
		Statement statement;
		try {
			connection=dataSource.getConnection();
			statement=connection.createStatement();
			Date date=new Date(user.getCreateTime().getTime());
			//System.out.println(date);
			statement.execute("INSERT INTO user(id, name, password, createTime, realname) "
					+ "VALUES('"+user.getId()+"','"+user.getName()+"','"+user.getPassWord()+
					"','"+date+"','"+user.getRealName()+"')");
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see com.mybank.fundtrans.dao.UserDao#delete(int)
	 */
	@Override
	public void delete(int fundNo) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.mybank.fundtrans.dao.UserDao#update(com.mybank.fundtrans.domain.User)
	 */
	@Override
	public void update(User user) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.mybank.fundtrans.dao.UserDao#findByName(java.lang.String, java.lang.String)
	 */
	@Override
	public User findByName(String userName, String userPassword) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
