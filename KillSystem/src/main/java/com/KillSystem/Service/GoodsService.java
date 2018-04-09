package com.KillSystem.Service;

import java.util.Map;

import com.KillSystem.domain.Goods;
import com.KillSystem.domain.Order;
import com.github.pagehelper.PageInfo;

/**
 * @author xcxcxcxcx
 * 
 * @Comments
 * 商品服务接口
 * 包括管理员查询商品的分页和用户查询商品的分页等
 * 
 * 2018年4月5日
 *
 */
public interface GoodsService extends BaseService<Goods>{
	PageInfo<Map<String, Goods>> select(Goods goods,int pageNum,int pageSize);
	PageInfo<Map<String, Goods>> selectById(Goods goods,int pageNum,int pageSize);
	Goods getGoodsById(Goods goods);
	long decrGoodsStock(Order order);
	long incrGoodsStock(Order order);
	boolean checkGoodsStockInRedis(Order order);
}
