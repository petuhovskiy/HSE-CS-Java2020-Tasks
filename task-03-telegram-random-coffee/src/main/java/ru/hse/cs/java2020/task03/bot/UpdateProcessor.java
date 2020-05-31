package ru.hse.cs.java2020.task03.bot;

import com.pengrad.telegrambot.model.Update;

public interface UpdateProcessor {
    void processUpdate(Update update);
}
