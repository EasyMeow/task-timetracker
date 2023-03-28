package com.example.timetrack.ui.uitls;

import com.vaadin.flow.component.notification.Notification;
import org.vaadin.addons.badge.Badge;

public class Utils {

    public static void showErrorNotification(String message) {
        Notification notification = new Notification(message, 3000, Notification.Position.TOP_END);
        notification.setThemeName("error");
        notification.open();
    }

    public static Badge createDefaultBadge(String text) {
        Badge badge = new Badge(text);
        badge.setVariant(Badge.BadgeVariant.NORMAL);
        return badge;
    }

    public static Badge createSuccessBadge(String text) {
        Badge badge = new Badge(text);
        badge.setVariant(Badge.BadgeVariant.SUCCESS);
        return badge;
    }

    public static Badge createSuccessPrimaryBadge(String text) {
        Badge badge = new Badge(text);
        badge.setVariant(Badge.BadgeVariant.SUCCESS);
        badge.setPrimary(true);
        return badge;
    }

    public static Badge createContrastBadge(String text) {
        Badge badge = new Badge(text);
        badge.setVariant(Badge.BadgeVariant.CONTRAST);
        return badge;
    }

    public static Badge createErrorBadge(String text) {
        Badge badge = new Badge(text);
        badge.setVariant(Badge.BadgeVariant.ERROR);
        return badge;
    }
}
