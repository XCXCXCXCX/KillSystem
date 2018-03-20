package com.KillSystem.DAO.mapper;

import java.util.Map;

import com.KillSystem.domain.Order;

public interface OrderMapper {
	int delete(Order order);
	int update(Order order);
	int updateOrderState(Order order);
	int createOrder(Order order);
	Map<String,Order> select(Order order);
	Order selectByorderid(Order order);
	
}
