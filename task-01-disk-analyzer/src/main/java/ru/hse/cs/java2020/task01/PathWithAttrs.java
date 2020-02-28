package ru.hse.cs.java2020.task01;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

final class PathWithAttrs {
    private Path path;
    private BasicFileAttributes attrs;

    PathWithAttrs(Path p, BasicFileAttributes a) {
        this.path = p;
        this.attrs = a;
    }

    Path getPath() {
        return this.path;
    }

    BasicFileAttributes getAttrs() {
        return this.attrs;
    }

    long getSize() {
        return this.attrs.size();
    }

    String prettySize() {
        return TotalDiskUsage.formatBytes(this.attrs.size());
    }
}
