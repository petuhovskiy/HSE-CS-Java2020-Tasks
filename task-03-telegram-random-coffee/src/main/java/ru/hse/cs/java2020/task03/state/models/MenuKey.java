package ru.hse.cs.java2020.task03.state.models;

public final class MenuKey {
    public static final String UNAUTHORIZED = "";
    public static final String AWAIT_ORG_ID = "auth.await.orgid";
    public static final String MAIN_MENU = "main.menu";
    public static final String CREATE_TASK_QUEUE = "task.create.queue";
    public static final String CREATE_TASK_NAME = "task.create.name";
    public static final String CREATE_TASK_DESCRIPTION = "task.create.description";
    public static final String CREATE_TASK_ASSIGNEE = "task.create.assignee";
    public static final String VIEW_TASK = "task.view.key";
    public static final String SEARCH_TASK = "task.search.page";

    private MenuKey() {
    }
}
