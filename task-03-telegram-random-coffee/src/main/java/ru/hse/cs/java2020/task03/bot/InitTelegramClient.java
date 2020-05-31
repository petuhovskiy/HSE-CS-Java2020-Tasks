package ru.hse.cs.java2020.task03.bot;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitTelegramClient {
    @Bean
    TelegramBot createBot(@Value("${telegram.bot.token}") String token) {
        return new TelegramBot.Builder(token)
                .debug()
                .build();
    }
}
