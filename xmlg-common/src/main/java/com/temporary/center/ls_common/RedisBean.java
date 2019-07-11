package com.temporary.center.ls_common;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * 封装redis 缓存服务器服务接口 <功能详细描述>
 * 
 * @author 卿剑
 * @version [版本号, 2015年1月8日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Component
public class RedisBean {
	
	private static final LogUtil logger = LogUtil.getLogUtil(RedisBean.class);

//	@Autowired
//	@Qualifier("jedisPool")
//	private JedisPool jedisPool;
	@Autowired
	RedisUtil redisService;
	
	/**
	 * 锁Key成功
	 */
	private static final String LOCK_SUCCESS = "OK";
	
	/**
	 * 如果key存在，修改value值
	 * Only set the key if it already exist.
	 */
	public static final String SET_IF_EXIST_XX = "XX";
	
	/**
	 * 如果key存在，则不修改
	 * Only set the key if it does not already exist
	 */
    public static final String SET_IF_NOT_EXIST_NX = "NX";
    
    /**
     * 毫秒级别
     * expire time units: PX = milliseconds
     */
    public static final String SET_WITH_EXPIRE_PX_TIME = "PX";
    
    /**
     * 秒级别
     * expire time units: EX = seconds; 
     */
    public static final String SET_WITH_EXPIRE_EX_TIME = "EX";
    
    /**
     * 释放Key成功
     */
    public static final Long RELEASE_SUCCESS = 1L;
    

	/**
	 * 获取一个jedis 客户端
	 * 
	 * @return
	 */
	protected Jedis getJedis() throws JedisException {
		Jedis jedis = redisService.getJedisPool().getResource();
		return jedis; // 模板方法
	}

	/**
	 * Handle jedisException, write log and return whether the connection is
	 * broken.
	 */
	protected boolean handleJedisException(JedisException jedisException) {
		if (jedisException instanceof JedisConnectionException) {
			logger.error("Redis connection lost.", jedisException);
		} else if (jedisException instanceof JedisDataException) {
			if ((jedisException.getMessage() != null)
					&& (jedisException.getMessage().indexOf("READONLY") != -1)) {
				logger.error("Redis connection are read-only slave.",
						jedisException);
			} else {
				// dataException, isBroken=false
				return false;
			}
		} else {
			logger.error("Jedis exception happen.", jedisException);
		}
		return true;
	}

	/**
	 * Return jedis connection to the pool, call different return methods
	 * depends on the conectionBroken status.
	 */
	protected void closeResource(Jedis jedis, boolean conectionBroken) {
		try {
			if (conectionBroken) {
				redisService.getJedisPool().returnBrokenResource(jedis);
			} else {
				redisService.getJedisPool().returnResource(jedis);
			}
		} catch (Exception e) {
			logger.error(
					"return back jedis failed, will fore close the jedis.", e);

			if ((jedis != null) && jedis.isConnected()) {
				try {
					try {
						jedis.quit();
					} catch (Exception e2) {
					}
					jedis.disconnect();
				} catch (Exception e2) {

				}
			}
		}
	}

	/**
	 * 增加 <功能详细描述>
	 * 
	 * @param key
	 * @param expiresTime
	 * @param value
	 * @return
	 * @throws Exception
	 *             [参数说明]
	 * 
	 * @return boolean [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public boolean add(final String key, final int expiresTime,
			final String value) throws Exception {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			if (jedis.exists(key)) {
				return Boolean.FALSE;
			}
			jedis.set(key, value);
			if (expiresTime > 0) {
				jedis.expire(key, expiresTime);
			}
			return Boolean.TRUE;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}

	}

	public boolean delete(final String key) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.del(key) > 0;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}

	}

	public Long deleteAndReturnCounts(final String key) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.del(key);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}

	}

	public String get(final String key) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.get(key);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}

	
	public boolean set(byte[] key,byte[] value) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			jedis.set(key, value);
			return Boolean.TRUE;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}
	
	public boolean set(final String key, final int expiresTime,
			final String value) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			jedis.set(key, value);
			if (expiresTime > 0) {
				jedis.expire(key, expiresTime);
			}
			return Boolean.TRUE;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}

	public boolean hsetTime(final String key, final String field,
			int expiresTime, final String value) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			jedis.hset(key, field, value);
			if (expiresTime > 0) {
				jedis.expire(key, expiresTime);
			}
			return Boolean.TRUE;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}

	public long incr(final String key) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.incr(key);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}

	}

	public long incrBy(final String key, final long step) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.incrBy(key, step);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}

	public long decr(final String key) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.decr(key);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}

	public long decrBy(final String key, final long decrement) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.decrBy(key, decrement);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}

	// redis key 操作
	public boolean exists(final String key) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.exists(key);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
		
	}

	// redis Hash 操作
	public long hlen(final String key) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.hlen(key);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
		
	}

	public long hsetnx(final String key, final String field, final String value) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			long result = jedis.hsetnx(key, field, value);

			return result;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
		
	}

	public long hset(final String key, final String field, final String value) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			long result = jedis.hset(key, field, value);

			return result;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
		
	}

	public String hset(final String key, final Map<String, String> hash,int outTimeS) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			String result = jedis.hmset(key, hash);
			if(outTimeS>0){
				jedis.expire(key,outTimeS);
			}
			return result;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
		
	}

	public String hget(final String key, final String field) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			String result = jedis.hget(key, field);

			return result;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
		
	}

	public Map<String, String> hgetAll(final String key, final String field) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			Map<String, String> result = jedis.hgetAll(key);

			return result;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
		
	}

	public boolean hexists(final String key, final String field) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			boolean result = jedis.hexists(key, field);

			return result;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
		
	}

	public Map<String, String> hgetAll(final String key) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			Map<String, String> result = jedis.hgetAll(key);
			return result;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
		
	}

	public Long hdel(final String key, final String[] fields) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			Long result = jedis.hdel(key, fields);
			return result;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
		
	}

	// Redis List 操作
	public long lpush(final String key, final String... values) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			long result = jedis.lpush(key, values);

			return result;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
		
	}
	
	public String lpop(final String key) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			String result = jedis.lpop(key);
			return result;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
		
	}

	/**
	 * 移除并获取列表最后一个元素
	 */
	public String rpop(final String key) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			String result = jedis.rpop(key);
			return result;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		

	}
	
	public String lIndex(final String key, Long index) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			String result = jedis.lindex(key, index);
			return result;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		

	}
	
	public List<String> lRange(final String key, Long start, Long end) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			List<String> result = jedis.lrange(key, start, end);
			return result;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		

	}

	// Redis Set操作
	public long sadd(final String key, final String... members) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.sadd(key, members);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
		
	}

	public long sremove(final String key, final String... members) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.srem(key, members);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
		
	}

	public long scard(final String key) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.scard(key);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
		
	}

	public boolean sismember(final String key, final String member) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.sismember(key, member);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
		
	}

	public Set<String> smembers(final String key) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.smembers(key);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
		
	}

	public String spop(final String key) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.spop(key);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
		
	}

	public String srandmember(final String key) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.srandmember(key);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
		
	}
	
	// Reids sorted set 操作
	/**
	 * 新增成员member并设置score为1
	 * @param key
	 * @param member
	 * @return
	 */
	public Long zAdd(final String key, String member) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.zadd(key, 1, member);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	} 
	
	/**
	 * 新增成员member并设置score为指定的值score
	 * @param key
	 * @param member
	 * @return
	 */
	public Long zAdd(final String key, String member, Double score) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.zadd(key, score, member);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	} 
	/**
	 * 删除成员member
	 * @param key
	 * @param members
	 * @return 成功删除的元素数量
	 */
	public Long zRem(final String key, String[] members) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.zrem(key, members);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	} 
	/**
	 * 将成员member的score加1
	 * @param key
	 * @param member
	 * @return
	 */
	public Double zIncr(final String key, String member) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.zincrby(key, 1, member);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}
	/**
	 * 将成员member的score增加指定的值score，如果不存在member，创建并设置score
	 * @param key
	 * @param member
	 * @param score
	 * @return
	 */
	public Double zIncr(final String key, String member, Double score) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.zincrby(key, score, member);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}
	
	/**
	 * 按score从大到小，获取制定数量count的成员
	 * @param key
	 * @param count
	 * @return
	 */
	public Set<String> zMax(final String key, final Integer count) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.zrevrange(key, 0, count);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}
	
	/**
	 * 按score从小到大，获取制定数量count的成员
	 * @param key
	 * @param count
	 * @return
	 */
	public Set<String> zMin(final String key, final Integer count) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.zrange(key, 0, count);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}
	
	/**
	 * 获取制定索引 (start - end)内的成员
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<String> zRange(final String key, final Integer start, final Integer end) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.zrevrange(key, start, end);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}
	
	/**
	 * 获取 指定member的索引 0开头
	 * @param key
	 * @param member
	 * @return
	 */
	public Long zRange(final String key, final String member) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.zrank(key, member);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}
	
	public Double zScore(final String key, final String member) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.zscore(key, member);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}
	
	public Set<String> keys(final String pattern) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.keys(pattern);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}

	// Redis pub/sub
	public void publish(final String channel, final String message) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			jedis.publish(channel, message);// 发送消息
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
		
	}

	public boolean setnx(final String key, final String value,
			final int expiresTime) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			long result = jedis.setnx(key, value);
			if (result == 1 && expiresTime > 0) {
				jedis.expire(key, expiresTime);
			}
			return result == 1;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
		
	}
	public String getSet(final String key, final String value) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();					
			return jedis.getSet(key, value);	
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}
	
	public String setex(final String key, final String value,
			final int expiresTime) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			String result = jedis.setex(key, expiresTime, value);
			return result;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
		

	}

	public Map<String, String> hgetAll(final String key,
			final boolean afterDelete) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();

			Map<String, String> map = jedis.hgetAll(key);

			if (afterDelete) {
				jedis.del(key);
			}

			return map;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
		
	}
	
	public void subscribe(final JedisPubSub listener, final String... channels) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			// 使用模式匹配的方式设置频道
			jedis.subscribe(listener, channels);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
		
	}

	public void psubscribe(final JedisPubSub listener, final String[] patterns) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			// 使用模式匹配的方式设置频道
			jedis.psubscribe(listener, patterns);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
		
	}

	/**
	 * 清空redis 所有数据
	 * 
	 * @return
	 */
	public String flushDB() {
		return getJedis().flushDB();
	}

	/**
	 * 查看redis里有多少数据
	 */
	public long dbSize() {
		return getJedis().dbSize();
	}

	/**
	 * 检查是否连接成功
	 * 
	 * @return
	 */
	public String ping() {
		return getJedis().ping();
	}

	public Long hdel(final String key, String field) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			Long result = jedis.hdel(key, field);
			return result;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
		

		
	}

	/**
	 * 检查是有key值的缓存数据
	 * 
	 * @param key
	 * @return
	 */
	public boolean isExist(String key) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			boolean result = jedis.exists(key);
			return result;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}

	/**
	 * 返回key对应hash 的所有value。
	 * 
	 * @param key
	 * @return
	 */
	public List<String> hvals(String key) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			List<String> result = jedis.hvals(key);
			return result;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}
	
	public String hsetExpiresTime(final String key, final Map<String, String> hash,final int expiresTime) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			String result = jedis.hmset(key, hash);
			if (expiresTime > 0) {
                jedis.expire(key, expiresTime);
            }
			return result;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
		
	}

	/**
	 * 关闭redis连接
	 */
	public void closeRedis(Jedis jedis) {

		redisService.getJedisPool().returnResource(jedis);
	}


	/**
	 * 获取一个锁
	 *
	 * @param lock   锁名称
	 * @param millis 毫秒
	 * @return
	 */
	public boolean lock(String lock, long millis) {
		boolean success = false;
		Jedis jedis = getJedis();
		long value = System.currentTimeMillis() + millis;

		// 通过SETNX试图获取一个lock
		long acquired = jedis.setnx(lock, String.valueOf(value));

		if (acquired == 1) { //SETNX成功，则成功获取一个锁
			success = true;
		} else { //SETNX失败，说明锁仍然被其他对象保持，检查其是否已经超时
			//这里get时,可能已经失效了,拿到null
			String timeoutStr = jedis.get(lock);
			long oldValue = timeoutStr == null ? 0 : Long.valueOf(timeoutStr);

			if (oldValue < System.currentTimeMillis()) { //超时
				String getValue = jedis.getSet(lock, String.valueOf(value));
				if (Long.valueOf(getValue) == oldValue) { // 获取锁成功
					success = true;
				} else { // 已被其他进程捷足先登了
					success = false;
				}
			} else { //未超时，则直接返回失败
				success = false;
			}
		}
		closeRedis(jedis);
		return success;
	}
	
	/**
	 * 用于返回列表的长度
	 * @param key
	 * @return
	 * @author shiqing
	 * @time 2017年9月22日 上午10:42:18
	 */
	public Long llen(String key){
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			Long result = jedis.llen(key);
			return result;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}
	
	/**
	 * Redis Lrem 根据参数 COUNT 的值，移除列表中与参数 VALUE 相等的元素。
	 * count > 0 : 从表头开始向表尾搜索，移除与 VALUE 相等的元素，数量为 COUNT 。
		count < 0 : 从表尾开始向表头搜索，移除与 VALUE 相等的元素，数量为 COUNT 的绝对值。
		count = 0 : 移除表中所有与 VALUE 相等的值。
	 * @param key
	 * @return
	 * @author shiqing
	 * @time 2017年9月22日 上午10:42:18
	 */
	public Long lrem(String key, long count, String value){
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			Long result = jedis.lrem(key, count, value);
			return result;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}
	
	/**
	 * ltrim  remain  [start - end]
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public String ltrim(String key,long start,long end){
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			String ok = jedis.ltrim(key, start, end);
			return ok;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}

	/**
	 * 释放锁定
	 * @param lock
     */
	public void unlock(String lock) {
		Jedis jedis = getJedis();
		long current = System.currentTimeMillis();
		// 避免删除非自己获取得到的锁
		if (current <= Long.valueOf(jedis.get(lock)))
			jedis.del(lock);
		closeRedis(jedis);
	}
	
	
	
	/**
	 * 获取key的失效剩余时间
	 * @param key
	 * @return
	 */
	public Long getTtl(final String key) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			Long result = jedis.ttl(key);
			return result;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		
	}
	
	/**
	 * 将值插入到列表的尾部
	 */
	public void rpush(String key,String value){
        boolean broken = false;
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            jedis.rpush(key, value);
        }
        catch (JedisException e) {
            broken = handleJedisException(e);
            throw e;
        } finally {
            closeResource(jedis, broken);
        }
    }
    
    /**
     * 移除列表的最后一个元素，并将该元素添加到另一个列表并返回
     */
    public String rpoplpush(String name,String tmpName) {
        boolean broken = false;
        Jedis jedis = null;
        
        String task = null;
        try {
            jedis = this.getJedis();
            task = jedis.rpoplpush(name, tmpName);
        }
        catch (JedisException e) {
            broken = handleJedisException(e);
            throw e;
        } finally {
            closeResource(jedis, broken);
        }
        return task;
    }
    /**
     * 
    * @Title: sort  
    * @Description:获取数据(排序后的)
    * @author: 冯寒
    * @param key
    * @param sortingParameters
    * @return    
    * List<String>    
    * @throws
     */
    public List<String> sort(final String key, final SortingParams sortingParameters) {
        boolean broken = false;
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            if(sortingParameters==null){                
                return jedis.sort(key);
            }else{
                return jedis.sort(key,sortingParameters);
            }            
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw e;
        } finally {
            closeResource(jedis, broken);
        }
    }
    
    /**
     * jimingmin ,类似keys命令,查大数据量时,用scan可以避免keys命令的性能问题
     * @param cursor
     * @param params
     * @return
     */
    public ScanResult<String> scan(String cursor,ScanParams params) {
        boolean broken = false;
        Jedis jedis = null;
        
        ScanResult<String> ret = null;
        try {
            jedis = this.getJedis();
            ret = jedis.scan(cursor, params);
        }
        catch (JedisException e) {
            broken = handleJedisException(e);
            throw e;
        } finally {
            closeResource(jedis, broken);
        }
        return ret;
    }

    
    /**
     * cc 设置key在指定时间失效
     * @param key
     * @param time
     * @return
     */
	public Long expireAt(String key, long time) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			Long result = jedis.expireAt(key, time);
			return result;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}
    
	/**
	 * Redis Expire 命令用于设置 key 的过期时间。key 过期后将不再可用。
	 * 设置成功返回 1 。 当 key 不存在或者不能为 key 设置过期时间时(比如在低于 2.1.3 版本的 Redis 中你尝试更新 key 的过期时间)返回 0 
	 * @param key
	 * @param expiresTime
	 * @return
	 * @author shiqing
	 * @time 2018年5月21日 下午4:20:43
	 */
	public boolean expire(final String key, final int expiresTime) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.expire(key, expiresTime) > 0;
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}

	}
	
	public long srem(String key, String... members) {
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			return jedis.srem(key, members);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
	}
	
	
	/**
	 * key锁
	 * @param key
	 * @param uniqueId 唯一编号
	 * @param expireTime key失效时间
	 * @param setIf 参考常量说明
	 * @param setWith 参考常量说明
	 * @return [参数说明]
	 * 
	 * @return boolean [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
    public boolean tryLock(final String key, final String uniqueId, 
            final int expireTime, final String setIf, final String setWith) {
        
        boolean broken = false;
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            
            String result = jedis.set(key, uniqueId, setIf, 
                    setWith, expireTime);

            if (LOCK_SUCCESS.equals(result)) {
                return true;
            }
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw e;
        } finally {
            closeResource(jedis, broken);
        }
        return false;
    }
	
    /**
     * 释放key锁
     * @param key
     * @param uniqueId 唯一编号
     * @return [参数说明]
     * 
     * @return boolean [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public boolean tryReleaseLock(final String key, final String uniqueId) {
        
        boolean broken = false;
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            
            final String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            
            Object result = jedis.eval(script, Collections.singletonList(key), 
                    Collections.singletonList(uniqueId));

            if (RELEASE_SUCCESS.equals(result)) {
                return true;
            }
            
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw e;
        } finally {
            closeResource(jedis, broken);
        }
        return false;
    }
    /**
     * 取队列消息，阻碍式
     * @param timeout   阻碍时间    0表示无限阻碍
     * @param keys       监听队列，不定参数，设置在前面的为优先队列
     * @return [参数说明]
     * 
     * @return List<String> [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<String> brpop(int timeout, String... keys) {
        boolean broken = false;
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            return jedis.brpop(timeout, keys);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw e;
        } finally {
            closeResource(jedis, broken);
        }
    }
	
    /**
     * 根据Key获取Val值
     * 效验key是否永久生效
     * 以秒为单位，返回给定 key 的剩余生存时间
     * @param key
     * @return [参数说明]
     * 
     * @return String [返回类型说明]
     * @exception throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public String getCheckTtl(final String key) {
        boolean broken = false;
        Jedis jedis = null;
        try {
            jedis = this.getJedis();
            
            Long ttlTime = jedis.ttl(key);
            if(null == ttlTime || ttlTime.longValue() <= 0){
                if(null!=ttlTime && ttlTime.longValue() == -1){
                    logger.warn("getCheckTtl key:{0},ttlTime:{1},jedis检查永久生效..", 
                            key, ttlTime);
                    boolean isFlg = jedis.del(key) > 0;
                    logger.warn("getCheckTtl key:{0},isFlg:{1},jedis删除..", 
                            key, isFlg);
                }
                return null;
            }
            
            return jedis.get(key);
        } catch (JedisException e) {
            broken = handleJedisException(e);
            throw e;
        } finally {
            closeResource(jedis, broken);
        }
    }

	public byte[] get(byte[] key) {
		byte[] result=null;
		boolean broken = false;
		Jedis jedis = null;
		try {
			jedis = this.getJedis();
			result=jedis.get(key);
		} catch (JedisException e) {
			broken = handleJedisException(e);
			throw e;
		} finally {
			closeResource(jedis, broken);
		}
		return result;
	}
    
    
}