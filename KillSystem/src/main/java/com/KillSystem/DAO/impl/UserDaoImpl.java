package com.KillSystem.DAO.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.KillSystem.DAO.UserDao;
import com.KillSystem.DAO.mapper.UserMapper;
import com.KillSystem.domain.Goods;
import com.KillSystem.domain.User;

@Repository
public class UserDaoImpl implements UserDao{

	@Autowired
	private UserMapper userMapper;
	
	public int insert(User user) {
		// TODO Auto-generated method stub
		return userMapper.insert(user);
	}

	public int delete(User user) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int update(User user) {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<Map<String, User>> select(User user) {
		// TODO Auto-generated method stub
		return userMapper.select(user);
	}

	public User login(User user) {
		// TODO Auto-generated method stub
		return userMapper.login(user);
	}

	public User selectByTel_num(User user) {
		// TODO Auto-generated method stub
		return userMapper.selectByTel_num(user);
	}
	
}
