package com.KillSystem.util;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.KillSystem.DAO.GoodsDao;
import com.KillSystem.DAO.OrderDao;
import com.KillSystem.DAO.impl.GoodsDaoImpl;
import com.KillSystem.DAO.impl.OrderDaoImpl;
import com.KillSystem.domain.Goods;
import com.KillSystem.domain.Order;


/**
 * @author xcxcxcxcx
 * 
 * @Comments
 * 先进先出队列
 * 启动先进先出队列，并反复在队列中poll出order对象，
 * 如果order不为空，向mysql创建该order并令库存减一
 * 
 * 使用事务管理实现创建订单、库存减一的事务
 * 
 * 2018年4月5日
 *
 */
@Repository
public class InitFIFOListener{
	
	private static  final Logger log = LoggerFactory.getLogger(InitFIFOListener.class);
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private GoodsDao goodsDao;
	
	public static ConcurrentLinkedQueue<Order> queue = new ConcurrentLinkedQueue<Order>();
	
	
	void initMethod() {
		goodsDao.initGoodsStock();
		new Thread(new Runnable() {

			@Override
			public void run() {
				//消费者线程
				// TODO Auto-generated method stub
				while(true) {
					Order order = InitFIFOListener.queue.poll();
					boolean toContinue = true;
					if(order == null) {
//						try {
//							this.wait(6000);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
						continue;
					}
					log.info("FIFO队列开始处理一个请求了！");
					log.info("address:"+order.getAddress()+",createTime:"+order.getCreate_time()+",goods_id:"+order.getGoods_id()+",order_id:"+order.getOrder_id()+",tel_num:"+order.getTel_num());
					
					Goods goods = new Goods();
					goods.setGoods_id(order.getGoods_id());
					
					//开启事务回滚
					orderDao.createOrderAndupdateGoodsStock(order,goods);
					
				}
			}
			
		}).start();
	}
}
