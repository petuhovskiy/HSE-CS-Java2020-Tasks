package ru.hse.cs.java2020.task02;

import java.nio.file.Files;

public class Main {
    public static void main(String[] args) throws Exception {
        final String cacheDir = Files.createTempDirectory("cacheMain").toString();
        final long memorySize = 1024 * 1024;
        final long diskSize = 1024 * 1024 * 100;
        final EvictionPolicy<Long> policy = new LFUPolicy.Builder<>();

        try (CacheImpl cache = new CacheImpl(memorySize, diskSize, cacheDir, policy)) {
            System.out.println(cache.get(0L));
            System.out.println(cache.put(0L, ""));
            System.out.println(cache.put(0L, "abacabadaba"));
            System.out.println(cache.put(0L, "test"));
            System.out.println(cache.get(0L));
            System.out.println(cache.put(1L, "string"));
            System.out.println(cache.put(1L, "strong"));
            System.out.println(cache.get(1L));
        }
    }
}
