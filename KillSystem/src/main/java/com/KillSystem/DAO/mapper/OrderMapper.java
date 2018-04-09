package com.KillSystem.DAO.mapper;

import java.util.List;
import java.util.Map;

import com.KillSystem.domain.Order;

/**
 * @author xcxcxcxcx
 * 
 * @Comments
 * 商品mapper
 * 连接mybatis的OrderMapper.xml文件
 * 
 * 2018年4月5日
 *
 */
public interface OrderMapper {
	int delete(Order order);
	int update(Order order);
	int updateOrderState(Order order);
	int createOrder(Order order);
	List<Map<String, Order>> select(Order order);
	Order selectByorderid(Order order);
	
}
