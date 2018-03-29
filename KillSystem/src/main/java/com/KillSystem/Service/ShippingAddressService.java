package com.KillSystem.Service;

import java.util.Map;

import com.KillSystem.DAO.BaseDao;
import com.KillSystem.domain.ShippingAddress;
import com.KillSystem.domain.User;
import com.github.pagehelper.PageInfo;

public interface ShippingAddressService extends BaseDao<ShippingAddress>{
	ShippingAddress selectByaddressid(ShippingAddress shippingAddress);
	PageInfo<Map<String,ShippingAddress>> selectByuserid(User user,int pageNo,int pageSize);
}
