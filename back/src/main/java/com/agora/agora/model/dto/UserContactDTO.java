package com.agora.agora.model.dto;

import com.agora.agora.model.ContactLink;

import java.util.List;

public class UserContactDTO {

    private int id;

    private String name;

    private String email;

    private String surname;

    private List<ContactLink> contactLinks;

    public UserContactDTO(int id, String name, String surname, String email, List<ContactLink> contactLinks) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.contactLinks = contactLinks;
    }

    public UserContactDTO() {
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

    public List<ContactLink> getContactLinks() {
        return contactLinks;
    }

    public void setContactLinks(List<ContactLink> contactLinks) {
        this.contactLinks = contactLinks;
    }
}
