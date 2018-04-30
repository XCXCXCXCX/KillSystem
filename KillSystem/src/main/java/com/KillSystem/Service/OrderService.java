package com.KillSystem.Service;

import java.util.Map;

import com.KillSystem.Service.BaseService;
import com.KillSystem.common.ServerResponse;
import com.KillSystem.domain.Order;
import com.github.pagehelper.PageInfo;


/**
 * @author xcxcxcxcx
 * 
 * @Comments
 * 订单服务
 * 提供了管理员的订单分页查询、获取支付订单状态、验证订单是否存在、订单支付、支付宝回调等接口
 * 
 * 2018年4月5日
 *
 */
public interface OrderService extends BaseService<Order>{
	int updateOrderState(Order order);
	PageInfo<Map<String,Order>> select(Order order,int pageNum,int pageSize);
	long createOrderInRedis(final Order order);
	long createPayInRedis(Order order);
	String updateOrderPayInRedis(Order order);
	boolean orderIsExist(Order order);
	boolean orderIsExistInRedis(Order order);
	String getPayState(Order order);
	ServerResponse pay(Order order,String path);
	ServerResponse aliCallback(Map<String, String> params);
	boolean orderIsExistByTelnumAndGoodsId(Order order);
}
