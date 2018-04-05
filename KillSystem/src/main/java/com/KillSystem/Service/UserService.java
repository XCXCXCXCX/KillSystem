package com.KillSystem.Service;

import com.KillSystem.domain.User;


/**
 * @author xcxcxcxcx
 * 
 * 用户服务
 * 提供 普通用户登陆、注册 和 admin用户的检验 接口
 * 
 * 2018年4月5日
 *
 */
public interface UserService extends BaseService<User>{

	User login(User user);
	
	User checkAdminRole(String username,String passwd);

	int register(User user);
}
