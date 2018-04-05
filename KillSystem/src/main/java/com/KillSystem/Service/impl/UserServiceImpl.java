package com.KillSystem.Service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.KillSystem.DAO.impl.UserDaoImpl;
import com.KillSystem.Service.UserService;
import com.KillSystem.domain.User;


/**
 * @author xcxcxcxcx
 * 
 * 用户服务实现类
 * 
 * 提供了用户的登陆、注册、登出、检验管理员身份接口
 * 
 * 2018年4月5日
 *
 */
@Service("UserService")
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserDaoImpl userDaoImpl;
	
	@Override
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

	@Override
	public User checkAdminRole(String username, String passwd) {
		// TODO Auto-generated method stub
		if("admin".equals(username)&&"admin".equals(passwd)) {
			User user = new User();
			user.setUsername(username);
			user.setPasswd(passwd);
			return user;
		}
		return null;
	}

	@Override
	public int register(User user) {
		// TODO Auto-generated method stub
		if(userDaoImpl.selectByTel_num(user)==null&&userDaoImpl.insert(user)==1) {
			return 1;
		}
		return 0;
	}
	
}
