package ru.hse.cs.java2020.task03.controller;

import ru.hse.cs.java2020.task03.bot.RequestMock;
import ru.hse.cs.java2020.task03.state.models.State;

public final class ControllerTest {
    private ControllerTest() {
    }

    public static RequestMock messageOf(State state, String text) {
        RequestMock req = new RequestMock();
        req.setText(text);
        req.setState(state);
        return req;
    }
}
