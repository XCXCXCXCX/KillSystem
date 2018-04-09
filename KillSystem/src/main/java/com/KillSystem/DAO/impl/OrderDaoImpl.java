package com.KillSystem.DAO.impl;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.KillSystem.DAO.GoodsDao;
import com.KillSystem.DAO.OrderDao;
import com.KillSystem.DAO.mapper.GoodsMapper;
import com.KillSystem.DAO.mapper.OrderMapper;
import com.KillSystem.domain.Goods;
import com.KillSystem.domain.Order;
import com.KillSystem.util.InitFIFOListener;
import com.KillSystem.util.JedisUtil;
import com.KillSystem.util.RedisLock;
import com.alibaba.druid.support.logging.Log;

import redis.clients.jedis.Jedis;


/**
 * @author xcxcxcxcx
 * 
 * @Comments
 * 订单Dao实现类
 * Dao接口只是实现基本功能
 * 需要添加功能时，直接在这里添加实现方法。
 * 
 * 2018年4月5日
 *
 */
@Repository
public class OrderDaoImpl implements OrderDao {

	private static final Logger log = LoggerFactory.getLogger(OrderDaoImpl.class);
	
	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private GoodsDao goodsDao;
	
	private Jedis jedis;
	

	@Override
	public int insert(Order t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Transactional
	@Override
	public int delete(Order order) {
		// TODO Auto-generated method stub
		return orderMapper.delete(order);
	}

	@Transactional
	@Override
	public int update(Order order) {
		// TODO Auto-generated method stub
		return orderMapper.update(order);
	}

	@Transactional
	@Override
	public int updateOrderState(Order order) {
		return orderMapper.updateOrderState(order);
	}

	@Transactional
	@Override
	public List<Map<String, Order>> select(Order order) {
		// TODO Auto-generated method stub
		return orderMapper.select(order);
	}

	@Transactional
	@Override
	public boolean orderIsExist(Order order) {
		return orderMapper.selectByorderid(order) == null ? false : true;
	}
	
	@Override
	public boolean orderIsExistInRedis(Order order) {
		try {
			jedis = JedisUtil.getConn();
			return jedis.get(order.getOrder_id()) == "nil" ? false : true;
		}finally {
			if(jedis == null) {
				jedis.close();
			}else {
				JedisUtil.returnConn(jedis);
			}
		}
	}

	@Transactional
	@Override
	public int createOrder(Order order)  {
		// TODO Auto-generated method stub
		try {
			return orderMapper.createOrder(order);
		}catch (RuntimeException e) {
			log.error("mysql创建订单"+order.getOrder_id()+"失败"+":goods_id="+order.getGoods_id()+",tel_num="+order.getTel_num()+",address="+order.getAddress());
			goodsDao.setBackGoodsStock(order);
			throw e;
		}
	}
	
	@Transactional
	@Override
	public boolean createOrderAndupdateGoodsStock(Order order,Goods goods) {
		// TODO Auto-generated method stub
		try {
			//在mysql中写入order信息
			createOrder(order);
			//初始化分布式锁
			RedisLock lock = new RedisLock(String.valueOf(order.getGoods_id()), 10000, 20000);
			//加锁
			try {
			    if(lock.lock()) {
			    	//令mysql中的库存减一
			    	goodsDao.updateGoodsStock(goods);
			    }
			}catch (InterruptedException e) {
			    e.printStackTrace();
			}finally {
			    //为了让分布式锁的算法更稳键些，持有锁的客户端在解锁之前应该再检查一次自己的锁是否已经超时，再去做DEL操作，因为可能客户端因为某个耗时的操作而挂起，
			    //操作完的时候锁因为超时已经被别人获得，这时就不必解锁了。 ————这里没有做
			    lock.unlock();
			}
			
		}catch(RuntimeException e) {
			log.error("mysql创建订单并库存减一事务操作失败！订单号:{},商品号:{}",order.getOrder_id(),order.getGoods_id());
		}
		return true;
	}

	@Override
	public long createOrderInRedis(Order order) {
		try {
			jedis = JedisUtil.getConn();
			return jedis.setnx(order.getOrder_id(), order.getTel_num() + "," + order.getAddress() + ","
					+ order.getGoods_id() + "," + DateTime.now().toString("YYYY-MM-dd HH-mm-ss"));
		}finally {
			if(jedis.expire(order.getOrder_id(), 300)!=1) {
				log.error("设置超时时间失败！");
			}
			if(jedis == null) {
				jedis.close();
			}else {
				JedisUtil.returnConn(jedis);
			}
			
		}
	}

	@Override
	public long createPayInRedis(Order order) {
		try {
			jedis = JedisUtil.getConn();
			return jedis.setnx(order.getOrder_id() + "_pay", "0");
		}finally {
			if(jedis.expire(order.getOrder_id() + "_pay", 600) != 1) {
				log.error("支付订单设置有效时间失败！");
			}
			if(jedis == null) {
				jedis.close();
			}else {
				JedisUtil.returnConn(jedis);
			}
			
		}
	}
	
	@Override
	public String updateOrderPayInRedis(Order order) {
		try {
			jedis = JedisUtil.getConn();
			String obj = jedis.getSet(order.getOrder_id() + "_pay", "1");
			return "支付成功"; 
		} finally { 
			if(jedis == null) {
				jedis.close();
			}else {
				JedisUtil.returnConn(jedis);
			}
		}
	}
	
	@Override
	public String getPayState(Order order) {
		try {
			jedis = JedisUtil.getConn();
			return jedis.get(order.getOrder_id() + "_pay");
		}finally { 
			if(jedis == null) {
				jedis.close();
			}else {
				JedisUtil.returnConn(jedis);
			}
		}
	}

	@Override
	public Order selectByorderIdInRedis(Order order) {
		// TODO Auto-generated method stub
		try {
			jedis = JedisUtil.getConn();
			Order obj = new Order();
			String result = jedis.get(order.getOrder_id());
			String[] arrayResult = result.split(",");
			obj.setOrder_id(order.getOrder_id());
			obj.setTel_num(arrayResult[0]);
			obj.setAddress(arrayResult[1]+","+arrayResult[2]+","+arrayResult[3]);
			obj.setGoods_id(Integer.parseInt(arrayResult[4]));
			return obj;
		}finally { 
			if(jedis == null) {
				jedis.close();
			}else {
				JedisUtil.returnConn(jedis);
			}
		}
		
	}
	
	
	
}
