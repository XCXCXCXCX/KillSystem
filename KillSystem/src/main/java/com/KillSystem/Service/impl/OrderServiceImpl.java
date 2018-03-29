package com.KillSystem.Service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.KillSystem.DAO.impl.OrderDaoImpl;
import com.KillSystem.Service.OrderService;
import com.KillSystem.domain.Order;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("OrderServiceImpl")
public class OrderServiceImpl implements OrderService{

	@Autowired
	private OrderDaoImpl orderDaoImpl;
	
	@Override
	public int insert(Order t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Order order) {
		// TODO Auto-generated method stub
		return orderDaoImpl.delete(order);
	}

	@Override
	public int update(Order order) {
		// TODO Auto-generated method stub
		return orderDaoImpl.update(order);
	}

	@Override
	public List<Map<String, Order>> select(Order order) {
		// TODO Auto-generated method stub
		return orderDaoImpl.select(order);
	}

	@Override
	public int updateOrderState(Order order) {
		// TODO Auto-generated method stub
		return orderDaoImpl.updateOrderState(order);
	}

	@Override
	public PageInfo<Map<String, Order>> select(Order order, int pageNum, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Order>> orderMap = select(order);
	    PageInfo<Map<String, Order>> p=new PageInfo<Map<String, Order>>(orderMap);
	    return p;
	}

}
