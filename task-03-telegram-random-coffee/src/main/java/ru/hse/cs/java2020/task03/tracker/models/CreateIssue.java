package ru.hse.cs.java2020.task03.tracker.models;

public class CreateIssue {
    private String summary;
    private QueueLink queue;
    private String description;
    private User assignee;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public QueueLink getQueue() {
        return queue;
    }

    public void setQueue(QueueLink queue) {
        this.queue = queue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }
}
