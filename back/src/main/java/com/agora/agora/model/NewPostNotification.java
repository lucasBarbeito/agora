package com.agora.agora.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "new_post_notification")
public class NewPostNotification extends Notification{
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post newPost;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
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
