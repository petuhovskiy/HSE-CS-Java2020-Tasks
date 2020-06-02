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

import static org.junit.Assert.assertEquals;
import static ru.hse.cs.java2020.task03.controller.ControllerTest.messageOf;

public class ViewIssueControllerTest {

    private ViewIssueController controller;
    private ResponseMock resp;
    private State state;

    @Before
    public void setUp() throws Exception {
        controller = new ViewIssueController();
        resp = new ResponseMock();
        state = new State();
    }

    @Test
    public void testScript() {
        Myself myself = new Myself();
        myself.setLogin("testaccount");
        state.setMyself(myself);

        Issue issue = new Issue();
        issue.setSummary("Issue name");
        issue.setDescription("Test description");
        issue.setKey("ABC-08");
        issue.setCommentWithoutExternalMessageCount(0);

        User createdBy = new User();
        createdBy.setDisplay("Creator of test issue");
        issue.setCreatedBy(createdBy);

        RequestMock req = messageOf(state, "Просмотреть задачу");
        req.setClient(ClientMock.create(
                issue
        ));

        ViewIssueController.start(req, resp);
        assertEquals(state.getKey(), MenuKey.VIEW_TASK);

        req.setText("TEST-001");
        controller.handleIssue(req, resp);
        assertEquals(state.getKey(), MenuKey.MAIN_MENU);

        resp.assertText("Задача <a href=\"https://tracker.yandex.ru/ABC-08\">ABC-08</a>\n" + "\n"
                + "<b>Название:</b> Issue name\n"
                + "<b>Автор:</b> Creator of test issue\n"
                + "<b>Исполнитель:</b> \n"
                + "<b>Наблюдатели:</b> Никого\n"
                + "<b>Комментариев:</b> 0\n"
                + "\n"
                + "<b>Описание:</b>\n"
                + "Test description", 2);
    }
}
