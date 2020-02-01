package ru.hse.cs.java2020.task01;

final class DirStats {
    private final long totalSize;
    private final int count;
    private final PathWithAttrs root;

    DirStats(long ts, int c, PathWithAttrs r) {
        this.totalSize = ts;
        this.count = c;
        this.root = r;
    }

    DirStats(PathWithAttrs pa) {
        this(pa.getSize(), 1, null);
    }

    long getTotalSize() {
        return this.totalSize;
    }

    int getCount() {
        return this.count;
    }

    PathWithAttrs getRoot() {
        return this.root;
    }

    DirStats withRoot(PathWithAttrs newRoot) {
        return new DirStats(this.totalSize, this.count, newRoot);
    }

    static DirStats combine(DirStats a, DirStats b) {
        return new DirStats(a.totalSize + b.totalSize, a.count + b.count, null);
    }
}
