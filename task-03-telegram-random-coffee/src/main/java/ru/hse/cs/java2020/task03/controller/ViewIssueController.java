package ru.hse.cs.java2020.task03.controller;

import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import ru.hse.cs.java2020.task03.bot.Request;
import ru.hse.cs.java2020.task03.bot.Response;
import ru.hse.cs.java2020.task03.bot.auto.BotController;
import ru.hse.cs.java2020.task03.bot.auto.BotRequestMapping;
import ru.hse.cs.java2020.task03.state.models.MenuKey;
import ru.hse.cs.java2020.task03.tracker.Client;
import ru.hse.cs.java2020.task03.tracker.models.Issue;
import ru.hse.cs.java2020.task03.tracker.models.User;

import java.util.stream.Collectors;

import static ru.hse.cs.java2020.task03.bot.utils.Format.errorMessage;
import static ru.hse.cs.java2020.task03.bot.utils.Format.lines;

@BotController
public class ViewIssueController {
    private static final String KEY_CANCEL = "❌ Отмена";

    public static void start(Request req, Response resp) {
        var state = req.getState();
        state.setKey(MenuKey.VIEW_TASK);
        resp.sendKeyboard("Введите ключ задачи:", new ReplyKeyboardMarkup(new String[][] {{KEY_CANCEL}}));
    }

    public static void viewIssue(Client client, String key, Response resp) {
        try {
            var response = client.issue(key).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("Unsuccessful request. Response=" + response.errorBody().string());
            }
            Issue issue = response.body();
            resp.sendKeyboard(
                    issueText(issue),
                    CommentsController.inlineKeyboard("Комментарии", key, 0)
            );
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendText(errorMessage(e));
        }
    }

    public static String issueText(Issue issue) {
        String followers = "Никого";
        if (issue.getFollowers() != null) {
            followers = issue
                    .getFollowers()
                    .stream()
                    .map(ViewIssueController::userText)
                    .collect(Collectors.joining(", "));
        }

        return lines(
                String.format("Задача <a href=\"https://tracker.yandex.ru/%s\">%s</a>", issue.getKey(), issue.getKey()),
                "",
                "<b>Название:</b> " + issue.getSummary(),
                "<b>Автор:</b> " + userText(issue.getCreatedBy()),
                "<b>Исполнитель:</b> " + userText(issue.getAssignee()),
                "<b>Наблюдатели:</b> " + followers,
                "<b>Комментариев:</b> " + issue.getCommentWithoutExternalMessageCount(),
                "",
                "<b>Описание:</b>",
                issue.getDescription()
        );
    }

    public static String userText(User user) {
        return (user == null ? "" : user.getDisplay());
    }

    @BotRequestMapping(key = MenuKey.VIEW_TASK)
    public void handleIssue(Request req, Response resp) {
        var text = req.getText();
        switch (text) {
            case KEY_CANCEL:
                MainController.goToMainMenu(req, resp);
                return;
            default:
                break;
        }

        MainController.goToMainMenu(req, resp);
        viewIssue(req.getClient(), text, resp);
    }
}
