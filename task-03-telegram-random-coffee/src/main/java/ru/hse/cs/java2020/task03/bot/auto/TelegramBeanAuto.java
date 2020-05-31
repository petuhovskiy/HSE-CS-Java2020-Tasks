package ru.hse.cs.java2020.task03.bot.auto;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@Component
public class TelegramBeanAuto implements BeanPostProcessor, Ordered {
    private static final Logger LOG = Logger.getLogger("TelegramBeans");

    private final List<RouteItem> items;
    private final Map<String, Class> botControllerMap;

    public TelegramBeanAuto() {
        this.items = new ArrayList<>();
        this.botControllerMap = new HashMap<>();
    }

    public List<RouteItem> getItems() {
        return items;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(BotController.class)) {
            botControllerMap.put(beanName, beanClass);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!botControllerMap.containsKey(beanName)) {
            return bean;
        }

        Class original = botControllerMap.get(beanName);
        Arrays.stream(original.getMethods())
                .filter(method -> method.isAnnotationPresent(BotRequestMapping.class))
                .forEach((Method method) -> generateController(bean, method));

        return bean;
    }

    private void generateController(Object bean, Method method) {
        BotController botController = bean.getClass().getAnnotation(BotController.class);
        BotRequestMapping botRequestMapping = method.getAnnotation(BotRequestMapping.class);

        Optional<String> key = Optional.empty();
        if (botRequestMapping.key().length != 0) {
            key = Optional.of(botRequestMapping.key()[0]);
        }

        String className = bean.getClass().getSimpleName();
        LOG.info(String.format("Found bot handler, class=%s, key=%s", className, key));

        items.add(new RouteItem(bean, method, key));
    }

    private static final int LAST_ORDER = 100;

    @Override
    public int getOrder() {
        return LAST_ORDER;
    }
}
