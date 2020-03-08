package ru.hse.cs.java2020.task02;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/* DiskCache stores data on disk as entries, which go one by one.
 * Entry contains of:
 * - long key (8 bytes)
 * - long size (8 bytes)
 * - String content (size bytes)
 */
public class DiskCache implements Cache<Long, String>, AutoCloseable {
    // entry head contains of two longs: key, size.
    private static final int HEAD_SIZE = 8 * 2;

    // charset is used for serialization/deserialization
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    // after compact operation, file size will be no bigger than COMPACT_FACTOR * maxFileSize
    private static final double COMPACT_FACTOR = 0.5;

    static class DataEntry {
        // offset specifies location in the file
        private long offset;

        // data key
        private long key;

        // size denotes data size in bytes, negative size means removed entry
        private long size;

        // data value
        private String data;

        DataEntry(long offset, long key, long size) {
            this.offset = offset;
            this.key = key;
            this.size = size;
        }

        DataEntry(long offset, long key, long size, String data) {
            this.offset = offset;
            this.key = key;
            this.size = size;
            this.data = data;
        }

        long getRealSize() {
            if (this.size == Long.MIN_VALUE) {
                return 0; // hack
            }
            return Math.abs(this.size);
        }

        boolean isRemoved() {
            return this.size < 0;
        }
    }

    static class Entry {
        private long offset;
        private long size;

        Entry(long offset, long size) {
            this.offset = offset;
            this.size = size;
        }
    }

    // all entries which are stored in file at the moment
    private final Map<Long, Entry> entries = new HashMap<>();

    private final SeekableByteChannel channel;
    private final long maxFileSize;
    private final EvictionResolver<Long> policy;

    public DiskCache(Path file, long maxSize, EvictionPolicy<Long> policy) throws IOException {
        this.channel = Files.newByteChannel(
                file,
                StandardOpenOption.CREATE,
                StandardOpenOption.READ,
                StandardOpenOption.WRITE
        );
        this.maxFileSize = maxSize;
        this.policy = policy.buildResolver();

        this.readAllData();
    }

    private void readAllData() throws IOException {
        DataEntry entry;
        while ((entry = readEntryWithoutData()) != null) {
            this.putToMemory(entry);
        }
    }

    private boolean readAll(ByteBuffer buf) throws IOException {
        do {
            if (this.channel.read(buf) <= 0) {
                break;
            }
        } while (buf.hasRemaining());
        return !buf.hasRemaining();
    }

    private boolean writeAll(ByteBuffer buf) throws IOException {
        do {
            if (this.channel.write(buf) <= 0) {
                break;
            }
        } while (buf.hasRemaining());
        return !buf.hasRemaining();
    }

    private DataEntry readEntryHead() throws IOException {
        long offset = this.channel.position();

        ByteBuffer buf = ByteBuffer.allocate(HEAD_SIZE);
        if (!this.readAll(buf)) {
            return null;
        }

        buf.rewind();
        long key = buf.getLong();
        long size = buf.getLong();
        return new DataEntry(offset, key, size);
    }

    private DataEntry readEntryWithoutData() throws IOException {
        DataEntry entry = readEntryHead();
        if (entry == null) {
            return null;
        }
        this.channel.position(this.channel.position() + entry.getRealSize());
        return entry;
    }

    private DataEntry readEntryWithData() throws IOException {
        DataEntry entry = readEntryHead();
        if (entry == null) {
            return null;
        }

        ByteBuffer buf = ByteBuffer.allocate((int) entry.getRealSize());
        if (!this.readAll(buf)) {
            return null;
        }

        entry.data = new String(buf.array(), CHARSET);
        return entry;
    }

    public void remove(Long key) throws IOException {
        Entry entry = this.entries.get(key);
        if (entry == null) {
            return;
        }

        long removedSize = -entry.size;
        if (removedSize == 0) {
            removedSize = Long.MIN_VALUE; // hack, because 0 is not negative (so sad)
        }

        ByteBuffer bb = ByteBuffer.allocate(HEAD_SIZE);
        bb.putLong(key);
        bb.putLong(removedSize);
        bb.rewind();

        this.channel.position(entry.offset);
        if (!this.writeAll(bb)) {
            throw new RuntimeException("failed to write updated entry head");
        }

        this.entries.remove(key);
        this.policy.notifyRemove(key);
    }

    private boolean writeToDisk(Long key, byte[] data) throws IOException {
        long size = data.length;

        ByteBuffer buf = ByteBuffer.allocate((int) (HEAD_SIZE + size));
        buf.putLong(key);
        buf.putLong(size);
        buf.put(data);
        buf.rewind();

        return this.writeAll(buf);
    }

    private DataEntry putToDisk(Long key, String value) throws IOException {
        byte[] data = value.getBytes(CHARSET);
        long size = data.length;

        long fileSize = this.channel.size();
        if (fileSize + HEAD_SIZE + size > maxFileSize) {
            // calculating size after compact
            long targetSize = (long) (maxFileSize * COMPACT_FACTOR);
            targetSize = Math.max(0, targetSize - HEAD_SIZE - size);

            if (targetSize + HEAD_SIZE + size > maxFileSize) {
                throw new RuntimeException("value is too large to be stored on disk");
            }

            this.compactDisk(targetSize);

            fileSize = this.channel.size();
            if (fileSize + HEAD_SIZE + size > maxFileSize) {
                throw new RuntimeException("value is too large to be stored on disk");
            }
        }

        this.channel.truncate(fileSize + HEAD_SIZE + size);
        this.channel.position(fileSize);

        if (!this.writeToDisk(key, data)) {
            throw new RuntimeException("failed to put entry on disk");
        }

        return new DataEntry(fileSize, key, size, value);
    }

    private void compactDisk(long targetSize) throws IOException {
        long totalSize = this.entries
                .values()
                .stream()
                .mapToLong(it -> it.size)
                .sum();

        while (totalSize > targetSize) {
            Long evictKey = this.policy.findEvict();
            if (evictKey == null) {
                throw new RuntimeException("unexpected null evictKey");
            }

            Entry entry = this.entries.get(evictKey);
            if (entry == null) {
                throw new RuntimeException("unexpected non found evictKey");
            }

            this.remove(evictKey);
            totalSize -= entry.size;
        }

        this.entries.keySet().forEach(this.policy::notifyRemove);
        this.entries.clear();

        long newPosition = 0;
        DataEntry entry;

        this.channel.position(0);
        while ((entry = readEntryWithData()) != null) {
            if (entry.isRemoved()) {
                continue;
            }

            // remember iteration position
            long pos = this.channel.position();

            // update offset
            entry.offset = newPosition;
            this.channel.position(newPosition);

            // write to disk and memory
            byte[] data = entry.data.getBytes(CHARSET);
            this.writeToDisk(entry.key, data);
            this.putToMemory(entry);

            // save new data offset
            newPosition = this.channel.position();

            // back to iteration position
            this.channel.position(pos);
        }

        // update file size
        this.channel.truncate(newPosition);
    }

    private void putToMemory(DataEntry d) {
        if (d.isRemoved()) {
            return;
        }

        this.entries.put(d.key, new Entry(d.offset, d.size));
        this.policy.notifyPut(d.key, d.size);
    }

    @Override
    public Optional<String> get(Long key) {
        Objects.requireNonNull(key);

        Entry entry = this.entries.get(key);
        if (entry == null) {
            return Optional.empty();
        }

        this.policy.notifyGet(key);

        try {
            this.channel.position(entry.offset);
            DataEntry dataEntry = this.readEntryWithData();
            if (dataEntry == null) {
                return Optional.empty();
            }
            return Optional.of(dataEntry.data);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<String> put(Long key, String value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);

        try {
            Optional<String> old = get(key);
            if (this.entries.containsKey(key)) {
                this.remove(key);
            }

            DataEntry entry = this.putToDisk(key, value);
            this.putToMemory(entry);

            return old;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        this.channel.close();
    }
}
