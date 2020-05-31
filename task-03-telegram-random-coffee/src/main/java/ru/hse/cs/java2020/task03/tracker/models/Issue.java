
package ru.hse.cs.java2020.task03.tracker.models;


public class Issue {
    private String self;
    private String id;
    private String key;
    private Integer version;
    private String summary;
    private String statusStartTime;
    private User updatedBy;
    private String description;
    private Type type;
    private Priority priority;
    private String createdAt;
    private User createdBy;
    private Integer commentWithoutExternalMessageCount;
    private Integer votes;
    private Integer commentWithExternalMessageCount;
    private QueueLink queue;
    private String updatedAt;
    private Status status;
    private Boolean favorite;

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStatusStartTime() {
        return statusStartTime;
    }

    public void setStatusStartTime(String statusStartTime) {
        this.statusStartTime = statusStartTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCommentWithoutExternalMessageCount() {
        return commentWithoutExternalMessageCount;
    }

    public void setCommentWithoutExternalMessageCount(Integer commentWithoutExternalMessageCount) {
        this.commentWithoutExternalMessageCount = commentWithoutExternalMessageCount;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public Integer getCommentWithExternalMessageCount() {
        return commentWithExternalMessageCount;
    }

    public void setCommentWithExternalMessageCount(Integer commentWithExternalMessageCount) {
        this.commentWithExternalMessageCount = commentWithExternalMessageCount;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public User getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public QueueLink getQueue() {
        return queue;
    }

    public void setQueue(QueueLink queue) {
        this.queue = queue;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "self='" + self + '\'' +
                ", id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", version=" + version +
                ", summary='" + summary + '\'' +
                ", statusStartTime='" + statusStartTime + '\'' +
                ", updatedBy=" + updatedBy +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", priority=" + priority +
                ", createdAt='" + createdAt + '\'' +
                ", createdBy=" + createdBy +
                ", commentWithoutExternalMessageCount=" + commentWithoutExternalMessageCount +
                ", votes=" + votes +
                ", commentWithExternalMessageCount=" + commentWithExternalMessageCount +
                ", queue=" + queue +
                ", updatedAt='" + updatedAt + '\'' +
                ", status=" + status +
                ", favorite=" + favorite +
                '}';
    }
}