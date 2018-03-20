package com.KillSystem.DAO.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.KillSystem.DAO.GoodsDao;
import com.KillSystem.DAO.mapper.GoodsMapper;
import com.KillSystem.domain.Goods;

@Repository
public class GoodsDaoImpl implements GoodsDao{

	@Autowired
	private GoodsMapper goodsMapper;
	
	@Override
	public int insert(Goods goods) {
		// TODO Auto-generated method stub
		return goodsMapper.insert(goods);
	}

	@Override
	public int delete(Goods goods) {
		// TODO Auto-generated method stub
		return goodsMapper.delete(goods);
	}

	@Override
	public int update(Goods goods) {
		// TODO Auto-generated method stub
		return goodsMapper.update(goods);
	}

	public List<Map<String, Goods>> select(Goods goods) {
		// TODO Auto-generated method stub
		return goodsMapper.select(goods);
	}

	public Map<String, Goods> selectById(Goods goods){
		return goodsMapper.selectById(goods);
	}

	public Goods getGoodsById(Goods goods) {
		return goodsMapper.getGoodsById(goods);
	}
	
	public int updateGoodsStock(int goods_id) {
		return goodsMapper.updateGoodsStock(goods_id);
	}

	public int updateGoodsStockBack(int goods_id) {
		// TODO Auto-generated method stub
		return goodsMapper.updateGoodsStockback(goods_id);
	}
	
}
