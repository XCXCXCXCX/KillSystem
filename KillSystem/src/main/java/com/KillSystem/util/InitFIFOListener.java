package com.KillSystem.util;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.KillSystem.DAO.impl.GoodsDaoImpl;
import com.KillSystem.DAO.impl.OrderDaoImpl;
import com.KillSystem.domain.Order;
import com.alibaba.druid.support.logging.Log;

public class InitFIFOListener implements Runnable{
	
	private static  final Logger logger = LoggerFactory.getLogger(InitFIFOListener.class);
	
	private RedisTemplate redisTemplate = new RedisTemplate();
	
	@Autowired
	private OrderDaoImpl orderDaoImpl;
	
	@Autowired
	private GoodsDaoImpl goodsDaoImpl;
	
	private static ConcurrentLinkedQueue<Order> queue = new ConcurrentLinkedQueue<Order>();
	
	void initMethod() {
		new Thread(new InitFIFOListener()).start();
	}

	//消费者线程
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Order order = queue.poll();
		boolean toContinue = true;
		
		//令mysql中的库存减一
		RedisLock lock = new RedisLock(redisTemplate, String.valueOf(order.getGoods_id()), 10000, 20000);
		try {
		    if(lock.lock()) {
		    	//需要加锁的代码
		    	if(goodsDaoImpl.updateGoodsStock(order.getGoods_id())==0) {
		    		logger.info("mysql库存"+order.getGoods_id()+"更新失败");
		    		toContinue = false;
		    	}
		    }
		}catch (InterruptedException e) {
		    e.printStackTrace();
		}finally {
		    //为了让分布式锁的算法更稳键些，持有锁的客户端在解锁之前应该再检查一次自己的锁是否已经超时，再去做DEL操作，因为可能客户端因为某个耗时的操作而挂起，
		    //操作完的时候锁因为超时已经被别人获得，这时就不必解锁了。 ————这里没有做
		    lock.unlock();
		}
		
		//在mysql中写入order信息
		//todo
		if(toContinue&&orderDaoImpl.createOrder(order)==0) {
			logger.info(order.getOrder_id()+"mysql订单插入失败");
			toContinue = false;
			while(goodsDaoImpl.updateGoodsStockBack(order.getGoods_id())==0) {
				logger.info("mysql库存"+order.getGoods_id()+"撤回失败");
			}
		}
	}
}
