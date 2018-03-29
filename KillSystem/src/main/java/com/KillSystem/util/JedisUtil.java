package com.KillSystem.util;

import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisUtil {
	
	private static JedisPool jedisPool = JedisPoolManager.INSTANCE.getInstance().getJedisPool();
	
	public static Jedis getConn() {
		return jedisPool.getResource();
	}
	
}
