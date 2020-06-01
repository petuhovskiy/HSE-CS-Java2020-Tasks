package ru.hse.cs.java2020.task03.controller;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import ru.hse.cs.java2020.task03.bot.Request;
import ru.hse.cs.java2020.task03.bot.Response;
import ru.hse.cs.java2020.task03.bot.auto.BotController;
import ru.hse.cs.java2020.task03.bot.auto.BotRequestMapping;
import ru.hse.cs.java2020.task03.tracker.models.Comment;

import java.util.List;
import java.util.stream.Collectors;

import static ru.hse.cs.java2020.task03.bot.utils.Format.errorMessage;
import static ru.hse.cs.java2020.task03.bot.utils.Format.lines;
import static ru.hse.cs.java2020.task03.bot.utils.Inline.createInline;

@BotController
public class CommentsController {
    public static final String PATH = "comments";
    public static final int PAGE_SIZE = 1; // TODO: 5

    public static InlineKeyboardMarkup inlineKeyboard(String text, String key, int page) {
        return new InlineKeyboardMarkup(new InlineKeyboardButton[][]{
                {new InlineKeyboardButton(text).callbackData(createInline(PATH, key, page))}
        });
    }

    @BotRequestMapping(inlinePrefix = PATH + "/")
    public void viewComments(Request req, Response resp) {
        var inline = req.getInline();
        String key = inline.string(1);
        int page = inline.integer(2);

        var client = req.getClient();

        try {
            var response = client.commentsIssue(key).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException("Unsuccessful request. Response=" + response.errorBody().string());
            }
            List<Comment> comments = response.body();

            comments = comments
                    .stream()
                    .skip(PAGE_SIZE * page)
                    .limit(PAGE_SIZE)
                    .collect(Collectors.toList());

            if (comments.size() == 0) {
                resp.sendText("Комментариев больше нет.");
                return;
            }

            resp.sendKeyboard(
                    lines(
                            String.format("Комментарии по задаче %s (страница %d)", key, page + 1),
                            "",
                            comments
                                    .stream()
                                    .map(CommentsController::commentText)
                                    .collect(Collectors.joining("\n\n"))
                    ),
                    CommentsController.inlineKeyboard("Смотреть дальше", key, page + 1)
            );
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendText(errorMessage(e));
        }
    }

    public static String commentText(Comment comment) {
        return lines(
                String.format(
                        "%s [%s]",
                        comment.getCreatedBy().getDisplay(),
                        comment.getCreatedAt()
                ),
                comment.getText()
        );
    }
}
