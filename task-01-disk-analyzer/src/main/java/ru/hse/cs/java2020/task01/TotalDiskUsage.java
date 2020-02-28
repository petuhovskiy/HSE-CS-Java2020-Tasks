package ru.hse.cs.java2020.task01;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

final class TotalDiskUsage {
    private TotalDiskUsage() {
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
            .filter(it -> !it.getPath().equals(root))
            .map(it -> new PathWithAttrs(root.relativize(it.getPath()), it.getAttrs()))
            .filter(it -> it.getPath().getNameCount() >= 1)
            .collect(Collectors.groupingBy(it -> it.getPath().getName(0)))
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
                        .filter(it -> it.getPath().equals(path))
                        .findAny();

                if (optRoot.isEmpty()) {
                    return;
                }

                DirStats stats = optStats.get().withRoot(optRoot.get());
                dirStats.add(stats);
            });

        dirStats.sort(Comparator.comparingLong(it -> -it.getTotalSize()));

        for (int i = 0; i < dirStats.size(); i++) {
            int index = i + 1;
            DirStats stats = dirStats.get(i);
            PathWithAttrs dir = stats.getRoot();
            String path = dir.getPath().toString();
            String prettySize = formatBytes(stats.getTotalSize());
            String percent = formatPercent(stats.getTotalSize(), totalSize);
            int items = stats.getCount() - 1;

            if (dir.getAttrs().isDirectory()) {
                path += "/";
            } else {
                items = -1;
            }

            System.out.printf("%2d. %35s| %10s| %7s|", index, path, prettySize, percent);
            if (items != -1) {
                System.out.printf("%6d items", items);
            }

            System.out.println();
        }
    }

    private static void printBiggestFiles(Path root, List<PathWithAttrs> paths) {
        System.out.println("----------- BIGGEST FILES -----------");

        final int topFilesCount = 10;
        List<PathWithAttrs> biggestFiles = paths
                .stream()
                .filter(it -> it.getAttrs().isRegularFile())
                .sorted(Comparator.comparingLong(it -> -it.getAttrs().size()))
                .limit(topFilesCount)
                .collect(Collectors.toList());

        for (int i = 0; i < biggestFiles.size(); i++) {
            int index = i + 1;
            PathWithAttrs path = biggestFiles.get(i);
            String size = path.prettySize();

            System.out.printf("%2d. %10s| %s\n", index, size, root.relativize(path.getPath()));
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

    static String formatBytes(long bytes) {
        final int bytesInKb = 1024;
        return String.format("%d Kb", bytes / bytesInKb);
    }

    private static String formatPercent(long size, long total) {
        final int totalPercent = 100;
        double p = (double) size / total * totalPercent;
        return String.format("%.02f%%", p);
    }
}
