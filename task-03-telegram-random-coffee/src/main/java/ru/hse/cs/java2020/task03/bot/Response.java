package ru.hse.cs.java2020.task03.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

public class Response {
    private final TelegramBot bot;
    private final long chatId;

    public Response(TelegramBot bot, long chatId) {
        this.bot = bot;
        this.chatId = chatId;
    }

    public TelegramBot getBot() {
        return bot;
    }

    public long getChatId() {
        return chatId;
    }

    public void sendText(String text) {
        this.bot.execute(
                new SendMessage(chatId, text).parseMode(ParseMode.HTML)
        );
    }
}
