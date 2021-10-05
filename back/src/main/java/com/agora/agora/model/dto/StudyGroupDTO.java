package com.agora.agora.model.dto;

import java.time.LocalDate;

public class StudyGroupDTO {

    private int id;

    private String name;

    private String description;

/* TODO
    private List<Label> labels = new ArrayList<>();

 */

    private int creatorId;

    private LocalDate creationDate;

    private boolean currentUserIsMember = false;

    public StudyGroupDTO() {
    }

    public StudyGroupDTO(int id, String name, String description, int creatorId, LocalDate creationDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creatorId = creatorId;
        this.creationDate = creationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isCurrentUserIsMember() {
        return currentUserIsMember;
    }

    public void setCurrentUserIsMember(boolean currentUserIsMember) {
        this.currentUserIsMember = currentUserIsMember;
    }
}
