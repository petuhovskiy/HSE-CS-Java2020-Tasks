package ru.hse.cs.java2020.task03.bot.utils;

import org.apache.logging.log4j.util.Strings;
import org.springframework.web.util.HtmlUtils;

import java.util.Arrays;

public class Format {
    public static String lines(String... args) {
        return Strings.join(Arrays.asList(args), '\n');
    }

    public static boolean noText(String text) {
        return text == null || text.equals("");
    }

    public static String errorMessage(Exception e) {
        return lines("Произошла ошибка.", "Детали:", HtmlUtils.htmlEscape(e.getMessage()));
    }
}
