package com.KillSystem.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author xcxcxcxcx
 * 
 * @Comments
 * JedisUtil提供获取连接、返回连接、关闭连接池方法
 * 
 * 2018年4月5日
 *
 */
public class JedisUtil {
	
	private static JedisPool jedisPool = JedisPoolManager.INSTANCE.getJedisPool();
	
	public static Jedis getConn() {
		return jedisPool.getResource();
	}
	
	public static void returnConn(Jedis jedis) {
		jedisPool.returnResource(jedis);
	}
	
	public static void returnBrokenConn(Jedis jedis) {
		jedisPool.returnBrokenResource(jedis);
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
