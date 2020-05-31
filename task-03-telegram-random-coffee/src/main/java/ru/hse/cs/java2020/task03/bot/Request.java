package ru.hse.cs.java2020.task03.bot;

import com.pengrad.telegrambot.model.Message;
import ru.hse.cs.java2020.task03.state.models.State;
import ru.hse.cs.java2020.task03.tracker.Client;
import ru.hse.cs.java2020.task03.tracker.ClientFactory;

public class Request {
    private final Message message;
    private final State state;

    public Request(Message message, State state) {
        this.message = message;
        this.state = state;
    }

    public Message getMessage() {
        return message;
    }

    public State getState() {
        return state;
    }

    public long getChatId() {
        return state.getChatId();
    }

    public String getText() {
        if (message == null) {
            return null;
        }
        return message.text();
    }

    public Client getClient() {
        return new ClientFactory().buildClient(getState().getAccessToken(), getState().getOrgId());
    }
}
