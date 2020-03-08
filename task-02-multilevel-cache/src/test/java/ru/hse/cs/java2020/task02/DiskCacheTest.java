package ru.hse.cs.java2020.task02;

import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class DiskCacheTest {
    @Test
    public void testSaveRestore() throws Exception {
        final String[] values = {"myyyyyy", "preciouuuuuuus", "striiiiiiiiiiings", "abacabadaba", "o", "", "ololo", "ulele"};

        Path tempDir = Files.createTempDirectory("testSaveRestore");
        Path tempFile = tempDir.resolve("cache.dat");

        final long maxSize = 4096;
        EvictionPolicy<Long> policy = new EvictionPolicyDummy();

        for (int i = 0; i < values.length; i++) {
            try (DiskCache cache = new DiskCache(tempFile, maxSize, policy)) {
                for (int j = 0; j < i; j++) {
                    assertEquals(values[j], cache.get((long) j).get());
                }

                cache.put((long) i, values[i]);
            }
        }
    }

    @Test
    public void testCompact() throws Exception {
        final String[] values = {"a", "b", "c", "d", "e", "f", "g", "h"};

        Path tempDir = Files.createTempDirectory("testCompact");
        Path tempFile = tempDir.resolve("cache.dat");

        final int iterations = 10000;
        final long maxSize = 4096;
        EvictionPolicy<Long> policy = new LRUPolicy<>();

        try (DiskCache cache = new DiskCache(tempFile, maxSize, policy)) {
            for (int it = 0; it < iterations; it++) {
                long key = it % values.length;

                Optional<String> prev = cache.put(key, values[(int) key]);
                assertEquals(values[(int) key], prev.orElse(values[(int) key]));
            }
        }
    }

    @Test
    public void fuzzTest() throws Exception {
        Path tempDir = Files.createTempDirectory("fuzzTest");
        Path tempFile = tempDir.resolve("cache.dat");

        final long maxSize = 1024 * 1024 * 2;
        EvictionPolicy<Long> policy = new LRUPolicy<>();

        try (DiskCache cache = new DiskCache(tempFile, maxSize, policy)) {
            CacheTest.fuzzTestCache(cache);
        }
    }
}
