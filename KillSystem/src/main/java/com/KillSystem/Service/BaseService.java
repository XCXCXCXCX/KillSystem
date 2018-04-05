package com.KillSystem.Service;

import java.util.List;
import java.util.Map;


/**
 * @author xcxcxcxcx
 * 
 * @param <T>
 * 
 * 对外暴露服务接口
 * 基础服务接口提供基础的增删改查服务
 * 
 * 2018年4月5日
 *
 */
public interface BaseService<T> {
	int insert(T t);
	int delete(T t);
	int update(T t);
	List<Map<String, T>> select(T t);
}
