package ru.hse.cs.java2020.task01;

import java.time.Duration;
import java.time.Instant;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: ./gradlew :task-01:run --args 'path/to/folder'");
            return;
        }

        Instant start = Instant.now();

        try {
            TotalDiskUsage.printDiskUsage(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Instant end = Instant.now();
        Duration totalTime = Duration.between(start, end);

        System.out.println();
        System.out.printf("Total time: %d.%03d s\n", totalTime.toSeconds(), totalTime.toMillisPart());
    }
}
