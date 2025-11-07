package com.mitrais.cdc.cli.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ScreenManager {

    private final Map<String, Screen> screenMap = new HashMap<>();
    private ApplicationContext applicationContext;

    @Autowired
    public ScreenManager(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Screen getScreen(ScreenEnum screenEnum) {
        Object screen = applicationContext.getBean(screenEnum.getClassObject());
        return screen != null ? (Screen) screen : null;
    }
}
