package com.agora.agora.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "group_invite_notification")
public class GroupInviteNotification extends Notification{
    @ManyToOne
    private StudyGroup group;

    public GroupInviteNotification() {
    }

    public GroupInviteNotification(int id, User user, boolean isRead, LocalDate creationDate, StudyGroup group) {
        super(id, user, isRead, creationDate);
        this.group = group;
    }
}
