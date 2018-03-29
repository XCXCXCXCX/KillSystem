package com.KillSystem.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public enum JedisPoolManager {
	
	INSTANCE;
	
	private JedisPool jedisPool = null;
	
	private JedisPoolManager() {
		//System.out.println("1");
		ApplicationContext context = new ClassPathXmlApplicationContext
	            ("config/jedis.xml");
		//System.out.println("2");
		jedisPool = (JedisPool) context.getBean("jedisPool");
		//System.out.println("3");
	}
	public static JedisPoolManager getInstance() {
		return INSTANCE;
	}
	
	public JedisPool getJedisPool() {
		return jedisPool;
	}
}
