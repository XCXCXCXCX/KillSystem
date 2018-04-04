package com.KillSystem.Service;

import java.util.Map;

import com.KillSystem.domain.Goods;
import com.KillSystem.domain.Order;
import com.github.pagehelper.PageInfo;

import redis.clients.jedis.Jedis;

public interface GoodsService extends BaseService<Goods>{
	PageInfo<Map<String, Goods>> select(Goods goods,int pageNum,int pageSize);
	PageInfo<Map<String, Goods>> selectById(Goods goods,int pageNum,int pageSize);
	Goods getGoodsById(Goods goods);
	long decrGoodsStock(Order order);
	long incrGoodsStock(Order order);
}
