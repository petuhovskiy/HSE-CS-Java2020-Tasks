
package ru.hse.cs.java2020.task03.tracker.models;


public class User {
    private String self;
    private String id;
    private String display;

    public User() {
    }

    public User(String id) {
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

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    @Override
    public String toString() {
        return "User{" +
                "self='" + self + '\'' +
                ", id='" + id + '\'' +
                ", display='" + display + '\'' +
                '}';
    }
}