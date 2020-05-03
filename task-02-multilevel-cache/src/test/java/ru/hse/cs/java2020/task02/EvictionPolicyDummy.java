package ru.hse.cs.java2020.task02;

public class EvictionPolicyDummy<T> implements EvictionResolver<T> {
    static class Builder<T> implements EvictionPolicy<T> {
        @Override
        public EvictionResolver<T> buildResolver() {
            return new EvictionPolicyDummy<T>();
        }
    }

    @Override
    public void notifyPut(T id, long size) {
    }

    @Override
    public void notifyGet(T id) {
    }

    @Override
    public void notifyRemove(T id) {
    }

    @Override
    public T findEvict() {
        throw new UnsupportedOperationException();
    }
}
