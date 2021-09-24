package com.agora.agora.model.form;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class EditStudyGroupForm {

    @NotNull
    private String name;

    private String description;

/* TODO
    @NotNull
    private List<Label> labels = new ArrayList<>();

 */

    public EditStudyGroupForm(String name, String description) {
        this.name = name;
        this.description = description;
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
}
