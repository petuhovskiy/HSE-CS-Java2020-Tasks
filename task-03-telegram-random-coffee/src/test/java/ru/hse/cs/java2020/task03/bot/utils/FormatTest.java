package ru.hse.cs.java2020.task03.bot.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FormatTest {
    @Test
    public void lines() {
        assertEquals("", Format.lines());
        assertEquals("abc", Format.lines("abc"));
        assertEquals("a\nb", Format.lines("a", "b"));
    }

    @Test
    public void noText() {
        assertTrue(Format.noText(""));
        assertTrue(Format.noText(null));
        assertFalse(Format.noText("abc"));
    }

    @Test
    public void errorMessage() {
        assertEquals(
                "Произошла ошибка.\nДетали:\nmessage",
                Format.errorMessage(new RuntimeException("message"))
        );
        assertEquals(
                "Произошла ошибка.\nДетали:\n&lt;a&gt;",
                Format.errorMessage(new RuntimeException("<a>"))
        );
    }
}
