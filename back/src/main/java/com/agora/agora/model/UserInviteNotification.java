package com.agora.agora.model;


import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "user_invitation_notification")
public class UserInviteNotification extends Notification{

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private StudyGroup group;

    public UserInviteNotification() {
    }

    public UserInviteNotification(User user, boolean isRead, LocalDate creationDate, StudyGroup group) {
        super(user, isRead, creationDate);
        this.group = group;
    }

    public StudyGroup getGroup() {
        return group;
    }

    public void setGroup(StudyGroup group) {
        this.group = group;
    }
}
