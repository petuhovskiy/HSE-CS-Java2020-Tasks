package ru.hse.cs.java2020.task03.tracker.models;

public class Comment {
    private String self;
    private Integer id;
    private String longId;
    private String text;
    private User createdBy;
    private User updatedBy;
    private String createdAt;
    private String updatedAt;
    private Integer version;
    private String type;
    private String transport;

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLongId() {
        return longId;
    }

    public void setLongId(String longId) {
        this.longId = longId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "self='" + self + '\'' +
                ", id=" + id +
                ", longId='" + longId + '\'' +
                ", text='" + text + '\'' +
                ", createdBy=" + createdBy +
                ", updatedBy=" + updatedBy +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", version=" + version +
                ", type='" + type + '\'' +
                ", transport='" + transport + '\'' +
                '}';
    }
}