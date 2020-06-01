package ru.hse.cs.java2020.task03.controller;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import ru.hse.cs.java2020.task03.bot.Request;
import ru.hse.cs.java2020.task03.bot.Response;
import ru.hse.cs.java2020.task03.bot.auto.BotController;
import ru.hse.cs.java2020.task03.bot.auto.BotRequestMapping;
import ru.hse.cs.java2020.task03.state.models.MenuKey;
import ru.hse.cs.java2020.task03.tracker.models.Issue;
import ru.hse.cs.java2020.task03.tracker.models.SearchIssue;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static ru.hse.cs.java2020.task03.bot.utils.Format.errorMessage;

@BotController
public class SearchIssueController {
    private static final String KEY_CANCEL = "❌ Отмена";
    private static final String KEY_LEFT = "<-";
    private static final String KEY_RIGHT = "->";

    private static final int PAGE_SIZE = 15;

    private static final String PREFIX_ISSUE = "/i";

    public static void start(Request req, Response resp) {
        var state = req.getState();
        var client = req.getClient();

        Map<String, String> filter = new HashMap<>();
        filter.put("assignee", state.getMyself().getUid().toString());
        var searchRequest = new SearchIssue(filter);
        try {
            var response = client.searchIssues(searchRequest, "-updatedAt").execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("Unsuccessful request. Response=" + response.errorBody().string());
            }
            var issues = response.body();
            state.setAllIssues(issues);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendText(errorMessage(e));
            return;
        }

        state.setKey(MenuKey.SEARCH_TASK);
        state.setSearchPage(0);
        showCurrentPage(req, resp);
    }

    public static int pagesCount(int size) {
        return (size + PAGE_SIZE - 1) / PAGE_SIZE;
    }

    public static void showCurrentPage(Request req, Response resp) {
        var state = req.getState();
        var page = state.getSearchPage();
        var issues = state.getAllIssues();

        resp.sendKeyboard(
                String.format(
                        "Cписок задач, назначенных на тебя, отсортированные по убыванию по дате обновления с паджинацией  (страница %d/%d):\n\n",
                        page + 1,
                        pagesCount(issues.size())
                ) + IntStream
                        .range(0, issues.size())
                        .skip(page * PAGE_SIZE)
                        .limit(PAGE_SIZE)
                        .mapToObj(it -> String.format(
                                "/i%s %s", it, issueLine(issues.get(it))
                        ))
                        .collect(Collectors.joining("\n")),
                keyboard(page, pagesCount(issues.size()))
        );
    }

    public static String issueLine(Issue issue) {
        return String.format("<b>%s</b>: %s", issue.getKey(), issue.getSummary());
    }

    @BotRequestMapping(key = MenuKey.SEARCH_TASK)
    public void handlePage(Request req, Response resp) {
        var text = req.getText();
        switch (text) {
            case KEY_CANCEL:
                MainController.goToMainMenu(req, resp);
                return;
            case KEY_LEFT:
                int page = req.getState().getSearchPage();
                if (page > 0) {
                    req.getState().setSearchPage(page - 1);
                }
                showCurrentPage(req, resp);
                return;
            case KEY_RIGHT:
                page = req.getState().getSearchPage();
                if (page + 1 < pagesCount(req.getState().getAllIssues().size())) {
                    req.getState().setSearchPage(page + 1);
                }
                showCurrentPage(req, resp);
                return;
        }

        if (!text.startsWith(PREFIX_ISSUE)) {
            return;
        }
        text = text.substring(PREFIX_ISSUE.length());

        try {
            int pos = Integer.parseInt(text);
            Issue issue = req.getState().getAllIssues().get(pos);
            ViewIssueController.viewIssue(req.getClient(), issue.getKey(), resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Keyboard keyboard(int page, int pagesCount) {
        return new ReplyKeyboardMarkup(new String[][]{
                Stream.of(
                        page > 0 ? KEY_LEFT : null,
                        page + 1 < pagesCount ? KEY_RIGHT : null
                )
                        .filter(Objects::nonNull)
                        .toArray(String[]::new),
                {KEY_CANCEL}
        });
    }
}
