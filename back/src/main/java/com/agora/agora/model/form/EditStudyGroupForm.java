package com.agora.agora.model.form;

import com.agora.agora.model.dto.LabelIdDTO;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class EditStudyGroupForm {

    @NotNull
    private String name;

    private String description;

    private List<LabelIdDTO> labels;

    public EditStudyGroupForm(String name, String description, List<LabelIdDTO> labels) {
        this.name = name;
        this.description = description;
        this.labels = labels;
    }

    public EditStudyGroupForm() {
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

    public List<LabelIdDTO> getLabels() {
        return labels;
    }

    public void setLabels(List<LabelIdDTO> labels) {
        this.labels = labels;
    }

}
