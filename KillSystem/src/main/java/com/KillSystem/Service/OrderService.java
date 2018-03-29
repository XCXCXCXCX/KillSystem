package com.KillSystem.Service;

import java.util.Map;

import com.KillSystem.Service.BaseService;
import com.KillSystem.domain.Order;
import com.github.pagehelper.PageInfo;

public interface OrderService extends BaseService<Order>{
	int updateOrderState(Order order);
	PageInfo<Map<String,Order>> select(Order order,int pageNum,int pageSize);
}
