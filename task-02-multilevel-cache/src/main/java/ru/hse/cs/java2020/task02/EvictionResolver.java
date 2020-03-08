package ru.hse.cs.java2020.task02;

public interface EvictionResolver<K> {
    // must be called after every put
    void notifyPut(K id, long size);

    // must be called after every get
    void notifyGet(K id);

    // must be called after remove
    void notifyRemove(K id);

    // returns key chosen to be evicted
    K findEvict();
}
