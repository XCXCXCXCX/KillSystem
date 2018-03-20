package com.KillSystem.DAO;

import java.util.Map;

import com.KillSystem.domain.Goods;
import com.KillSystem.domain.User;

public interface UserDao extends BaseDao<User>{

	User login(User user);

	int insert(User user);

	int delete(User user);

	int update(User user);

	
}
