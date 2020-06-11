package ru.hse.cs.java2020.task03.state;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.hse.cs.java2020.task03.state.models.State;

public interface StateRepository extends MongoRepository<State, ObjectId> {
    State findByChatId(long chatId);
}
