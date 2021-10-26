package com.agora.agora.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "group_invite_notification")
public class GroupInviteNotification extends Notification{
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private StudyGroup group;

    public GroupInviteNotification() {
    }

    public GroupInviteNotification(User user, boolean isRead, LocalDate creationDate, StudyGroup group) {
        super(user, isRead, creationDate);
        this.group = group;
    }
}
