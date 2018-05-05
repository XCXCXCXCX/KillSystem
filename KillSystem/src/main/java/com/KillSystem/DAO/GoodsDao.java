package com.KillSystem.DAO;

import java.util.List;
import java.util.Map;

import com.KillSystem.domain.Goods;
import com.KillSystem.domain.Order;

/**
 * @author xcxcxcxcx
 * 
 * @Comments
 * 商品Dao
 * 
 * 2018年4月5日
 *
 */
public interface GoodsDao extends BaseDao<Goods>{

	List<Map<String, Goods>> selectById(Goods goods);

	Goods getGoodsById(Goods goods);

	long setGoodsStock(Order order);

	long setBackGoodsStock(Order order);

	boolean checkGoodsStockInRedis(Order order);

	void initGoodsStock();

	int updateGoodsStockBack(Goods goods);

	int updateGoodsStock(Goods goods);

	int countGoods();

	int updateGoodsImgurl(int goods_id, String path);


	

}
