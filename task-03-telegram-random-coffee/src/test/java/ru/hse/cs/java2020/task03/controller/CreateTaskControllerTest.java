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
import ru.hse.cs.java2020.task03.tracker.models.Queue;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static ru.hse.cs.java2020.task03.controller.ControllerTest.messageOf;

public class CreateTaskControllerTest {

    private CreateTaskController controller;
    private ResponseMock resp;
    private State state;

    @Before
    public void setUp() throws Exception {
        controller = new CreateTaskController();
        resp = new ResponseMock();
        state = new State();
    }

    @Test
    public void testScript() {
        final int uid = 1001;
        Myself myself = new Myself();
        myself.setUid(uid);
        state.setMyself(myself);

        Queue queue1 = new Queue();
        queue1.setId(1);

        RequestMock req = messageOf(state, "Создать задачу");
        req.setClient(ClientMock.create(
                List.of(queue1),
                new Issue()
        ));

        CreateTaskController.start(req, resp);
        assertEquals(state.getKey(), MenuKey.CREATE_TASK_QUEUE);

        req.setText("1. queue.name");
        controller.handleQueue(req, resp);
        assertEquals(state.getKey(), MenuKey.CREATE_TASK_NAME);

        req.setText("New task");
        controller.handleName(req, resp);
        assertEquals(state.getKey(), MenuKey.CREATE_TASK_DESCRIPTION);

        req.setText("Description");
        controller.handleDescription(req, resp);
        assertEquals(state.getKey(), MenuKey.CREATE_TASK_ASSIGNEE);

        req.setText("Да");
        controller.handleAssignee(req, resp);
        assertEquals(state.getKey(), MenuKey.MAIN_MENU);

        assertEquals("1", state.getCreateIssue().getQueue().getId());
        assertEquals("New task", state.getCreateIssue().getSummary());
        assertEquals("Description", state.getCreateIssue().getDescription());
        assertEquals("1001", state.getCreateIssue().getAssignee().getId());
    }
}
