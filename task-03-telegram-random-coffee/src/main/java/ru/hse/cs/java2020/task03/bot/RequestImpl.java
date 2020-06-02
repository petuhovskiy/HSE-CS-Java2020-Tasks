package ru.hse.cs.java2020.task03.bot;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import ru.hse.cs.java2020.task03.bot.utils.Inline;
import ru.hse.cs.java2020.task03.state.models.State;
import ru.hse.cs.java2020.task03.tracker.Client;
import ru.hse.cs.java2020.task03.tracker.ClientFactory;

public class RequestImpl implements Request {
    private final Message message;
    private final CallbackQuery callback;
    private final State state;

    public RequestImpl(Message message, State state) {
        this.message = message;
        this.callback = null;
        this.state = state;
    }

    public RequestImpl(CallbackQuery callback, State state) {
        this.message = null;
        this.callback = callback;
        this.state = state;
    }

    public State getState() {
        return state;
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

    @Override
    public String getCallbackData() {
        return callback.data();
    }

    public Inline getInline() {
        return Inline.parseInline(getCallbackData());
    }
}
