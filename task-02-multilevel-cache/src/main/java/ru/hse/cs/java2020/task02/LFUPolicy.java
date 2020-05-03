package ru.hse.cs.java2020.task02;

import java.util.HashMap;
import java.util.LinkedHashSet;

public class LFUPolicy<T> implements EvictionResolver<T> {
    static class Builder<T> implements EvictionPolicy<T> {
        @Override
        public EvictionResolver<T> buildResolver() {
            return new LFUPolicy<>();
        }
    }

    private final HashMap<T, Integer> count;
    private final HashMap<Integer, LinkedHashSet<T>> freqs;

    private int minFreq;

    public LFUPolicy() {
        this.count = new HashMap<>();
        this.freqs = new HashMap<>();
        this.minFreq = 0;
    }

    private void putFreq(T id, int freq) {
        if (freq < this.minFreq) {
            this.minFreq = freq;
        }

        freqs
            .computeIfAbsent(freq, k -> new LinkedHashSet<>())
            .add(id);
    }

    private void removeFreq(T id, int freq) {
        LinkedHashSet<T> set = freqs.get(freq);
        if (set == null) {
            return;
        }
        set.remove(id);
        if (set.isEmpty()) {
            this.freqs.remove(freq);
        }
    }

    @Override
    public void notifyPut(T id, long size) {
        this.notifyRemove(id);
        this.count.put(id, 0);
        this.putFreq(id, 0);
    }

    @Override
    public void notifyGet(T id) {
        Integer freq = this.count.get(id);
        this.removeFreq(id, freq);
        freq++;
        this.putFreq(id, freq);
        this.count.put(id, freq);
    }

    @Override
    public void notifyRemove(T id) {
        Integer freq = this.count.get(id);
        if (freq == null) {
            return;
        }
        this.removeFreq(id, freq);
        this.count.remove(id);
    }

    @Override
    public T findEvict() {
        while (true) {
            LinkedHashSet<T> set = this.freqs.get(this.minFreq);
            if (!set.isEmpty()) {
                return set.iterator().next();
            }

            this.minFreq++;
        }
    }
}
