package com.KillSystem.DAO.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.KillSystem.DAO.OrderDao;
import com.KillSystem.DAO.mapper.OrderMapper;
import com.KillSystem.domain.Goods;
import com.KillSystem.domain.Order;

@Repository
public class OrderDaoImpl implements OrderDao{

	@Autowired
	private OrderMapper orderMapper;
	
	
	public int insert(Order t) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public int delete(Order t) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public int update(Order t) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public List<Map<String, Goods>> select(Order t) {
		// TODO Auto-generated method stub
		return null;
	}

	public int createOrder(Order order) {
		// TODO Auto-generated method stub
		return orderMapper.createOrder(order);
	}

}
