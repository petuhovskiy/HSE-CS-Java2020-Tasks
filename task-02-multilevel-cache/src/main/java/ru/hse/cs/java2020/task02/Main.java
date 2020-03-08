package ru.hse.cs.java2020.task02;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        final int argsCount = 4;
        final int cacheDirPos = 0;
        final int memorySizePos = 1;
        final int diskSizePos = 2;
        final int policyPos = 3;

        if (args.length != argsCount) {
            System.err.println("Usage: ./cache /path/to/cache/dir <memorySize> <diskSize> <policy>");
            return;
        }

        String cacheDir = args[cacheDirPos];
        long memorySize = Long.parseLong(args[memorySizePos]);
        long diskSize = Long.parseLong(args[diskSizePos]);

        EvictionPolicy<Long> policy;
        switch (args[policyPos]) {
            case "LRU":
                policy = new LRUPolicy.Builder<>();
                break;
            case "LFU":
                policy = new LFUPolicy.Builder<>();
                break;
            default:
                System.err.println("Only LFU and LRU policies are supported!");
                return;
        }

        try (CacheImpl cache = new CacheImpl(memorySize, diskSize, cacheDir, policy)) {
            String action;
            Scanner scanner = new Scanner(System.in);
            while (true) {
                action = scanner.next();

                switch (action) {
                    case "exit":
                        return;

                    case "put":
                        Long key = scanner.nextLong();
                        String value = scanner.nextLine();
                        System.out.printf("Previous value: %s\n", cache.put(key, value));
                        break;

                    case "get":
                        key = scanner.nextLong();
                        System.out.printf("Get %d = %s\n", key, cache.get(key));
                        break;

                    default:
                        System.err.println("Unknown operation, please enter one of [exit, put, get]");
                }
            }
        }
    }
}
