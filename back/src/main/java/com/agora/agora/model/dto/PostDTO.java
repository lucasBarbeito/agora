package com.agora.agora.model.dto;

import com.agora.agora.model.StudyGroup;
import com.agora.agora.model.User;

import java.time.LocalDateTime;

public class PostDTO {

    private int id;

    private String content;

    private int studyGroupId;

    private int creatorId;

    private LocalDateTime creationDateAndTime;

    public PostDTO() {
    }

    public PostDTO(int id, String content, int studyGroupId, int creatorId, LocalDateTime creationDateAndTime) {
        this.id = id;
        this.content = content;
        this.studyGroupId = studyGroupId;
        this.creatorId = creatorId;
        this.creationDateAndTime = creationDateAndTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStudyGroupId() {
        return studyGroupId;
    }

    public void setStudyGroupId(int studyGroupId) {
        this.studyGroupId = studyGroupId;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public LocalDateTime getCreationDateAndTime() {
        return creationDateAndTime;
    }

    public void setCreationDateAndTime(LocalDateTime creationDateAndTime) {
        this.creationDateAndTime = creationDateAndTime;
    }
}
