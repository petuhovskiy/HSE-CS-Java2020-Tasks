package ru.hse.cs.java2020.task02;

public interface EvictionPolicy<K> {
    EvictionResolver<K> buildResolver();
}
