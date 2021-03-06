package ru.hse.cs.java2020.task03.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;

public class ResponseImpl implements Response {
    private final TelegramBot bot;
    private final long chatId;

    public ResponseImpl(TelegramBot bot, long chatId) {
        this.bot = bot;
        this.chatId = chatId;
    }

    public void sendText(String text) {
        this.bot.execute(
                new SendMessage(chatId, text)
                        .parseMode(ParseMode.HTML)
        );
    }

    public void sendKeyboard(String text, Keyboard keyboard) {
        if (keyboard instanceof ReplyKeyboardMarkup) {
            ((ReplyKeyboardMarkup) keyboard).resizeKeyboard(true);
        }

        this.bot.execute(
                new SendMessage(chatId, text)
                        .parseMode(ParseMode.HTML)
                        .replyMarkup(keyboard)
        );
    }
}
