package ru.hse.cs.java2020.task03.tracker.models;

import java.util.Map;

public class SearchIssue {
    private Map<String, String> filter;

    public SearchIssue(Map<String, String> filter) {
        this.filter = filter;
    }

    public Map<String, String> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, String> filter) {
        this.filter = filter;
    }
}
