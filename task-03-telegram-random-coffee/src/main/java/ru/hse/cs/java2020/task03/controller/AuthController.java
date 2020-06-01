package ru.hse.cs.java2020.task03.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.HtmlUtils;
import ru.hse.cs.java2020.task03.bot.Request;
import ru.hse.cs.java2020.task03.bot.Response;
import ru.hse.cs.java2020.task03.bot.auto.BotController;
import ru.hse.cs.java2020.task03.bot.auto.BotRequestMapping;
import ru.hse.cs.java2020.task03.state.models.MenuKey;

import static ru.hse.cs.java2020.task03.bot.utils.Format.lines;
import static ru.hse.cs.java2020.task03.bot.utils.Format.noText;

@BotController
public class AuthController {
    private final String clientId;

    public AuthController(@Value("${ya.oauth.clientId}") String clientId) {
        this.clientId = clientId;
    }

    public void goToUnauthorized(Request req, Response resp) {
        req.getState().setKey(MenuKey.UNAUTHORIZED);
        req.getState().setAccessToken("");
        req.getState().setOrgId("");
        req.getState().setMyself(null);
        resp.sendText(
                lines(
                        "Для начала использования бота нужно выполнить несколько шагов:",
                        "",
                        String.format(
                                "1. Перейдите <a href=\"%s\">по ссылке</a>",
                                "https://oauth.yandex.ru/authorize?response_type=token&client_id=" + clientId
                        ),
                        "2. Примите условия и скопируйте токен",
                        "3. Пришлите токен отдельным сообщением"
                )
        );
    }

    @BotRequestMapping(key = MenuKey.UNAUTHORIZED)
    public void handleAuthToken(Request req, Response resp) {
        var text = req.getText();
        if (noText(text) || text.startsWith("/")) {
            goToUnauthorized(req, resp);
            return;
        }

        req.getState().setAccessToken(text.trim());
        goToAwaitOrgId(req, resp);
    }

    public void goToAwaitOrgId(Request req, Response resp) {
        req.getState().setKey(MenuKey.AWAIT_ORG_ID);
        resp.sendText(
                String.format(
                        "Теперь перейдите <a href=\"%s\">по ссылке</a>",
                        "https://tracker.yandex.ru/settings"
                ) + ", скопируйте 'ID организации для API' и пришлите отдельным сообщением"
        );
    }

    @BotRequestMapping(key = MenuKey.AWAIT_ORG_ID)
    public void handleOrgId(Request req, Response resp) {
        var text = req.getText();
        if (noText(text)) {
            goToAwaitOrgId(req, resp);
            return;
        }

        req.getState().setOrgId(text);
        var client = req.getClient();

        try {
            var myself = client.myself().execute();
            if (!myself.isSuccessful()) {
                throw new RuntimeException("Unsuccessful request. Response=" + myself.errorBody().string());
            }
            req.getState().setMyself(myself.body());

            MainController.goToMainMenu(req, resp);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendText(lines("Данные для авторизации не подходят или произошла ошибка.", "Детали:", HtmlUtils.htmlEscape(e.getMessage())));
        }

        goToUnauthorized(req, resp);
    }
}
