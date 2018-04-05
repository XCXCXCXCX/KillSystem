package com.KillSystem.util;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.KillSystem.DAO.impl.GoodsDaoImpl;
import com.KillSystem.DAO.impl.OrderDaoImpl;
import com.KillSystem.domain.Goods;
import com.KillSystem.domain.Order;


/**
 * @author xcxcxcxcx
 * 
 * 先进先出队列
 * 启动先进先出队列，并反复在队列中poll出order对象，
 * 如果order不为空，向mysql创建该order并令库存减一
 * 
 * todo 接下来使用事务管理来实现创建订单、库存减一的事务
 * 
 * 2018年4月5日
 *
 */
@Repository
public class InitFIFOListener implements Runnable{
	
	private static  final Logger logger = LoggerFactory.getLogger(InitFIFOListener.class);
	
	@Autowired
	private OrderDaoImpl orderDaoImpl;
	
	@Autowired
	private GoodsDaoImpl goodsDaoImpl;
	
	public static ConcurrentLinkedQueue<Order> queue = new ConcurrentLinkedQueue<Order>();
	
	public InitFIFOListener(GoodsDaoImpl goodsDaoImpl,OrderDaoImpl orderDaoImpl) {
		if(goodsDaoImpl == null) {
			System.out.println("goodsDaoImpl注入失败");
			return;
		}
		this.goodsDaoImpl = goodsDaoImpl;
		if(orderDaoImpl == null) {
			System.out.println("orderDaoImpl注入失败");
			return;
		}
		this.orderDaoImpl = orderDaoImpl;
	}
	
	void initMethod() {
		goodsDaoImpl.initGoodsStock();
		new Thread(new InitFIFOListener(goodsDaoImpl,orderDaoImpl)).start();
	}

	//消费者线程
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			System.out.println("FIFO，我在正常运行");
			Order order = queue.poll();
			boolean toContinue = true;
			if(order==null) {
				toContinue = false;
			}
			if(!toContinue) {
				continue;
			}
			System.out.println("FIFO队列开始处理一个请求了！");
			Goods goods = new Goods();
			goods.setGoods_id(order.getGoods_id());
			//令mysql中的库存减一
			RedisLock lock = new RedisLock(String.valueOf(order.getGoods_id()), 10000, 20000);
			try {
			    if(lock.lock()) {
			    	//需要加锁的代码
			    	if(goodsDaoImpl.updateGoodsStock(goods)==0) {
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
				try {
				    if(lock.lock()) {
				    	//需要加锁的代码
						if(goodsDaoImpl.updateGoodsStockBack(goods)==0) {
							logger.info("mysql库存"+order.getGoods_id()+"撤回失败(有效库存-1)");
						}
				    }
				}catch (InterruptedException e) {
				    e.printStackTrace();
				}finally {
				    //为了让分布式锁的算法更稳键些，持有锁的客户端在解锁之前应该再检查一次自己的锁是否已经超时，再去做DEL操作，因为可能客户端因为某个耗时的操作而挂起，
				    //操作完的时候锁因为超时已经被别人获得，这时就不必解锁了。 ————这里没有做
				    lock.unlock();
				}
			}
		}
	}
}
