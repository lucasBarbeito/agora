package com.agora.agora.model.dto;

import java.time.LocalDate;
import java.util.List;

public class FullStudyGroupDTO extends StudyGroupDTO{

    private List<UserContactDTO> userContacts;

    public FullStudyGroupDTO(int id, String name, String description, int creatorId, LocalDate creationDate, List<UserContactDTO> userContacts, List<LabelDTO> labels) {
        super(id, name, description, creatorId, creationDate, labels);
        this.userContacts = userContacts;
    }

    public FullStudyGroupDTO() {
    }

    public List<UserContactDTO> getUserContacts() {
        return userContacts;
    }

    public void setUserContacts(List<UserContactDTO> userContacts) {
        this.userContacts = userContacts;
    }
}
