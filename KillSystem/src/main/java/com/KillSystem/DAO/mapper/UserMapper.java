package com.KillSystem.DAO.mapper;

import java.util.List;
import java.util.Map;

import com.KillSystem.domain.Goods;
import com.KillSystem.domain.User;

public interface UserMapper {
	User login(User user);
	int insert(User user);
	List<Map<String, User>> select(User user);
	User selectByTel_num(User user);
}
