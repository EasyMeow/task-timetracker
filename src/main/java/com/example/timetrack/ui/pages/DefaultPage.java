package com.example.timetrack.ui.pages;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.server.VaadinSession;

public interface DefaultPage extends  BeforeEnterObserver {

    @Override
    default void beforeEnter(BeforeEnterEvent event) {

        Object user = VaadinSession.getCurrent().getAttribute("user");

        if (this instanceof LoginPage && user != null) {
            event.forwardTo("tasks");
            return;
        }

        beforeEnterOver(event);
    }

    default void beforeEnterOver(BeforeEnterEvent event) {

    }
}
