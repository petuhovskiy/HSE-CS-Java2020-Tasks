package ru.hse.cs.java2020.task03.state;

import org.springframework.lang.NonNull;
import ru.hse.cs.java2020.task03.state.models.State;

public interface StateProvider {
    @NonNull
    State fetchStateByChatId(long chatId);

    void saveState(State state);
}
