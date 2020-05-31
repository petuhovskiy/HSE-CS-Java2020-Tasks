package ru.hse.cs.java2020.task03.bot.auto;

import ru.hse.cs.java2020.task03.bot.MessageHandler;
import ru.hse.cs.java2020.task03.bot.Request;
import ru.hse.cs.java2020.task03.bot.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public class RouteItem implements MessageHandler {
    private final Object bean;
    private final Method method;
    private final Optional<String> key;

    public RouteItem(Object bean, Method method, Optional<String> key) {
        this.bean = bean;
        this.method = method;
        this.key = key;
    }

    public Object getBean() {
        return bean;
    }

    public Method getMethod() {
        return method;
    }

    public Optional<String> getKey() {
        return key;
    }

    @Override
    public void handleMessage(Request req, Response resp) {
        try {
            method.invoke(bean, req, resp);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
