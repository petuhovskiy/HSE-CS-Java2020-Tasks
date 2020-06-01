package ru.hse.cs.java2020.task03.bot.utils;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class InlineTest {

    private static final int ONETWOTHREE = 123;

    @Test
    public void parseInline() {
        assertArrayEquals(
                new String[] {"comments", "123"},
                Inline.parseInline("comments/123").getPath()
        );
    }

    @Test
    public void createInline() {
        assertEquals(
                "comments/123",
                Inline.createInline("comments", ONETWOTHREE)
        );
    }

    @Test
    public void string() {
        assertEquals(
                "comments",
                new Inline(new String[] {"comments", "123"}).string(0)
        );
    }

    @Test
    public void integer() {
        assertEquals(
                ONETWOTHREE,
                new Inline(new String[] {"comments", "123"}).integer(1)
        );
    }
}
