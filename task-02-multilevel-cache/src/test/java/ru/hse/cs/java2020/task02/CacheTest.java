package ru.hse.cs.java2020.task02;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import static org.junit.Assert.*;

public class CacheTest {
    private static Random random = new Random();

    public static void fuzzTestCache(Cache<Long, String> cache) {
        final int maxKey = 100;
        final int maxLength = 1000;
        final int iterations = 100000;

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
