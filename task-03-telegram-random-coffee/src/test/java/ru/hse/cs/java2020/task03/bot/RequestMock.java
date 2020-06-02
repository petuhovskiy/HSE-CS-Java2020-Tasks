package ru.hse.cs.java2020.task03.bot;

import ru.hse.cs.java2020.task03.bot.utils.Inline;
import ru.hse.cs.java2020.task03.state.models.State;
import ru.hse.cs.java2020.task03.tracker.Client;

public class RequestMock implements Request {
    private State state;
    private String text;
    private Client client;
    private String callbackData;

    public RequestMock() {
        this.state = new State();
    }

    @Override
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String getCallbackData() {
        return callbackData;
    }

    public void setCallbackData(String callbackData) {
        this.callbackData = callbackData;
    }

    @Override
    public Inline getInline() {
        return Inline.parseInline(getCallbackData());
    }
}
