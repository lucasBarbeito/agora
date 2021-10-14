package com.agora.agora.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "new_member_notification")
public class NewMemberNotification extends Notification {
    @ManyToOne
    private User newMember;
    @ManyToOne
    private StudyGroup studyGroup;

    public NewMemberNotification() {
    }

    public NewMemberNotification(int id, User user, boolean isRead, LocalDate creationDate, User newMember, StudyGroup studyGroup) {
        super(id, user, isRead, creationDate);
        this.newMember = newMember;
        this.studyGroup = studyGroup;
    }
}