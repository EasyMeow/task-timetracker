package com.example.timetrack.ui.uitls;

import com.vaadin.flow.component.notification.Notification;

public class Utils {

    public static void showErrorNotification(String message) {
        Notification notification = new Notification(message, 3000, Notification.Position.TOP_END);
        notification.setThemeName("error");
        notification.open();
    }
}
