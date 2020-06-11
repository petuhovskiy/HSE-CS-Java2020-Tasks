package ru.hse.cs.java2020.task03.bot.auto;

import org.springframework.stereotype.Component;
import ru.hse.cs.java2020.task03.bot.MessageHandler;
import ru.hse.cs.java2020.task03.bot.Request;
import ru.hse.cs.java2020.task03.bot.Response;

@Component
public class MessageHandlerAuto implements MessageHandler {
    private final Routes routes;

    public MessageHandlerAuto(Routes routes) {
        this.routes = routes;
    }

    @Override
    public void handleMessage(Request req, Response resp) {
        for (RouteItem item : routes.getItems()) {
            if (item.getKey().isEmpty()) {
                continue;
            }

            if (!item.getKey().get().equals(req.getState().getKey())) {
                continue;
            }

            item.handleMessage(req, resp);
            break;
        }
    }
}
