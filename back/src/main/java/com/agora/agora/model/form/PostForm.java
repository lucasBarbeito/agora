package com.agora.agora.model.form;

import com.agora.agora.model.StudyGroup;
import com.agora.agora.model.User;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class PostForm {

    @NotNull
    private String content;

    @NotNull
    private LocalDateTime creationDateAndTime;

    public PostForm() {
    }

    public PostForm(String content, LocalDateTime creationDateAndTime) {
        this.content = content;
        this.creationDateAndTime = creationDateAndTime;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreationDateAndTime() {
        return creationDateAndTime;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreationDateAndTime(LocalDateTime creationDateAndTime) {
        this.creationDateAndTime = creationDateAndTime;
    }
}
