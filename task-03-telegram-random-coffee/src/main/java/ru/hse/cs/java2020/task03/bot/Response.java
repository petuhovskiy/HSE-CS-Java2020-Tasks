package ru.hse.cs.java2020.task03.bot;

import com.pengrad.telegrambot.model.request.Keyboard;

public interface Response {
    void sendText(String text);
    void sendKeyboard(String text, Keyboard keyboard);
}
