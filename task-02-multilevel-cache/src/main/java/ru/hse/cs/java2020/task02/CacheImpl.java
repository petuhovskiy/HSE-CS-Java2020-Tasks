package ru.hse.cs.java2020.task02;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class CacheImpl implements Cache<Long, String>, AutoCloseable {
    private static final long ENTRY_WEIGHT = 4;

    private final DiskCache nextLevel;
    private final EvictionResolver<Long> policy;
    private final Map<Long, String> cache;
    private final long maxMemorySize;
    private long currentSize;

    public CacheImpl(long memorySize, long diskSize, String path, EvictionPolicy<Long> policy) throws IOException {
        Path file = Path.of(path, "cache.dat");

        this.nextLevel = new DiskCache(file, diskSize, policy);
        this.policy = policy.buildResolver();
        this.cache = new HashMap<>();
        this.maxMemorySize = memorySize;
        this.currentSize = 0;
    }

    @Override
    public Optional<String> get(Long key) {
        Objects.requireNonNull(key);

        String value = this.cache.get(key);
        if (value != null) {
            this.policy.notifyGet(key);
            return Optional.of(value);
        }

        Optional<String> next = this.nextLevel.get(key);
        if (next.isPresent() && this.weightOf(next.get().length()) < maxMemorySize) {
            this.saveInMemory(key, next.get());
            return next;
        }

        return Optional.empty();
    }

    @Override
    public Optional<String> put(Long key, String value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);

        Optional<String> old = get(key);

        // if value does not fit in memory
        if (this.weightOf(value.length()) >= maxMemorySize) {
            this.remove(key);
            this.nextLevel.put(key, value);
            return old;
        }

        this.saveInMemory(key, value);
        return old;
    }

    private void saveInMemory(Long key, String value) {
        // remove obsolete value from nextLevel cache
        try {
            this.nextLevel.remove(key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // remove obsolete value from this level cache
        this.remove(key);

        // free up space
        long size = value.length();
        while (this.currentSize + this.weightOf(size) > maxMemorySize) {
            Long evictKey = this.policy.findEvict();
            if (evictKey == null) {
                throw new RuntimeException("unexpected null evictKey");
            }

            String evictValue = this.cache.get(evictKey);
            if (evictValue == null) {
                throw new RuntimeException("unexpected non found evictKey");
            }

            this.remove(evictKey);
            this.nextLevel.put(evictKey, evictValue);
        }

        // put in memory cache
        this.policy.notifyPut(key, size);
        this.cache.put(key, value);
        this.currentSize += this.weightOf(size);
    }

    private void remove(Long key) {
        String value = this.cache.get(key);
        if (value == null) {
            return;
        }

        this.policy.notifyRemove(key);
        this.cache.remove(key);
        this.currentSize -= this.weightOf(value.length());
    }

    private long weightOf(long size) {
        return size + ENTRY_WEIGHT;
    }

    @Override
    public void close() throws Exception {
        this.nextLevel.close();
    }
}
