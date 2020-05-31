package ru.hse.cs.java2020.task03.controller;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import ru.hse.cs.java2020.task03.bot.Request;
import ru.hse.cs.java2020.task03.bot.Response;
import ru.hse.cs.java2020.task03.bot.auto.BotController;
import ru.hse.cs.java2020.task03.bot.auto.BotRequestMapping;
import ru.hse.cs.java2020.task03.state.models.MenuKey;
import ru.hse.cs.java2020.task03.tracker.models.CreateIssue;
import ru.hse.cs.java2020.task03.tracker.models.Issue;
import ru.hse.cs.java2020.task03.tracker.models.QueueLink;
import ru.hse.cs.java2020.task03.tracker.models.User;

import java.util.stream.Stream;

import static ru.hse.cs.java2020.task03.bot.utils.Format.errorMessage;
import static ru.hse.cs.java2020.task03.bot.utils.Format.lines;

@BotController
public class CreateTaskController {
    private static final String KEY_BACK = "↩️ Назад";
    private static final String KEY_CANCEL = "❌ Отмена";
    private static final String KEY_YES = "Да";
    private static final String KEY_NO = "Нет";

    public static void start(Request req, Response resp) {
        askQueue(req, resp);
    }

    public static void askQueue(Request req, Response resp) {
        var state = req.getState();
        var client = req.getClient();
        state.setKey(MenuKey.CREATE_TASK_QUEUE);
        state.setCreateIssue(new CreateIssue());

        try {
            var response = client.queues().execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("Unsuccessful request. Response=" + response.errorBody().string());
            }
            var queues = response.body();
            String[][] buttons = Stream.concat(
                    queues.stream()
                            .map(it -> it.getId() + ". " + it.getName()),
                    Stream.of(KEY_CANCEL)
            )
                    .map(it -> new String[]{it})
                    .toArray(String[][]::new);

            resp.sendKeyboard("Выберите очередь:", new ReplyKeyboardMarkup(buttons));
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendText(errorMessage(e));
            MainController.goToMainMenu(req, resp);
        }
    }

    @BotRequestMapping(key = MenuKey.CREATE_TASK_QUEUE)
    public void handleQueue(Request req, Response resp) {
        var text = req.getText();
        switch (text) {
            case KEY_BACK:
            case KEY_CANCEL:
                MainController.goToMainMenu(req, resp);
                return;
        }

        var pos = text.indexOf('.');
        if (pos != -1) {
            text = text.substring(0, pos);
        }

        try {
            Integer.valueOf(text);
        } catch (NumberFormatException e) {
            resp.sendText(errorMessage(e));
            askQueue(req, resp);
            return;
        }

        req.getState().getCreateIssue().setQueue(new QueueLink(text));
        askName(req, resp);
    }

    public static void askName(Request req, Response resp) {
        var state = req.getState();
        state.setKey(MenuKey.CREATE_TASK_NAME);
        resp.sendKeyboard("Введите имя задачи:", backKeyboard());
    }

    @BotRequestMapping(key = MenuKey.CREATE_TASK_NAME)
    public void handleName(Request req, Response resp) {
        var text = req.getText();
        switch (text) {
            case KEY_BACK:
                askQueue(req, resp);
                return;
            case KEY_CANCEL:
                MainController.goToMainMenu(req, resp);
                return;
        }

        req.getState().getCreateIssue().setSummary(text);
        askDescription(req, resp);
    }

    public static void askDescription(Request req, Response resp) {
        var state = req.getState();
        state.setKey(MenuKey.CREATE_TASK_DESCRIPTION);
        resp.sendKeyboard("Введите описание задачи:", backKeyboard());
    }

    @BotRequestMapping(key = MenuKey.CREATE_TASK_DESCRIPTION)
    public void handleDescription(Request req, Response resp) {
        var text = req.getText();
        switch (text) {
            case KEY_BACK:
                askName(req, resp);
                return;
            case KEY_CANCEL:
                MainController.goToMainMenu(req, resp);
                return;
        }

        req.getState().getCreateIssue().setDescription(text);
        askAssignee(req, resp);
    }

    public static void askAssignee(Request req, Response resp) {
        var state = req.getState();
        state.setKey(MenuKey.CREATE_TASK_ASSIGNEE);

        String[][] buttons = new String[][]{
            {KEY_YES, KEY_NO},
            {KEY_BACK, KEY_CANCEL},
        };

        resp.sendKeyboard("Хотите назначить задачу на себя?", new ReplyKeyboardMarkup(buttons));
    }

    @BotRequestMapping(key = MenuKey.CREATE_TASK_ASSIGNEE)
    public void handleAssignee(Request req, Response resp) {
        var text = req.getText();
        switch (text) {
            case KEY_BACK:
                askDescription(req, resp);
                return;
            case KEY_CANCEL:
                MainController.goToMainMenu(req, resp);
                return;
            case KEY_YES:
                req.getState().getCreateIssue().setAssignee(
                        new User(req.getState().getMyself().getUid().toString())
                );
                break;
            case KEY_NO:
                req.getState().getCreateIssue().setAssignee(null);
                break;
            default:
                askAssignee(req, resp);
        }

        createIssue(req, resp);
    }

    public void createIssue(Request req, Response resp) {
        try {
            var response = req.getClient().createIssue(req.getState().getCreateIssue()).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("Unsuccessful request. Response=" + response.errorBody().string());
            }
            Issue issue = response.body();
            resp.sendText(lines(
                    "Задача успешно создана.",
                    "Посмотреть можно по ссылке:",
                    String.format("https://tracker.yandex.ru/%s", issue.getKey())
            ));

            MainController.goToMainMenu(req, resp);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendText(errorMessage(e));
        }

        askAssignee(req, resp);
    }

    public static Keyboard backKeyboard() {
        return new ReplyKeyboardMarkup(
                new String[]{KEY_BACK, KEY_CANCEL}
        );
    }
}
