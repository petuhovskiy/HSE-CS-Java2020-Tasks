package ru.hse.cs.java2020.task03.controller;

import org.junit.Before;
import org.junit.Test;
import ru.hse.cs.java2020.task03.bot.RequestMock;
import ru.hse.cs.java2020.task03.bot.ResponseMock;
import ru.hse.cs.java2020.task03.state.models.MenuKey;
import ru.hse.cs.java2020.task03.state.models.State;
import ru.hse.cs.java2020.task03.tracker.ClientMock;
import ru.hse.cs.java2020.task03.tracker.models.Issue;
import ru.hse.cs.java2020.task03.tracker.models.Myself;
import ru.hse.cs.java2020.task03.tracker.models.User;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static ru.hse.cs.java2020.task03.controller.ControllerTest.messageOf;

public class SearchIssueControllerTest {

    private SearchIssueController controller;
    private ResponseMock resp;
    private State state;

    @Before
    public void setUp() throws Exception {
        controller = new SearchIssueController();
        resp = new ResponseMock();
        state = new State();
    }

    @Test
    public void testScript() {
        final int uid = 1001;
        Myself myself = new Myself();
        myself.setUid(uid);
        myself.setLogin("testaccount");
        myself.setDisplay("Author");
        state.setMyself(myself);

        User user = new User();
        user.setDisplay(myself.getDisplay());

        Issue issue = new Issue();
        issue.setSummary("Issue name");
        issue.setDescription("Test description");
        issue.setKey("ABC-08");
        issue.setCommentWithoutExternalMessageCount(0);

        issue.setCreatedBy(user);
        issue.setAssignee(user);

        RequestMock req = messageOf(state, "Искать задачу");
        req.setClient(ClientMock.create(
                List.of(issue),
                issue
        ));

        SearchIssueController.start(req, resp);
        assertEquals(state.getKey(), MenuKey.SEARCH_TASK);
        resp.assertText("Cписок задач, назначенных на тебя, "
                + "отсортированные по убыванию по дате обновления с паджинацией"
                + "  (страница 1/1):\n"
                + "\n"
                + "/i0 <b>ABC-08</b>: Issue name", 0);

        req.setText("/i0");
        controller.handlePage(req, resp);
        assertEquals(state.getKey(), MenuKey.SEARCH_TASK);
        resp.assertText("Задача <a href=\"https://tracker.yandex.ru/ABC-08\">ABC-08</a>\n"
                + "\n"
                + "<b>Название:</b> Issue name\n"
                + "<b>Автор:</b> Author\n"
                + "<b>Исполнитель:</b> Author\n"
                + "<b>Наблюдатели:</b> Никого\n"
                + "<b>Комментариев:</b> 0\n"
                + "\n"
                + "<b>Описание:</b>\n"
                + "Test description", 1);
    }
}
