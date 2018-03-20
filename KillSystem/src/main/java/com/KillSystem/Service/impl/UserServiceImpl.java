package com.KillSystem.Service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.KillSystem.DAO.impl.UserDaoImpl;
import com.KillSystem.Service.UserService;
import com.KillSystem.domain.User;

@Service("UserServiceImpl")
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserDaoImpl userDaoImpl;
	
	public User login(User user) {
		
		return userDaoImpl.login(user);
	}

	@Override
	public int insert(User t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(User t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(User t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Map<String, User>> select(User t) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
