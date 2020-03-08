package ru.hse.cs.java2020.task02;

public class EvictionPolicyDummy implements EvictionPolicy<Long> {
    @Override
    public void notifyPut(Long id, long size) {
    }

    @Override
    public void notifyGet(Long id) {
    }

    @Override
    public void notifyRemove(Long id) {
    }

    @Override
    public Long findEvict() {
        throw new UnsupportedOperationException();
    }
}
