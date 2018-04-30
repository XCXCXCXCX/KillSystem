package com.KillSystem.Service;

import java.util.Map;

import com.KillSystem.DAO.BaseDao;
import com.KillSystem.domain.ShippingAddress;
import com.KillSystem.domain.User;
import com.github.pagehelper.PageInfo;

/**
 * @author xcxcxcxcx
 * 
 * @Comments
 * 收货地址服务
 * 提供了 根据收货地址id查询收货地址 和 根据用户id查询收货地址的分页查询 接口
 * 
 * 2018年4月5日
 *
 */
public interface ShippingAddressService extends BaseDao<ShippingAddress>{
	ShippingAddress selectByaddressid(ShippingAddress shippingAddress);
	PageInfo<Map<String,ShippingAddress>> selectByuserid(User user,int pageNo,int pageSize);
}
