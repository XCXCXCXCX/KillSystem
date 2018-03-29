package com.KillSystem.DAO.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.KillSystem.DAO.GoodsDao;
import com.KillSystem.DAO.mapper.GoodsMapper;
import com.KillSystem.domain.Goods;
import com.KillSystem.domain.Order;
import com.KillSystem.util.JedisUtil;

import redis.clients.jedis.Jedis;

@Repository
public class GoodsDaoImpl implements GoodsDao {

	@Autowired
	private GoodsMapper goodsMapper;

	private Jedis jedis;
	
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

	public List<Map<String, Goods>> selectById(Goods goods) {
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

	// todo setGoodsStock
	public long setGoodsStock(Order order) {
		long obj = 0;
		try {
			jedis = JedisUtil.getConn();
			obj = jedis.decr(String.valueOf(order.getGoods_id()));
			if(obj == -1) {
				jedis.incr(String.valueOf(order.getGoods_id()));
				return -1;
			}
		} finally {
			jedis.close();
		}
		return obj;
	}
	
	//todo setBack
	public long setBackGoodsStock(Order order) {
		long obj = 0;
		try {
			jedis = JedisUtil.getConn();
			obj = jedis.incr(String.valueOf(order.getGoods_id()));
		} finally {
			jedis.close();
		}
		return obj;
	}
	
	//init stock
	public long initGoodsStock() {
		long obj = 0;
		try {
			jedis = JedisUtil.getConn();
			for(int i = 1;i < 100;i++) {
				Goods goods = new Goods();
				goods.setGoods_id(i);
				goods = getGoodsById(goods);
				if(goods != null) {
					jedis.setnx(i + "", goods.getGoods_stock() + "");
				}
			}
		} finally {
			jedis.close();
		}
		return obj;
	}
}
