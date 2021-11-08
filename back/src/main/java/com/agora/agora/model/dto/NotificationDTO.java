package com.agora.agora.model.dto;

import com.agora.agora.model.type.NotificationType;

public class NotificationDTO {

    private NotificationType notificationType;

    private int id;

    private int studyGroupId;

    private int notificationTypeId;

    private boolean isRead;

    private int userRecipientId;

    public NotificationDTO(NotificationType notificationType, int id, int studyGroupId, int notificationTypeId, boolean isRead, int userRecipientId) {
        this.notificationType = notificationType;
        this.studyGroupId = studyGroupId;
        this.notificationTypeId = notificationTypeId;
        this.isRead = isRead;
        this.userRecipientId = userRecipientId;
    }

    public NotificationDTO() {
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public int getStudyGroupId() {
        return studyGroupId;
    }

    public void setStudyGroupId(int studyGroupId) {
        this.studyGroupId = studyGroupId;
    }

    public int getNotificationTypeId() {
        return notificationTypeId;
    }

    public void setNotificationTypeId(int notificationTypeId) {
        this.notificationTypeId = notificationTypeId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public int getUserRecipientId() {
        return userRecipientId;
    }

    public void setUserRecipientId(int userRecipientId) {
        this.userRecipientId = userRecipientId;
    }
}
