package ru.hse.cs.java2020.task03.bot.auto;

import org.springframework.stereotype.Component;
import ru.hse.cs.java2020.task03.bot.InlineHandler;
import ru.hse.cs.java2020.task03.bot.Request;
import ru.hse.cs.java2020.task03.bot.Response;

@Component
public class InlineHandlerAuto implements InlineHandler {
    private final Routes routes;

    public InlineHandlerAuto(Routes routes) {
        this.routes = routes;
    }

    @Override
    public void handleInline(Request req, Response resp) {
        for (RouteItem item : routes.getItems()) {
            if (item.getInlinePrefix().isEmpty()) {
                continue;
            }

            String prefix = item.getInlinePrefix().get();
            if (!req.getCallback().data().startsWith(prefix)) {
                continue;
            }

            item.handleInline(req, resp);
            break;
        }
    }
}

