package ru.hse.cs.java2020.task03.controller;

import org.junit.Before;
import org.junit.Test;
import ru.hse.cs.java2020.task03.bot.RequestMock;
import ru.hse.cs.java2020.task03.bot.ResponseMock;
import ru.hse.cs.java2020.task03.state.models.MenuKey;
import ru.hse.cs.java2020.task03.state.models.State;
import ru.hse.cs.java2020.task03.tracker.ClientMock;
import ru.hse.cs.java2020.task03.tracker.models.Myself;

import static org.junit.Assert.assertEquals;
import static ru.hse.cs.java2020.task03.bot.utils.Format.lines;
import static ru.hse.cs.java2020.task03.controller.ControllerTest.messageOf;

public class AuthControllerTest {
    private static final String CLIENT_ID = "12345";

    private AuthController controller;
    private ResponseMock resp;
    private State state;

    @Before
    public void setUp() throws Exception {
        controller = new AuthController(CLIENT_ID);
        resp = new ResponseMock();
        state = new State();
    }

    @Test
    public void goToUnauthorized() {
        state.setKey(MenuKey.MAIN_MENU);
        controller.goToUnauthorized(messageOf(state, "/start"), resp);
        resp.assertText(lines(
                "Для начала использования бота нужно выполнить несколько шагов:",
                "",
                "1. Перейдите <a href=\"https://oauth.yandex.ru/authorize?response_type=token&client_id=12345\">по ссылке</a>",
                "2. Примите условия и скопируйте токен",
                "3. Пришлите токен отдельным сообщением"
        ));
        assertEquals(state.getKey(), MenuKey.UNAUTHORIZED);
    }

    @Test
    public void handleAuthToken() {
        state.setKey(MenuKey.UNAUTHORIZED);
        controller.handleAuthToken(messageOf(state, "auth_token"), resp);
        assertEquals("auth_token", state.getAccessToken());
        assertEquals(state.getKey(), MenuKey.AWAIT_ORG_ID);
    }

    @Test
    public void handleOrgId() {
        state.setKey(MenuKey.AWAIT_ORG_ID);

        Myself myself = new Myself();
        myself.setLogin("mylogin");

        RequestMock req = messageOf(state, "org_id");
        req.setClient(ClientMock.create(myself));

        controller.handleOrgId(req, resp);
        assertEquals(MenuKey.MAIN_MENU, state.getKey());
        assertEquals(myself, state.getMyself());
        assertEquals(state.getOrgId(), "org_id");
    }
}
