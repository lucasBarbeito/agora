package com.agora.agora.model;


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
    private StudyGroup studyGroup;

    @ManyToOne
    private User creator;

    @Column
    private LocalDateTime creationDateAndTime;

    public Post() {
    }

    public Post(String content, StudyGroup studyGroup, User creator, LocalDateTime creationDateAndTime) {
        this.content = content;
        this.studyGroup = studyGroup;
        this.creator = creator;
        this.creationDateAndTime = creationDateAndTime;
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

    public void setCreationDateAndTime(LocalDateTime creationDateAndTime) {
        this.creationDateAndTime = creationDateAndTime;
    }

    public String getContent() {
        return content;
    }

    public User getCreator() {
        return creator;
    }

    public LocalDateTime getCreationDateAndTime() {
        return creationDateAndTime;
    }

    public StudyGroup getStudyGroup() {
        return studyGroup;
    }
}
