package com.KillSystem.DAO.mapper;

import java.util.List;
import java.util.Map;

import com.KillSystem.domain.Goods;

public interface GoodsMapper {
	int insert(Goods goods);
	int delete(Goods goods);
	int update(Goods goods);
	List<Map<String, Goods>> select(Goods goods);
	List<Map<String, Goods>> selectById(Goods goods);
	Goods getGoodsById(Goods goods);
	int updateGoodsStock(int goods_id);
	int updateGoodsStockback(int goods_id);
}
