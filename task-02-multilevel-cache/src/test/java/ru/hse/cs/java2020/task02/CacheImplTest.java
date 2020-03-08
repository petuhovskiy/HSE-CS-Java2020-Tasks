package ru.hse.cs.java2020.task02;

import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;

public class CacheImplTest {
    @Test
    public void fuzzTestBig() throws Exception {
        Path tempDir = Files.createTempDirectory("fuzzTest");

        final long memorySize = 1024 * 128;
        final long diskSize = 1024 * 1024 * 2;
        EvictionPolicy<Long> policy = new LRUPolicy.Builder<>();

        try (CacheImpl cache = new CacheImpl(memorySize, diskSize, tempDir.toString(), policy)) {
            final int keys = 2000;
            final int size = 1024 * 16;
            final int iterations = 100000;
            CacheTest.fuzzTestCache(cache, keys, size, iterations);
        }
    }

    @Test
    public void fuzzTest() throws Exception {
        Path tempDir = Files.createTempDirectory("fuzzTest");

        final long memorySize = 1024 * 128;
        final long diskSize = 1024 * 1024 * 2;
        EvictionPolicy<Long> policy = new LRUPolicy.Builder<>();

        try (CacheImpl cache = new CacheImpl(memorySize, diskSize, tempDir.toString(), policy)) {
            CacheTest.fuzzTestCache(cache);
        }
    }

    @Test
    public void fuzzTest2() throws Exception {
        Path tempDir = Files.createTempDirectory("fuzzTest");

        final long memorySize = 1024 * 128;
        final long diskSize = 1024 * 1024 * 2;
        EvictionPolicy<Long> policy = new LRUPolicy.Builder<>();

        try (CacheImpl cache = new CacheImpl(memorySize, diskSize, tempDir.toString(), policy)) {
            final int keys = 100;
            final int size = 1024 * 1024;
            final int iterations = 1000;
            CacheTest.fuzzTestCache(cache, keys, size, iterations);
        }
    }

    @Test
    public void fuzzTestLFU() throws Exception {
        Path tempDir = Files.createTempDirectory("fuzzTest");

        final long memorySize = 1024 * 128;
        final long diskSize = 1024 * 1024 * 2;
        EvictionPolicy<Long> policy = new LFUPolicy.Builder<>();

        try (CacheImpl cache = new CacheImpl(memorySize, diskSize, tempDir.toString(), policy)) {
            CacheTest.fuzzTestCache(cache);
        }
    }
}
