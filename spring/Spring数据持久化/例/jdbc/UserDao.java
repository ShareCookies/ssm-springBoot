package com.mybank.fundtrans.dao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import com.mybank.fundtrans.domain.Fund;
import com.mybank.fundtrans.domain.User;
/**
* Description: TODO
* @author 
* @date 2017年2月25日 下午2:57:56
 */
public interface UserDao {
	List findAll();    //获取所有的用户记录,返回User对象列表
	//String findAll();
	void insert(User user);    //插入一条用户记录
	void delete(int fundNo);    //按主键删除一条基金记录
	void update(User user);    //更新一条用户记录
	User findByName(String userName,String userPassword);  //按账号名查找用户，返回User对象
}
