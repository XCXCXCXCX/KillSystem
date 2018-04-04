package com.KillSystem.DAO.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.KillSystem.DAO.GoodsDao;
import com.KillSystem.DAO.mapper.GoodsMapper;
import com.KillSystem.domain.Goods;
import com.KillSystem.domain.Order;
import com.KillSystem.util.JedisPoolManager;
import com.KillSystem.util.JedisUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

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

	public int updateGoodsStock(Goods goods) {
		return goodsMapper.updateGoodsStock(goods);
	}

	public int updateGoodsStockBack(Goods goods) {
		// TODO Auto-generated method stub
		return goodsMapper.updateGoodsStockback(goods);
	}

	public long createPayInRedis(Order order) {
		try {
			jedis = JedisUtil.getConn();
			return jedis.setnx(order.getOrder_id()+"_pay", "0");
		} finally {
			
			if(jedis == null) {
				jedis.close();
			}else {
				JedisUtil.returnConn(jedis);
			}
			
		}
		
	}
	
	// todo setGoodsStock
	public long setGoodsStock(Order order) {
		try {
			jedis = JedisUtil.getConn();
			return jedis.decr(String.valueOf(order.getGoods_id()));
		} finally {
			System.out.println("getNumActive:"+JedisPoolManager.getInstance().getJedisPool().getNumActive());
			System.out.println("getNumIdle:"+JedisPoolManager.getInstance().getJedisPool().getNumIdle());
			System.out.println("getNumWaiters:"+JedisPoolManager.getInstance().getJedisPool().getNumWaiters());
			if(jedis == null) {
				jedis.close();				
			}else {
				JedisUtil.returnConn(jedis);
				System.out.println("库存减少的时候归还了连接！");
			}
			
		}
	}
	
	//todo setBack
	public long setBackGoodsStock(Order order) {
		try {
			jedis = JedisUtil.getConn();
			return jedis.incr(String.valueOf(order.getGoods_id()));
		} finally {
			if(jedis == null) {
				jedis.close();
			}else {
				JedisUtil.returnConn(jedis);
				System.out.println("库存撤回的时候归还了连接！");
			}
			
		}
	}
	
	//init stock
	public void initGoodsStock() {
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
			if(jedis == null) {
				jedis.close();
			}else {
				JedisUtil.returnConn(jedis);
			}
			
		}
	}

	
}
