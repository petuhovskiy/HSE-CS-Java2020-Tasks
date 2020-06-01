package ru.hse.cs.java2020.task03.controller;

import org.junit.Before;
import org.junit.Test;
import ru.hse.cs.java2020.task03.bot.ResponseMock;
import ru.hse.cs.java2020.task03.state.models.State;

import static org.junit.Assert.assertEquals;
import static ru.hse.cs.java2020.task03.bot.utils.Format.lines;
import static ru.hse.cs.java2020.task03.controller.ControllerTest.messageOf;

public class AuthControllerTest {
    private static final String CLIENT_ID = "12345";

    private AuthController controller;
    private ResponseMock response;
    private State state;

    @Before
    public void setUp() throws Exception {
        controller = new AuthController(CLIENT_ID);
        response = new ResponseMock();
        state = new State();
    }

    @Test
    public void goToUnauthorized() {
        controller.goToUnauthorized(messageOf(state, "/start"), response);
        response.assertText(lines(
                "Для начала использования бота нужно выполнить несколько шагов:",
                "",
                "1. Перейдите <a href=\"https://oauth.yandex.ru/authorize?response_type=token&client_id=12345\">по ссылке</a>",
                "2. Примите условия и скопируйте токен",
                "3. Пришлите токен отдельным сообщением"
        ));
    }

    @Test
    public void handleAuthToken() {
        controller.handleAuthToken(messageOf(state, "auth_token"), response);
        assertEquals("auth_token", state.getAccessToken());
    }
}
