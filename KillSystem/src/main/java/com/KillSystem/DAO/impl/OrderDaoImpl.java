package com.KillSystem.DAO.impl;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.KillSystem.DAO.OrderDao;
import com.KillSystem.DAO.mapper.OrderMapper;
import com.KillSystem.domain.Order;
import com.KillSystem.util.JedisUtil;
import redis.clients.jedis.Jedis;


/**
 * @author xcxcxcxcx
 * 
 * 订单Dao实现类
 * Dao接口只是实现基本功能
 * 需要添加功能时，直接在这里添加实现方法。
 * 
 * 2018年4月5日
 *
 */
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

	public boolean orderIsExist(Order order) {
		return orderMapper.selectByorderid(order)==null ? false : true;
	}
	
	public boolean orderIsExistInRedis(Order order) {
		try {
			jedis = JedisUtil.getConn();
			return jedis.get(order.getOrder_id()) == "nil" ? false : true;
		}finally {
			if(jedis == null) {
				jedis.close();
			}else {
				JedisUtil.returnConn(jedis);
			}
		}
	}

	public int createOrder(Order order) {
		// TODO Auto-generated method stub
		return orderMapper.createOrder(order);
	}

	public long createOrderInRedis(Order order) {
		try {
			jedis = JedisUtil.getConn();
			return jedis.setnx(order.getOrder_id(), order.getTel_num() + "," + order.getAddress() + ","
					+ order.getGoods_id() + "," + DateTime.now().toString("YYYY-MM-dd HH-mm-ss"));
		}finally {
			if(jedis.expire(order.getOrder_id(), 180)!=1) {
				System.out.println("设置超时时间失败！");
			}
			if(jedis == null) {
				jedis.close();
			}else {
				JedisUtil.returnConn(jedis);
				System.out.println("创建订单的时候归还了连接！");
			}
			
		}
	}

	public long createPayInRedis(Order order) {
		try {
			jedis = JedisUtil.getConn();
			return jedis.setnx(order.getOrder_id() + "_pay", "0");
		}finally {
			if(jedis.expire(order.getOrder_id() + "_pay", 300) != 1) {
				System.out.println("支付订单设置有效时间失败！");
			}
			if(jedis == null) {
				jedis.close();
			}else {
				JedisUtil.returnConn(jedis);
			}
			
		}
	}
	
	
	public String updateOrderPayInRedis(Order order) {
		try {
			jedis = JedisUtil.getConn();
			String obj = jedis.getSet(order.getOrder_id() + "_pay", "1");
			if ("1".equals(obj)) {
				return "已支付";
			} else if ("0".equals(obj)) {
				return "支付成功";
			} else {
				jedis.del(order.getOrder_id() + "_pay");
				return "不存在";
			}
		} finally { 
			if(jedis == null) {
				jedis.close();
			}else {
				JedisUtil.returnConn(jedis);
			}
		}
	}
	
	public String getPayState(Order order) {
		try {
			jedis = JedisUtil.getConn();
			return jedis.get(order.getOrder_id() + "_pay");
		}finally { 
			if(jedis == null) {
				jedis.close();
			}else {
				JedisUtil.returnConn(jedis);
			}
		}
	}
	
}
