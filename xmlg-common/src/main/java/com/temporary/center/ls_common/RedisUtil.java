package com.temporary.center.ls_common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.util.Pool;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("redis")
public class RedisUtil {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    // 分片客户端连接池
    @Autowired(required = false)
    private Pool<Jedis> jedisPool;

    public Pool<Jedis> getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(Pool<Jedis> jedisPool) {
        this.jedisPool = jedisPool;
    }

    public <T> T execute(Function<T, Jedis> fun) {
        Jedis Jedis = null;
        try {
            // 从连接池中获取到jedis分片对象
            Jedis = getShardedJedis();
            return fun.callback(Jedis);
        } finally {
            if (null != Jedis) {
                // 关闭，检测连接是否有效，有效则放回到连接池中，无效则重置状态
                Jedis.close();
            }
        }
    }

    private Jedis getShardedJedis() {
        return jedisPool.getResource();
    }

    /**
     * 执行set操作
     *
     * @param key
     * @param value
     * @return
     */
    public String set(final String key, final String value) {
        return this.execute((Jedis e) -> e.set(key, value));
    }

    /*
     * key 自增
     */
    public Long incr(final String key) {
        return incr(key, 1L);
    }

    /*
     * key 自增
     */
    public Long incr(final String key, final Long val) {
        return this.execute((Jedis e) -> e.incrBy(key, val));
    }

    /**
     * 执行set操作
     *
     * @param keys
     * @param values
     * @return
     * @Param seconds
     */
    public String batchSet(final List<String> keys, final List<String> values, final Integer seconds) {
        return this.execute((Jedis e) -> {
                if (keys == null || keys.size() < 1 || values == null || values.size() < 1 || keys.size() != values.size())
                    return null;

                Pipeline p = e.pipelined();
                int len = keys.size();

                for (int i = 0; i < len; i++) {
                    p.set(keys.get(i), values.get(i));
                }
                p.sync();
                return null;
        });
    }

    /**
     * 执行get操作
     *
     * @param key
     * @return
     */
    public String get(final String key) {
        return this.execute((Jedis e) -> e.get(key));
    }

    /**
     * 执行get操作模糊查询
     *
     * @param key
     * @return
     */
    public String[] getKeysByRegex(final String key) {
        return this.execute((Jedis e) -> {
                Set<String> keys = e.keys(key);
                String[] results = keys.toArray(new String[0]);
                return results;
        });
    }

    /**
     * 是否存在此 key
     *
     * @param key
     * @return
     */
    public boolean exsit(final String key) {
        return this.execute((Jedis e) -> e.exists(key));
    }

    /**
     * 执行删除操作
     *
     * @param key
     * @return
     */
    public Long del(final String key) {
        return this.execute((Jedis e) -> e.del(key));
    }

    /**
     * 设置生存时间，单位为：秒
     *
     * @param key
     * @param seconds
     * @return
     */
    public Long expire(final String key, final Integer seconds) {
        return this.execute((Jedis e) -> e.expire(key, seconds));
    }

    /**
     * 执行set操作并且设置生存时间，单位为：秒
     *
     * @param key
     * @param value
     * @return
     */
    public String set(final String key, final String value, final Integer seconds) {
        return this.execute((Jedis e) -> {
                String str = e.set(key, value);
                e.expire(key, seconds);
                return str;
        });
    }

    /**
     * hset 添加
     *
     * @param key
     * @param value
     * @return
     */
    public Long setHash(final String key, final String field, final String value) {
        return this.execute((Jedis e) -> e.hset(key, field, value));
    }

    /**
     * hset 添加
     *
     * @param key
     * @param value
     * @return
     */
    public void setSpiderHash(String keyh, String key, final String field, final String value) {
        final String keyv = keyh + key;
        this.execute((Jedis e) -> e.hset(keyv, field, value));
    }

    /**
     * hmset
     *
     * @param key
     * @param hash
     * @return
     */
    public String setHash(final String key, final Map<String, String> hash) {
        return this.execute((Jedis e) -> e.hmset(key, hash));
    }

    /**
     * hast 某个字段自增
     *
     * @param key
     * @param val
     * @return
     */
    public Long hashIncr(final String key, final String field, final Long val) {
        return this.execute((Jedis e) -> e.hincrBy(key, field, val));
    }

    /**
     * hset get
     *
     * @param key
     * @param field
     * @return
     */
    public String getHash(final String key, final String field) {
        return this.execute((Jedis e) -> e.hget(key, field));
    }

    /**
     * hset get
     *
     * @param keyh
     * @param key
     * @param field
     * @return
     */
    public String getSpiderHash(final String keyh, final String key, final String field) {
        final String keyv = keyh + key;
        return this.execute((Jedis e) -> e.hget(keyv, field));
    }

    /**
     * hset  del
     *
     * @param keyh
     * @param key
     * @param field
     * @return
     */
    public Long delSpiderHash(final String keyh, final String key, final String field) {
        return this.execute((Jedis e) -> e.hdel(key, field));
    }

    /**
     *
     */
    /**
     * hset 删除
     *
     * @param key
     * @param field
     * @return
     */
    public Long deleteHash(final String key, final String... field) {
        return this.execute((Jedis e) -> e.hdel(key, field));
    }

    /**
     * 判断队列是否存在数据
     * @param key
     */
    public boolean lLen(final String key) {
        return this.execute((Jedis e) -> e.llen(key.toString())>0);
    }

    /**
     * 插入队列
     *
     * @param key value
     */
    public Long rpush(final String key, final String... value) {
        return this.execute((Jedis e) -> e.rpush(key, value));
    }

    /**
     * 读取队列
     *
     * @param key
     */
    public String lpop(final String key) {
        return this.execute((Jedis e) -> e.lpop(key.toString()));
    }

    /**
     * 返回Set集合中的所有元素
     *
     * @param key
     * @return
     */
    public Set<String> getAllSetElement(String key) {
        return this.execute((Jedis e) -> e.smembers(key));
    }


    /**
     * 获取list数据
     *
     * @param key
     * @return
     */
    public List<String> getAllListElement(final String key) {
        return this.execute((Jedis e) -> e.lrange(key, 0, -1));
    }


    /**
     * 获取list数据
     *
     * @param key
     * @return
     */
    public Map<String, String> getAllHashElement(final String key) {
        return this.execute((Jedis e) -> e.hgetAll(key));
    }
}
