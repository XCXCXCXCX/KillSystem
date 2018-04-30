package com.KillSystem.DAO.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.KillSystem.DAO.GoodsDao;
import com.KillSystem.DAO.mapper.GoodsMapper;
import com.KillSystem.domain.Goods;
import com.KillSystem.domain.Order;
import com.KillSystem.util.JedisPoolManager;
import com.KillSystem.util.JedisUtil;

import redis.clients.jedis.Jedis;

/**
 * @author xcxcxcxcx
 * 
 * @Comments
 * 商品Dao实现类
 * Dao接口只是实现基本功能
 * 需要添加功能时，直接在这里添加实现方法。
 * 1.与数据库之间的交互使用mybatis的mapper接口，包括增删改查，主要是为管理员提供服务
 * 2.与redis之间的交互使用单例的jedisPool，包括商品库存的初始化、减一、撤回，主要为用户提供服务
 * 
 * 2018年4月5日
 *
 */
@Repository
public class GoodsDaoImpl implements GoodsDao {

	private static  final Logger log = LoggerFactory.getLogger(GoodsDaoImpl.class);
	
	@Autowired
	private GoodsMapper goodsMapper;

	@Transactional
	@Override
	public int insert(Goods goods) {
		// TODO Auto-generated method stub
		return goodsMapper.insert(goods);
	}

	@Transactional
	@Override
	public int delete(Goods goods) {
		// TODO Auto-generated method stub
		return goodsMapper.delete(goods);
	}

	@Transactional
	@Override
	public int update(Goods goods) {
		// TODO Auto-generated method stub
		return goodsMapper.update(goods);
	}

	@Transactional
	@Override
	public List<Map<String, Goods>> select(Goods goods) {
		// TODO Auto-generated method stub
		return goodsMapper.select(goods);
	}

	@Transactional
	@Override
	public List<Map<String, Goods>> selectById(Goods goods) {
		return goodsMapper.selectById(goods);
	}

	@Transactional
	@Override
	public Goods getGoodsById(Goods goods) {
		return goodsMapper.getGoodsById(goods);
	}

	@Transactional
	@Override
	public int updateGoodsStock(Goods goods){
		
		try {
			return goodsMapper.updateGoodsStock(goods);
		}catch (RuntimeException e) {
			log.error("mysql更新库存失败"+goods.getGoods_stock());
			throw e;
		}
		
	}

	@Transactional
	@Override
	public int updateGoodsStockBack(Goods goods){
		// TODO Auto-generated method stub
		try {
			return goodsMapper.updateGoodsStockback(goods);
		}catch (RuntimeException e) {
			log.error("mysql撤回库存失败"+goods.getGoods_stock());
			throw e;
		}
	}
	
	
	//redis操作:库存减一
	@Override
	public long setGoodsStock(Order order) {
		Jedis jedis = null;
		try {
			jedis = JedisUtil.getConn();
			long decr = jedis.decr(String.valueOf(order.getGoods_id()));
			if(decr < 0) {
				return jedis.incr(String.valueOf(order.getGoods_id()));
			}
			return decr;
		}catch(Exception e){
			log.error("redis操作:库存减一失败!",e);
			JedisUtil.returnBrokenConn(jedis);
		}finally {
//			System.out.println("getNumActive:"+JedisPoolManager.INSTANCE.getJedisPool().getNumActive());
//			System.out.println("getNumIdle:"+JedisPoolManager.INSTANCE.getJedisPool().getNumIdle());
//			System.out.println("getNumWaiters:"+JedisPoolManager.INSTANCE.getJedisPool().getNumWaiters());
			if(jedis == null) {
				jedis.close();				
			}else {
				JedisUtil.returnConn(jedis);
			}
			
		}
		//redis操作失败返回-1
		return -1;
	}
	
	//redis操作:撤回库存减一
	@Override
	public long setBackGoodsStock(Order order) {
		Jedis jedis = null;
		try {
			jedis = JedisUtil.getConn();
			return jedis.incr(String.valueOf(order.getGoods_id()));
		}catch(Exception e){
			JedisUtil.returnBrokenConn(jedis);
		}finally {
			if(jedis == null) {
				jedis.close();
			}else {
				JedisUtil.returnConn(jedis);
			}
			
		}
		//redis操作失败返回-1
		return -1;
	}
	
	//初始化redis中的默认库存数为mysql中的库存数
	@Override
	public void initGoodsStock() {
		Jedis jedis = null;
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
		}catch(Exception e){
			JedisUtil.returnBrokenConn(jedis);
		}finally {
			if(jedis == null) {
				jedis.close();
			}else {
				JedisUtil.returnConn(jedis);
			}
			
		}
	}

	//redis操作:检查商品库存是否小于等于0
	@Override
	public boolean checkGoodsStockInRedis(Order order) {
		Jedis jedis = null;
		try {
			jedis = JedisUtil.getConn();
			return "0".equals(jedis.get(order.getGoods_id()+"")) ? false : true;
		}catch(Exception e){
			JedisUtil.returnBrokenConn(jedis);
		}finally {
			if(jedis == null) {
				jedis.close();
			}else {
				try {
					JedisUtil.returnConn(jedis);
				}catch(IllegalStateException e) {
					log.error("jedis资源池返回连接失败,连接无效或已返回",e);
				}
			}
			
		}
		return false;
	}

	@Override
	public int countGoods() {
		// TODO Auto-generated method stub
		return goodsMapper.countGoods();
	}

	
}
