package com.KillSystem.Service;

import java.util.Map;

import com.KillSystem.Service.BaseService;
import com.KillSystem.common.ServerResponse;
import com.KillSystem.domain.Order;
import com.github.pagehelper.PageInfo;

import redis.clients.jedis.Jedis;

public interface OrderService extends BaseService<Order>{
	int updateOrderState(Order order);
	PageInfo<Map<String,Order>> select(Order order,int pageNum,int pageSize);
	long createOrderInRedis(Order order);
	long deleteOrderInRedis(Order order);
	long createPayInRedis(Order order);
	long deletePayInRedis(Order order);
	String updateOrderPayInRedis(Order order);
	boolean orderIsExist(Order order);
	String getPayState(Order order);
	ServerResponse pay(Order order,String path);
	ServerResponse aliCallback(Map<String, String> params);
}
