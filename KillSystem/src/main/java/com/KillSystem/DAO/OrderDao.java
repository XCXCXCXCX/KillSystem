package com.KillSystem.DAO;

import java.util.Map;

import com.KillSystem.domain.Goods;
import com.KillSystem.domain.Order;

/**
 * @author xcxcxcxcx
 * 
 * @Comments
 * 订单Dao
 * 
 * 2018年4月5日
 *
 */
public interface OrderDao extends BaseDao<Order>{

	int updateOrderState(Order order);

	long createOrderInRedis(final Order order);

	long createPayInRedis(Order order);

	String updateOrderPayInRedis(Order order);

	boolean orderIsExist(Order order);

	boolean orderIsExistInRedis(Order order);

	String getPayState(Order order);

	int createOrder(Order order);

	Order selectByorderIdInRedis(Order order);

	boolean createOrderAndupdateGoodsStock(Order order, Goods goods);

	void setPayStateFailed(Order order);

	Order selectBytelnumAndgoodsid(String tel_num, int goods_id);
	
}
