package ru.hse.cs.java2020.task03.controller;

import org.springframework.beans.factory.annotation.Value;
import ru.hse.cs.java2020.task03.bot.Request;
import ru.hse.cs.java2020.task03.bot.Response;
import ru.hse.cs.java2020.task03.bot.auto.BotController;
import ru.hse.cs.java2020.task03.bot.auto.BotRequestMapping;
import ru.hse.cs.java2020.task03.state.models.MenuKey;

@BotController
public class AuthController {
    private final String clientId;

    public AuthController(@Value("${ya.oauth.clientId}") String clientId) {
        this.clientId = clientId;
    }

    @BotRequestMapping(key = MenuKey.UNAUTHORIZED)
    public void handleAuth(Request req, Response resp) {
        var text = req.getText();

        resp.sendText(
                "Для начала использования бота нужно выполнить несколько шагов:\n"
                .concat("\n")
                .concat(String.format(
                        "1. Перейдите <a href=\"%s\">по ссылке</a>\n",
                        "https://oauth.yandex.ru/authorize?response_type=token&client_id=" + clientId
                ))
                .concat("2. Примите условия и скопируйте токен\n")
                .concat("3. Пришлите токен отдельным сообщением")
        );
    }
}
