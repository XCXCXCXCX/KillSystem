package com.KillSystem.DAO;

import java.util.List;
import java.util.Map;

import com.KillSystem.domain.Goods;
import com.KillSystem.domain.Order;

public interface BaseDao<T> {
	int insert(T t);
	int delete(T t);
	int update(T t);
	List<Map<String, T>> select(T t);
}
