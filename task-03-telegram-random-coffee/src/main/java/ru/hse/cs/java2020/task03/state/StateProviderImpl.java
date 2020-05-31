package ru.hse.cs.java2020.task03.state;

import org.springframework.stereotype.Component;
import ru.hse.cs.java2020.task03.state.models.MenuKey;
import ru.hse.cs.java2020.task03.state.models.State;

@Component
public class StateProviderImpl implements StateProvider {
    private final StateRepository repo;

    public StateProviderImpl(StateRepository repo) {
        this.repo = repo;
    }

    @Override
    public State fetchStateByChatId(long chatId) {
        State state = repo.findByChatId(chatId);
        if (state == null) {
            state = new State(chatId);
        }
        if (state.getKey() == null) {
            state.setKey(MenuKey.UNAUTHORIZED);
        }
        return state;
    }

    @Override
    public void saveState(State state) {
        repo.save(state);
    }
}
