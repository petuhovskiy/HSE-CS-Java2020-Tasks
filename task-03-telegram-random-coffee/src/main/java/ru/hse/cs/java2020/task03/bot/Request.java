package ru.hse.cs.java2020.task03.bot;

import ru.hse.cs.java2020.task03.bot.utils.Inline;
import ru.hse.cs.java2020.task03.state.models.State;
import ru.hse.cs.java2020.task03.tracker.Client;

public interface Request {
    State getState();
    String getText();
    Client getClient();
    String getCallbackData();
    Inline getInline();
}
