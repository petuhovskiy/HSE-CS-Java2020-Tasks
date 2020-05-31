package ru.hse.cs.java2020.task03.state.models;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import ru.hse.cs.java2020.task03.tracker.models.CreateIssue;
import ru.hse.cs.java2020.task03.tracker.models.Myself;

@Document(collection = "state")
public class State {
    @MongoId
    private ObjectId id;

    @Indexed
    private Long chatId;

    private String key;

    private String accessToken;
    private String orgId;
    private Myself myself;

    private CreateIssue createIssue;

    public State() {
    }

    public State(long chatId) {
        this.chatId = chatId;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Myself getMyself() {
        return myself;
    }

    public void setMyself(Myself myself) {
        this.myself = myself;
    }

    public CreateIssue getCreateIssue() {
        return createIssue;
    }

    public void setCreateIssue(CreateIssue createIssue) {
        this.createIssue = createIssue;
    }
}
