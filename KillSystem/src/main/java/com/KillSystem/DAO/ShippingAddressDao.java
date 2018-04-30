package com.KillSystem.DAO;

import java.util.List;
import java.util.Map;

import com.KillSystem.domain.ShippingAddress;
import com.KillSystem.domain.User;

/**
 * @author xcxcxcxcx
 * 
 * @Comments
 * 收货地址Dao
 * 增加了 根据用户id查询收货地址接口 和 根据收货地址id查询收货地址接口 
 * 
 * 2018年4月5日
 *
 */
public interface ShippingAddressDao extends BaseDao<ShippingAddress>{

	List<Map<String, ShippingAddress>> selectByuserid(User user);
	
	ShippingAddress selectByaddressid(ShippingAddress shippingAddress);
	
}
