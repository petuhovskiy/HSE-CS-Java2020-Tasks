package ru.hse.cs.java2020.task02;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public final class CacheTest {
    private static Random random = new Random();

    private CacheTest() {
    }

    public static void fuzzTestCache(Cache<Long, String> cache) {
        final int keys = 100;
        final int size = 1000;
        final int iterations = 100000;
        fuzzTestCache(cache, keys, size, iterations);
    }

    public static void fuzzTestCache(Cache<Long, String> cache, int maxKey, int maxLength, int iterations) {
        Map<Long, String> map = new HashMap<>();
        for (int it = 0; it < iterations; it++) {
            final long key = random.nextInt(maxKey);
            final int length = random.nextInt(maxLength);
            final String val = generateString(length);

            Optional<String> prev = cache.put(key, val);

            prev.ifPresent(s -> assertEquals(map.get(key), s));
            map.put(key, val);
        }
    }

    public static String generateString(int length) {
        char[] arr = new char[length];
        for (int i = 0; i < length; i++) {
            arr[i] = generateChar();
        }

        return String.valueOf(arr);
    }

    public static char generateChar() {
        final char l = 'a';
        final char r = 'z';
        return (char) (l + random.nextInt(r - l + 1));
    }
}
