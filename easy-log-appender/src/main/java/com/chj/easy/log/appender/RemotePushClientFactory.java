package com.chj.easy.log.appender;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * description TODO
 * company 铁人科技
 *
 * @author 陈浩杰
 * @date 2023/7/13 10:42
 */
public class RemotePushClientFactory {

    private final static int TIMEOUT = 1000;

    public static Jedis getJedis(String host, int port, String password, int database) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(30);
        config.setMaxIdle(10);
        config.setTestOnBorrow(true);
        JedisPool jedisPool = null;
        if (password != null && !"".equals(password)) {
            jedisPool = new JedisPool(config, host, port, TIMEOUT, password, database);
        } else {
            jedisPool = new JedisPool(config, host, port, TIMEOUT, null, database);
        }
        return jedisPool.getResource();
    }
}
