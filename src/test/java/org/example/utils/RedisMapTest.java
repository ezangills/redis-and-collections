package org.example.utils;

import org.example.config.RedisConfiguration;
import org.example.util.RedisMap;
import org.junit.jupiter.api.*;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.util.KeyValue;

import java.util.*;

import static org.example.config.RedisConfiguration.NODES;

/**
 * At the moment requires redis cluster to be running for the tests to pass
 * I would not do it in real life code and I would either mock it, or more likely make these integration tests
 * In order to run the tests you need to remove the @Disabled annotation and launch redis cluster
 */
@Disabled
public class RedisMapTest {

    private JedisCluster cluster;
    private RedisMap map;

    @BeforeEach
    public void beforeEach() {
        cluster = new JedisCluster(NODES);
        map = new RedisMap(cluster, RedisConfiguration.REDIS_HASH_KEY);
        map.clear();
    }

    @AfterEach
    public void afterEach() {
        cluster.close();
    }

    @Test
    @DisplayName("Tests that put method works")
    public void putValues_successful() {
        Assertions.assertTrue(map.isEmpty());

        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);
        map.put("4", 4);
        map.put("5", 5);
        map.put("6", 6);

        Map<String, Integer> expected = new HashMap<>();

        expected.put("1", 1);
        expected.put("2", 2);
        expected.put("3", 3);
        expected.put("4", 4);
        expected.put("5", 5);
        expected.put("6", 6);

        Assertions.assertEquals(expected, map);
        Assertions.assertTrue(map.containsKey("1"));
        Assertions.assertTrue(map.containsKey("2"));
        Assertions.assertTrue(map.containsKey("3"));
        Assertions.assertTrue(map.containsKey("4"));
        Assertions.assertTrue(map.containsKey("5"));
        Assertions.assertTrue(map.containsKey("6"));
        Assertions.assertTrue(map.containsValue(1));
        Assertions.assertTrue(map.containsValue(2));
        Assertions.assertTrue(map.containsValue(3));
        Assertions.assertTrue(map.containsValue(4));
        Assertions.assertTrue(map.containsValue(5));
        Assertions.assertTrue(map.containsValue(6));
        Assertions.assertFalse(map.isEmpty());
    }

    @Test
    @DisplayName("Tests that put method updates existing entries")
    public void putValues_updateEntry_successful() {
        map.put("1", 1);
        Assertions.assertEquals(1, map.get("1"));
        map.put("1", 2);
        Assertions.assertNotEquals(1, map.get("1"));
        Assertions.assertEquals(2, map.get("1"));
    }

    @Test
    @DisplayName("Tests that after updating existing entries old value is correctly retrieved")
    public void putValues_updateEntry_oldValueRetrieved() {
        map.put("1", 1);
        Assertions.assertEquals(1, map.get("1"));
        Integer oldValue = map.put("1", 2);
        Assertions.assertEquals(1, oldValue);
    }

    @Test
    @DisplayName("Tests that if put is called first time no old value is retrieved")
    public void putValues_firstTime_noOldValue() {
        Integer oldValue = map.put("1", 1);
        Assertions.assertNull(oldValue);
    }

    @Test
    @DisplayName("Tests that size method works correctly")
    public void size_successful() {
        Assertions.assertEquals(0, map.size());
        map.put("1", 1);
        Assertions.assertEquals(1, map.size());
        map.put("2", 2);
        Assertions.assertEquals(2, map.size());
        map.put("3", 3);
        Assertions.assertEquals(3, map.size());
        map.put("3", 3);
        Assertions.assertEquals(3, map.size());
        map.remove("3");
        Assertions.assertEquals(2, map.size());
        map.remove("2");
        Assertions.assertEquals(1, map.size());
        map.remove("1");
        Assertions.assertEquals(0, map.size());
    }

    @Test
    @DisplayName("Tests that isEmpty method works correctly")
    public void isEmpty_successful() {
        Assertions.assertTrue(map.isEmpty());
        map.put("1", 1);
        Assertions.assertFalse(map.isEmpty());
        map.remove("1", 1);
        Assertions.assertTrue(map.isEmpty());
    }

    @Test
    @DisplayName("Tests that containsKey method works correctly")
    public void containsKey_successful() {
        Assertions.assertFalse(map.containsKey("1"));
        map.put("1", 1);
        Assertions.assertTrue(map.containsKey("1"));
        map.remove("1", 1);
        Assertions.assertFalse(map.containsKey("1"));
    }

    @Test
    @DisplayName("Tests that containsValue method works correctly")
    public void containsValue_successful() {
        Assertions.assertFalse(map.containsValue("1"));
        map.put("1", 1);
        Assertions.assertTrue(map.containsValue("1"));
        map.remove("1", 1);
        Assertions.assertFalse(map.containsValue("1"));
    }

    @Test
    @DisplayName("Tests that get method works correctly")
    public void get_successful() {
        Assertions.assertNull(map.get("1"));
        map.put("1", 1);
        Assertions.assertEquals(1, map.get("1"));
        map.remove("1", 1);
        Assertions.assertNull(map.get("1"));
    }

    @Test
    @DisplayName("Tests that remove method does not return any values if entry didn't exist")
    public void remove_noEntry_noOldValue() {
        Assertions.assertNull(map.remove("1"));
    }

    @Test
    @DisplayName("Tests that remove method does not return any values if entry didn't exist")
    public void remove_entryExisted_oldValueCorrect() {
        map.put("1", 1);
        Assertions.assertEquals(1, map.remove("1"));
    }

    @Test
    @DisplayName("Tests that remove method works")
    public void remove_successful() {
        map.put("1", 1);
        Assertions.assertEquals(1, map.size());
        map.remove("1");
        Assertions.assertEquals(0, map.size());
    }

    @Test
    @DisplayName("Tests that putAll method works")
    public void putAllValues_successful() {
        Assertions.assertTrue(map.isEmpty());

        Map<String, Integer> expected = new HashMap<>();
        expected.put("1", 1);
        expected.put("2", 2);
        expected.put("3", 3);
        expected.put("4", 4);
        expected.put("5", 5);
        expected.put("6", 6);

        map.putAll(expected);

        Assertions.assertEquals(expected, map);
        Assertions.assertTrue(map.containsKey("1"));
        Assertions.assertTrue(map.containsKey("2"));
        Assertions.assertTrue(map.containsKey("3"));
        Assertions.assertTrue(map.containsKey("4"));
        Assertions.assertTrue(map.containsKey("5"));
        Assertions.assertTrue(map.containsKey("6"));
        Assertions.assertTrue(map.containsValue(1));
        Assertions.assertTrue(map.containsValue(2));
        Assertions.assertTrue(map.containsValue(3));
        Assertions.assertTrue(map.containsValue(4));
        Assertions.assertTrue(map.containsValue(5));
        Assertions.assertTrue(map.containsValue(6));
        Assertions.assertFalse(map.isEmpty());
    }

    @Test
    @DisplayName("Tests that putAll method updates existing entries")
    public void putAllValues_updateEntry_successful() {
        map.put("1", 1);
        map.put("2", 2);
        Assertions.assertEquals(1, map.get("1"));
        Assertions.assertEquals(2, map.get("2"));
        Map<String, Integer> expected = new HashMap<>();
        expected.put("1", 3);
        expected.put("2", 4);
        map.putAll(expected);
        Assertions.assertNotEquals(1, map.get("1"));
        Assertions.assertNotEquals(2, map.get("2"));
        Assertions.assertEquals(3, map.get("1"));
        Assertions.assertEquals(4, map.get("2"));
    }

    @Test
    @DisplayName("Tests that clear method works")
    public void clear_successful() {
        Assertions.assertTrue(map.isEmpty());
        map.put("1", 1);
        map.put("2", 2);
        Assertions.assertFalse(map.isEmpty());
        map.clear();
        Assertions.assertTrue(map.isEmpty());
    }

    @Test
    @DisplayName("Tests that keySet is correctly retrieved")
    public void keySet_successful() {
        Assertions.assertTrue(map.keySet().isEmpty());
        map.put("1", 1);
        map.put("2", 2);
        Set<String> expected = new HashSet<>() {{
            add("1");
            add("2");
        }};
        Assertions.assertEquals(expected, map.keySet());
        map.clear();
        Assertions.assertTrue(map.keySet().isEmpty());
    }

    @Test
    @DisplayName("Tests that values is correctly retrieved")
    public void values_successful() {
        Assertions.assertTrue(map.values().isEmpty());
        map.put("1", 1);
        map.put("2", 2);
        Collection<Integer> expected = new ArrayList<>() {{
            add(1);
            add(2);
        }};
        Assertions.assertEquals(expected, map.values());
        map.clear();
        Assertions.assertTrue(map.values().isEmpty());
    }

    @Test
    @DisplayName("Tests that entrySet is correctly retrieved")
    public void entrySet_successful() {
        Assertions.assertTrue(map.entrySet().isEmpty());
        map.put("1", 1);
        map.put("2", 2);
        Set<Map.Entry<String, Integer>> expected = new HashSet<Map.Entry<String, Integer>>() {{
            add(new KeyValue<>("1", 1));
            add(new KeyValue<>("2", 2));
        }};
        Assertions.assertEquals(expected, map.entrySet());
        map.clear();
        Assertions.assertTrue(map.entrySet().isEmpty());
    }

    @Test
    @DisplayName("Tests that equals works properly by comparing same maps")
    public void equals_successful() {
        map.put("1", 1);

        Map<String, Integer> expected = new HashMap<>();

        expected.put("1", 1);

        Assertions.assertTrue(expected.equals(map));
    }

    @Test
    @DisplayName("Tests that equals works properly by comparing maps with different keys")
    public void equals_keyDifferent_notEquals() {
        map.put("1", 1);

        Map<String, Integer> expected = new HashMap<>();

        expected.put("2", 1);

        Assertions.assertFalse(expected.equals(map));
    }

    @Test
    @DisplayName("Tests that equals works properly by comparing maps with different values")
    public void equals_valueDifferent_notEquals() {
        map.put("1", 1);

        Map<String, Integer> expected = new HashMap<>();

        expected.put("1", 2);

        Assertions.assertFalse(expected.equals(map));
    }
}
