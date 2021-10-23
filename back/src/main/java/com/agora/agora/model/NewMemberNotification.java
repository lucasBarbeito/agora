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

    public NewMemberNotification(User user, boolean isRead, LocalDate creationDate, User newMember, StudyGroup studyGroup) {
        super(user, isRead, creationDate);
        this.newMember = newMember;
        this.studyGroup = studyGroup;
    }

    public User getNewMember() {
        return newMember;
    }

    public void setNewMember(User newMember) {
        this.newMember = newMember;
    }

    public StudyGroup getStudyGroup() {
        return studyGroup;
    }

    public void setStudyGroup(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }
}
