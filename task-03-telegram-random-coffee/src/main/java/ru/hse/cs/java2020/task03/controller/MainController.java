package ru.hse.cs.java2020.task03.controller;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import ru.hse.cs.java2020.task03.bot.Request;
import ru.hse.cs.java2020.task03.bot.Response;
import ru.hse.cs.java2020.task03.bot.auto.BotController;
import ru.hse.cs.java2020.task03.bot.auto.BotRequestMapping;
import ru.hse.cs.java2020.task03.state.models.MenuKey;

import static ru.hse.cs.java2020.task03.bot.utils.Format.lines;

@BotController
public class MainController {
    private static final String KEY_LOGOUT = "\uD83D\uDEAA Выйти";
    private static final String KEY_SPECIAL = "\uD83C\uDFB1";

    private final AuthController authController;

    public MainController(AuthController authController) {
        this.authController = authController;
    }

    public static void goToMainMenu(Request req, Response resp) {
        var state = req.getState();
        state.setKey(MenuKey.MAIN_MENU);

        String fullName = String.format(
            "%s %s aka %s",
            state.getMyself().getFirstName(),
            state.getMyself().getLastName(),
            state.getMyself().getLogin()
        );

        resp.sendKeyboard(
                lines(
                        String.format("Привет, %s", fullName),
                        "",
                        "Выбери нужный пункт меню:"
                ),
                menuKeyboard()
        );
    }

    public static Keyboard menuKeyboard() {
        return new ReplyKeyboardMarkup(
                new String[]{KEY_LOGOUT}
        );
    }

    @BotRequestMapping(key = MenuKey.MAIN_MENU)
    public void handleMainMenu(Request req, Response resp) {
        switch (req.getText()) {
            case KEY_LOGOUT:
                resp.sendKeyboard("До свидания!", new ReplyKeyboardRemove());
                authController.goToUnauthorized(req, resp);
                return;
            case KEY_SPECIAL:
                resp.sendText(lines("Nothing here...", "Just a horse"));
                return;
            default:
                resp.sendText("Похоже ты ошибся. Я не знаю такой команды.");
        }

        goToMainMenu(req, resp);
    }
}
