package com.KillSystem.DAO.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.KillSystem.DAO.OrderDao;
import com.KillSystem.DAO.mapper.OrderMapper;
import com.KillSystem.domain.Goods;
import com.KillSystem.domain.Order;
import com.KillSystem.util.JedisPoolManager;
import com.KillSystem.util.JedisUtil;

import redis.clients.jedis.Jedis;

@Repository
public class OrderDaoImpl implements OrderDao {

	@Autowired
	private OrderMapper orderMapper;

	private Jedis jedis;

	public int insert(Order t) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int delete(Order order) {
		// TODO Auto-generated method stub
		return orderMapper.delete(order);
	}

	public int update(Order order) {
		// TODO Auto-generated method stub
		return orderMapper.update(order);
	}

	public int updateOrderState(Order order) {
		return orderMapper.updateOrderState(order);
	}

	public List<Map<String, Order>> select(Order order) {
		// TODO Auto-generated method stub
		return orderMapper.select(order);
	}

	public Order orderIsExist(Order order) {
		return orderMapper.selectByorderid(order);
	}

	public int createOrder(Order order) {
		// TODO Auto-generated method stub
		return orderMapper.createOrder(order);
	}

	public String createOrderInRedis(Order order) {
		jedis = JedisUtil.getConn();
		long obj = jedis.setnx(order.getOrder_id(), order.getTel_num() + "," + order.getAddress() + ","
				+ order.getGoods_id() + "," + order.getCreate_time() + "," + order.getIs_success());
		jedis.close();
		return obj == 1 ? order.getOrder_id() : null;
	}
	
	public String deleteOrderInRedis(Order order) {
		jedis = JedisUtil.getConn();
		long obj = jedis.del(order.getOrder_id());
		jedis.close();
		return obj == 1 ? order.getOrder_id() : null;
	}

	public String createOrderPayInRedis(Order order) {
		try {
			jedis = JedisUtil.getConn();
			long obj = jedis.setnx(order.getOrder_id() + "_pay", "0");
			if(obj == 1) {
				return order.getOrder_id() + "_pay创建成功";
			}
			return order.getOrder_id() + "_pay已存在";
		}finally {
			jedis.close();
		}
	}
	
	public String deleteOrderPayInRedis(Order order) {
		try {
			jedis = JedisUtil.getConn();
			long obj = jedis.del(order.getOrder_id() + "_pay");
			if(obj == 1) {
				return order.getOrder_id() + "_pay删除成功";
			}
			return order.getOrder_id() + "_pay不存在";
		}finally {
			jedis.close();
		}
	}
	
	public String updateOrderPayInRedis(Order order) {
		try {
			jedis = JedisUtil.getConn();
			String obj = jedis.getSet(order.getOrder_id() + "_pay", "1");
			if (obj == "1") {
				return order.getOrder_id() + "_pay已支付";
			} else if (obj == "0") {
				return order.getOrder_id() + "_pay支付成功";
			} else {
				jedis.del(order.getOrder_id() + "_pay");
				return order.getOrder_id() + "_pay不存在";
			}
		} finally { 
			jedis.close();
		}
	}
	
}
