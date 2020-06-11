package ru.hse.cs.java2020.task03.bot;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ResponseMock implements Response {
    public static final int CHAT_ID = 0;

    private List<SendMessage> messages;

    public ResponseMock() {
        this.messages = new ArrayList<>();
    }

    @Override
    public void sendText(String text) {
        messages.add(
                new SendMessage(CHAT_ID, text)
                        .parseMode(ParseMode.HTML)
        );
    }

    @Override
    public void sendKeyboard(String text, Keyboard keyboard) {
        if (keyboard instanceof ReplyKeyboardMarkup) {
            ((ReplyKeyboardMarkup) keyboard).resizeKeyboard(true);
        }

        messages.add(
                new SendMessage(CHAT_ID, text)
                        .parseMode(ParseMode.HTML)
                        .replyMarkup(keyboard)
        );
    }

    public List<SendMessage> getMessages() {
        return messages;
    }

    public void assertText(String text, int pos) {
        assertEquals(text, messages.get(pos).getParameters().get("text"));
    }

    public void assertText(String text) {
        assertEquals(1, messages.size());
        assertText(text, 0);
    }
}
