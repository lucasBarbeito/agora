package com.agora.agora.model.dto;

import com.agora.agora.model.type.UserType;

import java.util.List;

public class FullUserDTO {

    //TODO: Agregar lista de grupos cuando se haga merge con master

    private int id;

    private String name;

    private String surname;

    private String email;

    private List<StudyGroupDTO> userGroups;

    public FullUserDTO(){}

    public FullUserDTO(int id, String name, String surname, String email, List<StudyGroupDTO> userGroups) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.userGroups = userGroups;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<StudyGroupDTO> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<StudyGroupDTO> userGroups) {
        this.userGroups = userGroups;
    }
}
