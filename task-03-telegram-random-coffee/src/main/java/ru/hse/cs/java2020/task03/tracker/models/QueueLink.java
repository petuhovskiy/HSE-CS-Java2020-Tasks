package ru.hse.cs.java2020.task03.tracker.models;

public class QueueLink {
    private String self;
    private String id;
    private String key;
    private String display;

    public QueueLink() {
    }

    public QueueLink(String id) {
        this.id = id;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    @Override
    public String toString() {
        return "QueueLink{" +
                "self='" + self + '\'' +
                ", id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", display='" + display + '\'' +
                '}';
    }
}