package com.agora.agora.model.type;

public enum NotificationType {

    NEW_MEMBER_NOTIFICATION,
    NEW_POST_NOTIFICATION;

    public static String toString(NotificationType notificationType) {
        switch (notificationType) {
            case NEW_MEMBER_NOTIFICATION:
                return "NEW_MEMBER_NOTIFICATION";
            case NEW_POST_NOTIFICATION:
                return "NEW_POST_NOTIFICATION";
        }
        return "";
    }
}
