package com.KillSystem.DAO;

import java.util.List;
import java.util.Map;

import com.KillSystem.domain.ShippingAddress;
import com.KillSystem.domain.User;

public interface ShippingAddressDao extends BaseDao<ShippingAddress>{

	List<Map<String, ShippingAddress>> selectByuserid(User user);
	
	ShippingAddress selectByaddressid(ShippingAddress shippingAddress);
	
}
