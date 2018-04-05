package com.KillSystem.DAO;

import com.KillSystem.domain.User;

/**
 * @author xcxcxcxcx
 * 
 * 用户Dao
 * 增加了 登陆接口、 根据tel_num查询用户接口
 * 
 * 注册服务是利用了insert接口和selectByTel_num接口实现的
 * 
 * 2018年4月5日
 *
 */
public interface UserDao extends BaseDao<User>{

	User login(User user);

	User selectByTel_num(User user);

	
}
