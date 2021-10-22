package com.agora.agora.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "new_post_notification")
public class NewPostNotification extends Notification{
    @ManyToOne
    private Post newPost;
    @ManyToOne
    private StudyGroup group;

    public NewPostNotification() {
    }

    public NewPostNotification(User user, boolean isRead, LocalDate creationDate, Post newPost, StudyGroup group) {
        super(user, isRead, creationDate);
        this.newPost = newPost;
        this.group = group;
    }

    public Post getNewPost() {
        return newPost;
    }

    public void setNewPost(Post newPost) {
        this.newPost = newPost;
    }

    public StudyGroup getGroup() {
        return group;
    }

    public void setGroup(StudyGroup group) {
        this.group = group;
    }
}
