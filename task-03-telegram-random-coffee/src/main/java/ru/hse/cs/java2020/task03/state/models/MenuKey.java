package ru.hse.cs.java2020.task03.state.models;

public final class MenuKey {
    public static final String UNAUTHORIZED = "";
    public static final String AWAIT_ORG_ID = "auth.await.orgid";
    public static final String MAIN_MENU = "main.menu";
    public static final String CREATE_TASK_QUEUE = "create.task.queue";
    public static final String CREATE_TASK_NAME = "create.task.name";
    public static final String CREATE_TASK_DESCRIPTION = "create.task.description";
    public static final String CREATE_TASK_ASSIGNEE = "create.task.assignee";

    private MenuKey() {
    }
}
