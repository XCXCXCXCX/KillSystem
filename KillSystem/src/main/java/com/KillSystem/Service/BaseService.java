package com.KillSystem.Service;

import java.util.List;
import java.util.Map;


public interface BaseService<T> {
	int insert(T t);
	int delete(T t);
	int update(T t);
	List<Map<String, T>> select(T t);
}
