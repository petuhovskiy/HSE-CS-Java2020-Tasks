package ru.hse.cs.java2020.task03.tracker;

import org.junit.Ignore;
import org.junit.Test;
import ru.hse.cs.java2020.task03.tracker.models.CreateIssue;
import ru.hse.cs.java2020.task03.tracker.models.QueueLink;
import ru.hse.cs.java2020.task03.tracker.models.SearchIssue;
import ru.hse.cs.java2020.task03.tracker.models.User;

import java.io.IOException;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

@Ignore("need to provide real tokens")
public class ClientTest {
    private static final Logger LOG = Logger.getLogger("TrackerClientTest");

    private static final String TOKEN = System.getenv("TEST_YA_TOKEN");
    private static final String ORG_ID = System.getenv("TEST_TRACKER_ORG");

    private static final int OK = 200;
    private static final int CREATED = 201;
    private static final int FORBIDDEN = 403;

    private final ClientFactory factory = new ClientFactory();

    @Test
    public void myself() throws IOException {
        var client = factory.buildClient(TOKEN, ORG_ID);

        var resp = client.myself().execute();
        assertTrue(resp.isSuccessful());
        assertEquals(OK, resp.code());
        LOG.log(Level.INFO, resp.body().toString());
    }

    @Test
    public void myselfFail() throws IOException {
        var client = factory.buildClient("abcde", "12345");

        var resp = client.myself().execute();
        assertFalse(resp.isSuccessful());
        assertEquals(FORBIDDEN, resp.code());
        LOG.log(Level.INFO, resp.toString());
    }

    @Test
    public void queues() throws IOException {
        var client = factory.buildClient(TOKEN, ORG_ID);
        var resp = client.queues().execute();
        assertTrue(resp.isSuccessful());
        assertEquals(OK, resp.code());
        LOG.log(Level.INFO, resp.body().toString());
    }

    @Test
    public void createIssue() throws IOException {
        var client = factory.buildClient(TOKEN, ORG_ID);
        var queues = client.queues().execute().body();
        var queue = queues.get(0);

        LOG.log(Level.INFO, "Creating task in " + queue);
        var req = new CreateIssue();
        req.setQueue(new QueueLink(queue.getId().toString()));
        req.setSummary("New test task");
        req.setDescription("Sample test description. A bit longer.");

        var resp = client.createIssue(req).execute();
        assertTrue(resp.isSuccessful());
        assertEquals(CREATED, resp.code());
        LOG.log(Level.INFO, resp.body().toString());
    }

    @Test
    public void createIssueOnMyself() throws IOException {
        var client = factory.buildClient(TOKEN, ORG_ID);
        var queues = client.queues().execute().body();
        var queue = queues.get(0);
        var myself = client.myself().execute().body();

        LOG.log(Level.INFO, "Creating task in " + queue);
        var req = new CreateIssue();
        req.setQueue(new QueueLink(queue.getId().toString()));
        req.setSummary("New my task");
        req.setDescription("Sample test description. Want to do this task.");
        req.setAssignee(new User(myself.getUid().toString()));

        var resp = client.createIssue(req).execute();
        assertTrue(resp.isSuccessful());
        assertEquals(CREATED, resp.code());
        LOG.log(Level.INFO, resp.body().toString());
    }

    @Test
    public void issue() throws IOException {
        var client = factory.buildClient(TOKEN, ORG_ID);

        var resp = client.issue("RWLISTIO-2").execute();
        assertTrue(resp.isSuccessful());
        assertEquals(OK, resp.code());
        LOG.log(Level.INFO, resp.body().toString());
    }

    @Test
    public void searchIssues() throws IOException {
        var client = factory.buildClient(TOKEN, ORG_ID);

        var myself = client.myself().execute().body();
        var req = new SearchIssue(Collections.singletonMap("assignee", myself.getUid().toString()));

        var resp = client.searchIssues(req, "-updatedAt").execute();
        assertTrue(resp.isSuccessful());
        assertEquals(OK, resp.code());
        LOG.log(Level.INFO, resp.body().toString());
    }

    @Test
    public void commentsIssue() throws IOException {
        var client = factory.buildClient(TOKEN, ORG_ID);

        var resp = client.commentsIssue("RWLISTIO-2").execute();
        assertTrue(resp.isSuccessful());
        assertEquals(OK, resp.code());
        LOG.log(Level.INFO, resp.body().toString());
    }
}
