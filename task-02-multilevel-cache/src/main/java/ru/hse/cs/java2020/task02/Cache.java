package ru.hse.cs.java2020.task02;

import java.util.Optional;

public interface Cache<K, V> {
    Optional<V> get(K key);
    Optional<V> put(K key, V value);
}
