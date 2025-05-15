package org.example.util;

import redis.clients.jedis.commands.JedisCommands;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class that implements java.util.Map but utilizes redis to contain all the data
 */
public class RedisMap implements Map<String,Integer> {
    private final JedisCommands commands;
    private final String redisHashKey;

    /**
     * Initializes redis map
     * @param commands can be a cluster or can be a single instance of redis
     * @param redisHashKey redis hash key
     */
    public RedisMap(JedisCommands commands, String redisHashKey) {
        this.commands = commands;
        this.redisHashKey = redisHashKey;
    }

    /**
     * Returns number of elements stored in redis
     * @return number of elements stored in redis
     */
    @Override
    public int size() {
        long len = commands.hlen(redisHashKey);
        return (int) len;
    }

    /**
     * Checks whether redis is empty or not
     * @return whether redis is empty or not
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Checks whether redis has an entry with that key or not
     * @param key key to be checked
     * @return whether redis has an entry with that key or not
     */
    @Override
    public boolean containsKey(Object key) {
        return commands.hexists(redisHashKey, key.toString());
    }

    /**
     * Checks whether redis has an entry with that value or not
     * @param value value to be checked
     * @return whether redis has an entry with that value or not
     */
    @Override
    public boolean containsValue(Object value) {
        return commands.hvals(redisHashKey).contains(value.toString());
    }

    /**
     * Returns value from an entry with that key
     * @param key the key whose value is to be returned
     * @return value from an entry with that key, or NULL if an entry with such key does not exist
     */
    @Override
    public Integer get(Object key) {
        String value = commands.hget(redisHashKey, key.toString());
        return (value != null) ? Integer.valueOf(value) : null;
    }

    /**
     * Creates a new entry with key value pair
     * @param key key
     * @param value value
     * @return old value if an entry with such key previously existed, or NULL otherwise
     */
    @Override
    public Integer put(String key, Integer value) {
        String old = commands.hget(redisHashKey, key);
        commands.hset(redisHashKey, key, value.toString());
        return (old != null) ? Integer.valueOf(old) : null;
    }

    /**
     * Removes an entry with that key
     * @param key key of entry to be removed
     * @return value of that entry, or NULL if an entry with such a key was not present
     */
    @Override
    public Integer remove(Object key) {
        String k = key.toString();
        String old = commands.hget(redisHashKey, k);
        commands.hdel(redisHashKey, k);
        return (old != null) ? Integer.valueOf(old) : null;
    }

    /**
     * Same as put but instead of single entry it enters a map of entries
     * @param map map to be inserted into the redis
     */
    @Override
    public void putAll(Map<? extends String, ? extends Integer> map) {
        Map<String, String> toStore = new HashMap<>();
        for (Entry<? extends String, ? extends Integer> e : map.entrySet()) {
            toStore.put(e.getKey(), e.getValue().toString());
        }
        commands.hset(redisHashKey, toStore);
    }

    /**
     * Removes all values from redis
     */
    @Override
    public void clear() {
        commands.del(redisHashKey);
    }

    /**
     * Returns a set of keys that are present in redis
     * @return a set of keys that are present in redis
     */
    @Override
    public Set<String> keySet() {
        return commands.hkeys(redisHashKey);
    }

    /**
     * Returns a collection of values that are present in redis
     * @return a collection of values that are present in redis
     */
    @Override
    public Collection<Integer> values() {
        return commands.hvals(redisHashKey).stream()
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }

    /**
     * Returns a set of entries that are present in redis
     * @return a set of entries that are present in redis
     */
    @Override
    public Set<Entry<String,Integer>> entrySet() {
        return commands.hgetAll(redisHashKey).entrySet().stream()
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), Integer.valueOf(e.getValue())))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Map map)) return false;
        return Objects.equals(entrySet(), map.entrySet());
    }

    @Override
    public int hashCode() {
        return Objects.hash(entrySet().hashCode());
    }

}
