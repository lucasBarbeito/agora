package com.agora.agora.model;


import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "study_group_post")
public class Post implements Identifiable{


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @Column
    private String content;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private StudyGroup studyGroup;

    @ManyToOne
    private User creator;

    @Column
    private LocalDateTime creationDateTime;

    public Post() {
    }

    public Post(String content, StudyGroup studyGroup, User creator, LocalDateTime creationDateTime) {
        this.content = content;
        this.studyGroup = studyGroup;
        this.creator = creator;
        this.creationDateTime = creationDateTime;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getContent() {
        return content;
    }

    public User getCreator() {
        return creator;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public StudyGroup getStudyGroup() {
        return studyGroup;
    }
}
