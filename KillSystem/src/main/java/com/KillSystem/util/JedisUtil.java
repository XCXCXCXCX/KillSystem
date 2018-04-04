package com.KillSystem.util;

import org.springframework.stereotype.Repository;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisUtil {
	
	private static JedisPool jedisPool = JedisPoolManager.INSTANCE.getInstance().getJedisPool();
	
	public static Jedis getConn() {
		return jedisPool.getResource();
	}
	
	public static void returnConn(Jedis jedis) {
		jedisPool.returnResource(jedis);
	}
	
	
    /**
     * 关闭连接池
     */
    public static void closePool() {
        if (jedisPool != null) {
            jedisPool.close();
        }
    }
	
}
