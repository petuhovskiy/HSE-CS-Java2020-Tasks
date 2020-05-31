package ru.hse.cs.java2020.task03.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.springframework.stereotype.Component;
import ru.hse.cs.java2020.task03.state.StateProvider;
import ru.hse.cs.java2020.task03.state.models.State;

@Component
public class UpdateProcessorImpl implements UpdateProcessor {
    private final TelegramBot bot;
    private final StateProvider stateProvider;
    private final MessageHandler messageHandler;

    public UpdateProcessorImpl(TelegramBot bot, StateProvider stateProvider, MessageHandler messageHandler) {
        this.bot = bot;
        this.stateProvider = stateProvider;
        this.messageHandler = messageHandler;
    }

    @Override
    public void processUpdate(Update update) {
        Message message = update.message();
        if (message == null) {
            return;
        }
        if (message.chat().type() != Chat.Type.Private) {
            return;
        }

        long chatId = message.chat().id();
        State state = stateProvider.fetchStateByChatId(chatId);

        handleMessage(chatId, state, message);

        stateProvider.saveState(state);
    }

    public void handleMessage(long chatId, State state, Message message) {
        final Request request = new Request(message, state);
        final Response response = new Response(bot, chatId);

        messageHandler.handleMessage(request, response);
    }
}
