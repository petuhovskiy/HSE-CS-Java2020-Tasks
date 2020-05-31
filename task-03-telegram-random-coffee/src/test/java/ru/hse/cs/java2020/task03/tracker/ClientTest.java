package ru.hse.cs.java2020.task03.tracker;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
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
}
