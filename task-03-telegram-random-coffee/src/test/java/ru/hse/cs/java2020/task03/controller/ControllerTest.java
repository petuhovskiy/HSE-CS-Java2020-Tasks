package ru.hse.cs.java2020.task03.controller;

import com.pengrad.telegrambot.model.Message;
import ru.hse.cs.java2020.task03.bot.Request;
import ru.hse.cs.java2020.task03.state.models.State;

import java.lang.reflect.Field;

public final class ControllerTest {
    private ControllerTest() {
    }

    public static Request messageOf(State state, String text) {
        Message message = new Message();
        try {
            Field f = message.getClass().getDeclaredField("text");
            f.setAccessible(true);
            f.set(message, text);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
        return new Request(message, state);
    }
}
