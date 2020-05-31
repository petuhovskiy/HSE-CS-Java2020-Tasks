package ru.hse.cs.java2020.task03.bot;

public interface MessageHandler {
    void handleMessage(Request req, Response resp);
}
