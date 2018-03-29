package com.KillSystem.Service;

import java.util.List;
import java.util.Map;

import com.KillSystem.domain.Goods;
import com.github.pagehelper.PageInfo;

public interface GoodsService extends BaseService<Goods>{
	PageInfo<Map<String, Goods>> select(Goods goods,int pageNum,int pageSize);
	PageInfo<Map<String, Goods>> selectById(Goods goods,int pageNum,int pageSize);
	Goods getGoodsById(Goods goods);
}
