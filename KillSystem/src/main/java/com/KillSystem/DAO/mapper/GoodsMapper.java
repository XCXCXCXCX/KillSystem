package com.KillSystem.DAO.mapper;

import java.util.List;
import java.util.Map;

import com.KillSystem.domain.Goods;

/**
 * @author xcxcxcxcx
 * 
 * @Comments
 * 商品mapper
 * 连接mybatis的GoodsMapper.xml文件
 * 
 * 2018年4月5日
 *
 */
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
