package org.example;

import org.example.util.RedisMap;
import redis.clients.jedis.JedisCluster;

import java.util.HashMap;
import java.util.Map;

import static org.example.config.RedisConfiguration.*;

/**
 * Just for DEMO purposes
 */
public class Main {

    public static void main(String[] args) {
        // try (Jedis jedis = new Jedis(LOCALHOST, 7001)) { // this line would be for running single instance of redis instead of cluster
        try (JedisCluster cluster = new JedisCluster(NODES)) {
            RedisMap map = new RedisMap(cluster, REDIS_HASH_KEY);
            System.out.println("Clearing out map (redis)");
            map.clear();

            System.out.println("Map is empty, isTrue=" + (map.isEmpty()));

            System.out.println("Starting to insert values");
            map.put("1", 1);
            map.put("2", 2);
            map.put("3", 3);
            map.put("4", 4);
            map.put("5", 5);
            map.put("6", 6);

            System.out.println("Key [1], value [1], isTrue=" + (map.get("1") == 1));
            System.out.println("Key [2], value [2], isTrue=" + (map.get("2") == 2));
            System.out.println("Key [3], value [3], isTrue=" + (map.get("3") == 3));
            System.out.println("Key [4], value [4], isTrue=" + (map.get("4") == 4));
            System.out.println("Key [5], value [5], isTrue=" + (map.get("5") == 5));
            System.out.println("Key [6], value [6], isTrue=" + (map.get("6") == 6));

            System.out.println("Contains key [1], isTrue=" + (map.containsKey("1")));
            System.out.println("Contains key [2], isTrue=" + (map.containsKey("2")));
            System.out.println("Contains key [3], isTrue=" + (map.containsKey("3")));
            System.out.println("Contains key [4], isTrue=" + (map.containsKey("4")));
            System.out.println("Contains key [5], isTrue=" + (map.containsKey("5")));
            System.out.println("Contains key [6], isTrue=" + (map.containsKey("6")));
            System.out.println("Does not contain key [7], isTrue=" + (!map.containsKey("7")));

            System.out.println("Contains value [1], isTrue=" + (map.containsValue(1)));
            System.out.println("Contains value [2], isTrue=" + (map.containsValue(2)));
            System.out.println("Contains value [3], isTrue=" + (map.containsValue(3)));
            System.out.println("Contains value [4], isTrue=" + (map.containsValue(4)));
            System.out.println("Contains value [5], isTrue=" + (map.containsValue(5)));
            System.out.println("Contains value [6], isTrue=" + (map.containsValue(6)));

            System.out.println("Replacing the value by key");
            Integer oldValue = map.put("6", 7);

            System.out.println("Key [6], value [7], isTrue=" + (map.get("6") == 7));
            System.out.println("Does not contain value [6], isTrue=" + (!map.containsValue(6)));
            System.out.println("Contains value [7], isTrue=" + (map.containsValue(7)));
            System.out.println("Old value from map with key [6], equals to 6, isTrue=" + (oldValue == 6));


            System.out.println("Size = 6, isTrue=" + (map.size() == 6));

            System.out.println("Removing an entry");
            Integer removedValue = map.remove("2");
            System.out.println("Does not contain key [2], isTrue=" + (!map.containsKey("2")));
            System.out.println("Removed value from map with key [2], equals to 2, isTrue=" + (removedValue == 2));

            System.out.println("Size = 5, isTrue=" + (map.size() == 5));

            System.out.println("Map is not empty, isTrue=" + (!map.isEmpty()));

            System.out.println("All entries:");
            for (Map.Entry<String,Integer> e : map.entrySet()) {
                System.out.printf("  %s => %d%n", e.getKey(), e.getValue());
            }

            System.out.println("Putting a map into this redis map");
            Map<String, Integer> secondMap = new HashMap<>();
            secondMap.put("8", 8);
            secondMap.put("9", 9);
            secondMap.put("10", 10);

            map.putAll(secondMap);

            System.out.println("Key [1], value [1], isTrue=" + (map.get("1") == 1));
            System.out.println("Key [3], value [3], isTrue=" + (map.get("3") == 3));
            System.out.println("Key [4], value [4], isTrue=" + (map.get("4") == 4));
            System.out.println("Key [5], value [5], isTrue=" + (map.get("5") == 5));
            System.out.println("Key [6], value [7], isTrue=" + (map.get("6") == 7));
            System.out.println("Key [8], value [8], isTrue=" + (map.get("8") == 8));
            System.out.println("Key [9], value [9], isTrue=" + (map.get("9") == 9));
            System.out.println("Key [10], value [10], isTrue=" + (map.get("10") == 10));

            System.out.println("All entries:");
            for (Map.Entry<String,Integer> e : map.entrySet()) {
                System.out.printf("  %s => %d%n", e.getKey(), e.getValue());
            }

            System.out.println("Map key set: " + map.keySet());
            System.out.println("Map values: " + map.values());
            System.out.println("Map entry set: " + map.entrySet());

            System.out.println("getOrDefault for key [5] returned real value [5], isTrue=" + (map.getOrDefault("5", 99) == 5));
            System.out.println("getOrDefault for key [11] returned default value [99], isTrue=" + (map.getOrDefault("11", 99) == 99));

            System.out.println("forEach works: ");
            map.forEach((k, v) -> {
                System.out.println("Key: " + k + "; Value: " + v);
            });

            System.out.println("Clearing out a map by calling remove method");
            for (int i = 0; i <= 10; i++) {
                map.remove(String.valueOf(i));
            }

            System.out.println("Map is empty, isTrue=" + (map.isEmpty()));

            System.out.println("Inserting an entry again to clear out the map by calling clear method");
            map.put("1", 1);

            System.out.println("Map is not empty, isTrue=" + (!map.isEmpty()));

            map.clear();

            System.out.println("Map is empty, isTrue=" + (map.isEmpty()));
        }
    }
}
