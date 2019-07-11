package com.temporary.center.ls_common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

import java.util.HashSet;
import java.util.Set;

@Configuration
//@PropertySource("classpath:/application-dev.properties")
public class RedisConfiguration {  
	
      
    @Bean(name= "jedis.pool")  
    @Autowired  
    public Pool<Jedis> jedisPool(@Qualifier("jedis.pool.config") JedisPoolConfig config,   
    			@Value("${jedis.pool.cluster}")int cluster,
                @Value("${jedis.pool.host}")String host,   
                @Value("${jedis.pool.port}")int port,
                @Value("${jedis.pool.timeout}")int timeout,
                @Value("${jedis.pool.name}")String name,
                @Value("${jedis.pool.password}")String password) {  
    	if (cluster==1) {
    		Set<String> sentinels = getSentinels(host);
			return new JedisSentinelPool(name, sentinels, config, timeout, password);
		}else{
			   return new JedisPool(config, host, port, timeout, password);
		}
    	
    }  
      
    @Bean(name= "jedis.pool.config")  
    public JedisPoolConfig jedisPoolConfig (@Value("${jedis.pool.config.maxTotal}")int maxTotal,  
                                @Value("${jedis.pool.config.maxIdle}")int maxIdle,  
                                @Value("${jedis.pool.config.maxWaitMillis}")int maxWaitMillis) {  
        JedisPoolConfig config = new JedisPoolConfig();  
        config.setMaxTotal(maxTotal);  
        config.setMaxIdle(maxIdle);  
        config.setMaxWaitMillis(maxWaitMillis);  
        return config;  
    }  
	public Set<String> getSentinels(String host) {
		if (host == null)
			return null;

		String[] strs = host.split(",");
		if (strs.length < 1)
			return null;

		Set<String> set = new HashSet<String>();
		for (String s : strs)
			if (s != null && s.trim().length() > 0)
				set.add(s.trim());

		return set;
	}

}  