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

    private static final String token = System.getenv("TEST_YA_TOKEN");
    private static final String orgId = System.getenv("TEST_TRACKER_ORG");

    private final ClientFactory factory = new ClientFactory();

    @Test
    public void myself() throws IOException {
        var client = factory.buildClient(token, orgId);

        var resp = client.myself().execute();
        assertTrue(resp.isSuccessful());
        assertEquals(200, resp.code());
        LOG.log(Level.INFO, resp.body().toString());
    }

    @Test
    public void myselfFail() throws IOException {
        var client = factory.buildClient("abcde", "12345");

        var resp = client.myself().execute();
        assertFalse(resp.isSuccessful());
        assertEquals(403, resp.code());
        LOG.log(Level.INFO, resp.toString());
    }

    @Test
    public void queues() throws IOException {
        var client = factory.buildClient(token, orgId);
        var resp = client.queues().execute();
        assertTrue(resp.isSuccessful());
        assertEquals(200, resp.code());
        LOG.log(Level.INFO, resp.body().toString());
    }

    @Test
    public void createIssue() throws IOException {
        var client = factory.buildClient(token, orgId);
        var queues = client.queues().execute().body();
        var queue = queues.get(0);

        LOG.log(Level.INFO, "Creating task in " + queue);
        var req = new CreateIssue();
        req.setQueue(new QueueLink(queue.getId().toString()));
        req.setSummary("New test task");
        req.setDescription("Sample test description. A bit longer.");

        var resp = client.createIssue(req).execute();
        assertTrue(resp.isSuccessful());
        assertEquals(201, resp.code());
        LOG.log(Level.INFO, resp.body().toString());
    }

    @Test
    public void createIssueOnMyself() throws IOException {
        var client = factory.buildClient(token, orgId);
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
        assertEquals(201, resp.code());
        LOG.log(Level.INFO, resp.body().toString());
    }

    @Test
    public void issue() throws IOException {
        var client = factory.buildClient(token, orgId);

        var resp = client.issue("RWLISTIO-2").execute();
        assertTrue(resp.isSuccessful());
        assertEquals(200, resp.code());
        LOG.log(Level.INFO, resp.body().toString());
    }

    @Test
    public void searchIssues() throws IOException {
        var client = factory.buildClient(token, orgId);

        var myself = client.myself().execute().body();
        var req = new SearchIssue(Collections.singletonMap("assignee", myself.getUid().toString()));

        var resp = client.searchIssues(req, "-updatedAt").execute();
        assertTrue(resp.isSuccessful());
        assertEquals(200, resp.code());
        LOG.log(Level.INFO, resp.body().toString());
    }
}
