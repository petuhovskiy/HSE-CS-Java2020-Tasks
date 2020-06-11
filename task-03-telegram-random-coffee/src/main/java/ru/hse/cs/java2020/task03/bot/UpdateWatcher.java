package ru.hse.cs.java2020.task03.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

@Component
public class UpdateWatcher implements UpdatesListener {
    private final TelegramBot bot;
    private final UpdateProcessor updateProcessor;

    public UpdateWatcher(TelegramBot bot, UpdateProcessor updateProcessor) {
        this.bot = bot;
        this.updateProcessor = updateProcessor;
    }

    @PostConstruct
    public void start() {
        bot.setUpdatesListener(this);
    }

    @PreDestroy
    public void stop() {
        bot.removeGetUpdatesListener();
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(updateProcessor::processUpdate);
        return CONFIRMED_UPDATES_ALL;
    }
}
