package com.KillSystem.Service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.KillSystem.DAO.impl.ShippingAddressDaoImpl;
import com.KillSystem.Service.ShippingAddressService;
import com.KillSystem.domain.Goods;
import com.KillSystem.domain.ShippingAddress;
import com.KillSystem.domain.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("ShippingAddressServiceImpl")
public class ShippingAddressServiceImpl implements ShippingAddressService{

	@Autowired
	private ShippingAddressDaoImpl shippingAddressDaoImpl;
	
	@Override
	public int insert(ShippingAddress shippingAddress) {
		// TODO Auto-generated method stub
		return shippingAddressDaoImpl.insert(shippingAddress);
	}

	@Override
	public int delete(ShippingAddress shippingAddress) {
		// TODO Auto-generated method stub
		return shippingAddressDaoImpl.delete(shippingAddress);
	}

	@Override
	public int update(ShippingAddress shippingAddress) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Map<String, ShippingAddress>> select(ShippingAddress shippingAddress) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Map<String,ShippingAddress>> selectByuserid(User user){
		return shippingAddressDaoImpl.selectByuserid(user);
	}
	
	public PageInfo<Map<String, ShippingAddress>> selectByuserid(User user,int pageNum,int pageSize){
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, ShippingAddress>> userMap = selectByuserid(user);
	    PageInfo<Map<String, ShippingAddress>> p=new PageInfo<Map<String, ShippingAddress>>(userMap);
	    return p;
	}

	public ShippingAddress selectByaddressid(ShippingAddress shippingAddress) {
		return shippingAddressDaoImpl.selectByaddressid(shippingAddress);
	}
}
