package ru.hse.cs.java2020.task02;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;

public class LRUPolicy<T> implements EvictionPolicy<T> {
    static class Entry<T> {
        private final T key;
        private final Instant moment;

        Entry(T key, Instant moment) {
            this.key = key;
            this.moment = moment;
        }
    };

    private Map<T, Instant> lastAccess;
    private Queue<Entry<T>> queue;

    public LRUPolicy() {
        this.lastAccess = new HashMap<>();
        this.queue = new ArrayDeque<>();
    }

    private void put(T id) {
        Instant now = Instant.now();
        this.lastAccess.put(id, now);
        this.queue.add(new Entry<>(id, now));
    }

    @Override
    public void notifyPut(T id, long size) {
        this.put(id);
    }

    @Override
    public void notifyGet(T id) {
        this.put(id);
    }

    @Override
    public void notifyRemove(T id) {
        this.lastAccess.remove(id);
    }

    @Override
    public T findEvict() {
        while (!this.queue.isEmpty()) {
            Entry<T> entry = this.queue.element();
            if (Objects.equals(this.lastAccess.get(entry.key), entry.moment)) {
                return entry.key;
            }
            this.queue.remove();
        }
        return null;
    }
}
