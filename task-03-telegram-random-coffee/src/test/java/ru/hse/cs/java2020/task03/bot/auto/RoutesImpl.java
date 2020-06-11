package ru.hse.cs.java2020.task03.bot.auto;

import java.util.List;

public class RoutesImpl implements Routes {
    private final List<RouteItem> items;

    public RoutesImpl(List<RouteItem> items) {
        this.items = items;
    }

    @Override
    public List<RouteItem> getItems() {
        return items;
    }
}
