package ru.hse.cs.java2020.task01;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

public class TotalDiskUsage {
    static class PathWithAttrs {
        Path path;
        BasicFileAttributes attrs;

        PathWithAttrs(Path path, BasicFileAttributes attrs) {
            this.path = path;
            this.attrs = attrs;
        }

        String prettySize() {
            return TotalDiskUsage.formatBytes(this.attrs.size());
        }

        long getSize() {
            return this.attrs.size();
        }
    }

    static class DirStats {
        long totalSize;
        int count;
        PathWithAttrs root;

        DirStats(long totalSize, int count) {
            this.totalSize = totalSize;
            this.count = count;
        }

        DirStats(PathWithAttrs pa) {
            this.totalSize = pa.getSize();
            this.count = 1;
        }

        static DirStats combine(DirStats a, DirStats b) {
            return new DirStats(a.totalSize + b.totalSize, a.count + b.count);
        }
    }

    public static void printDiskUsage(String dir) throws IOException {
        Path root = Paths.get(dir);
        List<PathWithAttrs> allPaths = listSubtree(root);

        long totalSize = allPaths
                .stream()
                .mapToLong(PathWithAttrs::getSize)
                .sum();

        System.out.println();
        printDirStats(root, allPaths, totalSize);

        System.out.println();
        printBiggestFiles(root, allPaths);
    }

    private static void printDirStats(Path root, List<PathWithAttrs> paths, long totalSize) {
        List<DirStats> dirStats = new ArrayList<>();

        paths
            .stream()
            .filter(it -> !it.path.equals(root))
            .map(it -> new PathWithAttrs(root.relativize(it.path), it.attrs))
            .filter(it -> it.path.getNameCount() >= 1)
            .collect(Collectors.groupingBy(it -> it.path.getName(0)))
            .forEach((path, children) -> {
                Optional<DirStats> optStats = children
                        .stream()
                        .map(DirStats::new)
                        .reduce(DirStats::combine);

                if (optStats.isEmpty()) {
                    return;
                }

                Optional<PathWithAttrs> optRoot =
                    children
                        .stream()
                        .filter(it -> it.path.equals(path))
                        .findAny();

                if (optRoot.isEmpty()) {
                    return;
                }

                DirStats stats = optStats.get();
                stats.root = optRoot.get();
                dirStats.add(stats);
            });

        dirStats.sort(Comparator.comparingLong(it -> -it.totalSize));

        for (int i = 0; i < dirStats.size(); i++) {
            int index = i + 1;
            DirStats stats = dirStats.get(i);
            PathWithAttrs dir = stats.root;
            String path = dir.path.toString();
            String prettySize = formatBytes(stats.totalSize);
            String percent = formatPercent(stats.totalSize, totalSize);
            int items = stats.count - 1;

            if (dir.attrs.isDirectory()) {
                path += "/";
            } else {
                items = -1;
            }

            System.out.printf("%2d. %20s| %10s| %7s|", index, path, prettySize, percent);
            if (items != -1) {
                System.out.printf("%6d items", items);
            }
            System.out.println();
        }
    }

    private static void printBiggestFiles(Path root, List<PathWithAttrs> paths) {
        System.out.println("----------- BIGGEST FILES -----------");

        List<PathWithAttrs> biggestFiles = paths
                .stream()
                .filter(it -> it.attrs.isRegularFile())
                .sorted(Comparator.comparingLong(it -> -it.attrs.size()))
                .limit(10)
                .collect(Collectors.toList());

        for (int i = 0; i < biggestFiles.size(); i++) {
            int index = i + 1;
            PathWithAttrs path = biggestFiles.get(i);
            String size = path.prettySize();

            System.out.printf("%2d. %10s| %s\n", index, size, root.relativize(path.path));
        }
    }

    private static List<PathWithAttrs> listSubtree(Path root) throws IOException {
        final List<PathWithAttrs> list = new ArrayList<>();

        Files.walkFileTree(root, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                list.add(new PathWithAttrs(dir, attrs));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                list.add(new PathWithAttrs(file, attrs));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                System.err.println("Failed to visit file: " + file);
                return FileVisitResult.SKIP_SUBTREE;
            }
        });

        return list;
    }

    private static String formatBytes(long bytes) {
        return String.format("%d Kb", bytes / 1024);
    }

    private static String formatPercent(long size, long total) {
        double p = (double) size / total * 100;
        return String.format("%.02f%%", p);
    }
}
