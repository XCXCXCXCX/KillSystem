package com.KillSystem.DAO.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.KillSystem.domain.Goods;

public interface GoodsMapper {
	int insert(Goods goods);
	int delete(Goods goods);
	int update(Goods goods);
	List<Map<String, Goods>> select(Goods goods);
	List<Map<String, Goods>> selectById(Goods goods);
	Goods getGoodsById(Goods goods);
	int updateGoodsStock(Goods goods);
	int updateGoodsStockback(Goods goods);
}
