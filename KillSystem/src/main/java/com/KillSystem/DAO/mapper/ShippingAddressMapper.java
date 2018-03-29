package com.KillSystem.DAO.mapper;

import java.util.List;
import java.util.Map;

import com.KillSystem.domain.ShippingAddress;
import com.KillSystem.domain.User;

public interface ShippingAddressMapper {
	int insert(ShippingAddress shippingAddress);
	int delete(ShippingAddress shippingAddress);
	List<Map<String, ShippingAddress>> selectByuserid(User user);
	ShippingAddress selectByaddressid(ShippingAddress shippingAddress);
}
