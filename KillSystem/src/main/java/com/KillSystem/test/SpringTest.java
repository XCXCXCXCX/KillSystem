package com.KillSystem.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.JedisPool;

public class SpringTest {
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext
	            ("config/jedis.xml");
		JedisPool jedisPool = (JedisPool) context.getBean("jedisPool");
		System.out.println(jedisPool.toString());
	}
	
}
