package com.agora.agora.model.form;

import com.agora.agora.model.dto.LabelDTO;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class StudyGroupForm {

    @NotNull
    private String name;

    private String description;

    @NotNull
    private List<LabelDTO> labels;

    @NotNull
    private int creatorId;

    @NotNull
    private LocalDate creationDate;

    public StudyGroupForm(@NotNull String name, String description, @NotNull int creatorId, @NotNull LocalDate creationDate, @NotNull List<LabelDTO>  labels) {
        this.name = name;
        this.description = description;
        this.creatorId = creatorId;
        this.creationDate = creationDate;
        this.labels = labels;
    }

    public StudyGroupForm() {
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

    public List<LabelDTO> getLabels() {
        return labels;
    }

    public void setLabels(List<LabelDTO> labels) {
        this.labels = labels;
    }
}
