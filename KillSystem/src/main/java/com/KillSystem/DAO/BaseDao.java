package com.KillSystem.DAO;

import java.util.List;
import java.util.Map;

import com.KillSystem.domain.Goods;
import com.KillSystem.domain.Order;


/**
 * @author xcxcxcxcx
 * 
 * @param <T>
 * 
 * @Comments
 * 基础Dao接口提供基本增删改查
 * 
 * 2018年4月5日
 *
 */
public interface BaseDao<T> {
	int insert(T t);
	int delete(T t);
	int update(T t);
	List<Map<String, T>> select(T t);
}
