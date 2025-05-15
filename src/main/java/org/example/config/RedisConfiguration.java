package org.example.config;

import redis.clients.jedis.HostAndPort;

import java.util.HashSet;
import java.util.Set;

/**
 * Hardcoded values just for the sake of the test
 * In real life I would utilize properties to set this up
 */
public class RedisConfiguration {
    public static final String LOCALHOST = "127.0.0.1";
    public static final Set<HostAndPort> NODES = new HashSet<>() {{
        add(new HostAndPort(LOCALHOST, 7001));
        add(new HostAndPort(LOCALHOST, 7002));
        add(new HostAndPort(LOCALHOST, 7003));
        add(new HostAndPort(LOCALHOST, 7004));
        add(new HostAndPort(LOCALHOST, 7005));
    }};
    public static final String REDIS_HASH_KEY = "TEST_REDIS_MAP";
}
