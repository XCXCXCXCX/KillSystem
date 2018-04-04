package com.KillSystem.Service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.KillSystem.DAO.impl.GoodsDaoImpl;
import com.KillSystem.Service.GoodsService;
import com.KillSystem.domain.Goods;
import com.KillSystem.domain.Order;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import redis.clients.jedis.Jedis;

@Service("GoodsServiceImpl")
public class GoodsServiceImpl implements GoodsService{

	@Autowired
	private GoodsDaoImpl goodsDaoImpl;
	
	
	@Override
	public int insert(Goods goods) {
		// TODO Auto-generated method stub
		return goodsDaoImpl.insert(goods);
	}

	@Override
	public int delete(Goods goods) {
		// TODO Auto-generated method stub
		return goodsDaoImpl.delete(goods);
	}

	@Override
	public int update(Goods goods) {
		// TODO Auto-generated method stub
		return goodsDaoImpl.update(goods);
	}

	@Override
	public List<Map<String, Goods>> select(Goods goods) {
		// TODO Auto-generated method stub
		return goodsDaoImpl.select(goods);
	}
	
	public PageInfo<Map<String, Goods>> select(Goods goods,int pageNum,int pageSize){
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Goods>> goodsMap = select(goods);
	    PageInfo<Map<String, Goods>> p=new PageInfo<Map<String, Goods>>(goodsMap);
	    return p;
	}

	public List<Map<String, Goods>> selectById(Goods goods) {
		// TODO Auto-generated method stub
		return goodsDaoImpl.selectById(goods);
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
		return goodsDaoImpl.getGoodsById(goods);
	}
	
	@Override
	public long decrGoodsStock(Order order) {
		return goodsDaoImpl.setGoodsStock(order);
	}

	@Override
	public long incrGoodsStock(Order order) {
		// TODO Auto-generated method stub
		return goodsDaoImpl.setBackGoodsStock(order);
	}

	

	
	
}
