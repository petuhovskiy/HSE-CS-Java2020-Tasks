package ru.hse.cs.java2020.task03.bot.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Inline {
    private final String[] path;

    public Inline(String[] path) {
        this.path = path;
    }

    public static Inline parseInline(String str) {
        return new Inline(str.split("/"));
    }

    public static String createInline(Object... objects) {
        return Arrays
                .stream(objects)
                .map(Object::toString)
                .collect(Collectors.joining("/"));
    }

    public String[] getPath() {
        return path;
    }

    public String string(int pos) {
        if (pos < 0 || pos >= path.length) {
            return "";
        }
        String res = path[pos];
        if (res == null) {
            return "";
        }
        return res;
    }

    public int integer(int pos) {
        String s = string(pos);
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
