package com.KillSystem.Service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.KillSystem.DAO.GoodsDao;
import com.KillSystem.Service.GoodsService;
import com.KillSystem.domain.Goods;
import com.KillSystem.domain.Order;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


/**
 * @author xcxcxcxcx
 * 
 * @Comments
 * 商品服务实现类
 * 
 * 提供管理员对商品的增删改查接口
 * 提供用户对商品的分页查询和对商品库存的操作接口
 * 
 * 2018年4月5日
 *
 */
@Service("GoodsService")
public class GoodsServiceImpl implements GoodsService{

	@Autowired
	private GoodsDao goodsDao;
	
	
	@Override
	public int insert(Goods goods) {
		// TODO Auto-generated method stub
		return goodsDao.insert(goods);
	}

	@Override
	public int delete(Goods goods) {
		// TODO Auto-generated method stub
		return goodsDao.delete(goods);
	}

	@Override
	public int update(Goods goods) {
		// TODO Auto-generated method stub
		return goodsDao.update(goods);
	}

	@Override
	public List<Map<String, Goods>> select(Goods goods) {
		// TODO Auto-generated method stub
		return goodsDao.select(goods);
	}
	
	public PageInfo<Map<String, Goods>> select(Goods goods,int pageNum,int pageSize){
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Goods>> goodsMap = select(goods);
	    PageInfo<Map<String, Goods>> p=new PageInfo<Map<String, Goods>>(goodsMap);
	    return p;
	}

	public List<Map<String, Goods>> selectById(Goods goods) {
		// TODO Auto-generated method stub
		return goodsDao.selectById(goods);
	}
	
	@Override
	public PageInfo<Map<String, Goods>> selectById(Goods goods, int pageNum, int pageSize) {
		// TODO Auto-generated method stub
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Goods>> goodsMap = selectById(goods);
	    PageInfo<Map<String, Goods>> p=new PageInfo<Map<String, Goods>>(goodsMap);
	    return p;
	}

	public Goods getGoodsById(Goods goods) {
		// TODO Auto-generated method stub
		return goodsDao.getGoodsById(goods);
	}
	
	@Override
	public long decrGoodsStock(Order order) {
		long flag = goodsDao.setGoodsStock(order);
		if(flag < 0) {
			goodsDao.setBackGoodsStock(order);
			return 0;
		}else {
			return flag;
		}
		
	}

	@Override
	public long incrGoodsStock(Order order) {
		// TODO Auto-generated method stub
		return goodsDao.setBackGoodsStock(order);
	}

	@Override
	public boolean checkGoodsStockInRedis(Order order) {
		// TODO Auto-generated method stub
		return goodsDao.checkGoodsStockInRedis(order);
	}

	@Override
	public int countGoods() {
		// TODO Auto-generated method stub
		return goodsDao.countGoods();
	}

	

	
	
}
