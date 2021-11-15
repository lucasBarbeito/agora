package com.agora.agora.model.form;

import com.agora.agora.model.ContactLink;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.List;

public class EditUserForm {

    @Nullable
    private String name, surname, password;
    @Nullable
    private List<ContactLink> contactLinks;

    public EditUserForm(String name, String surname, String password, List<ContactLink> contactLinks) {
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.contactLinks = contactLinks;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ContactLink> getContactLinks() {
        return contactLinks;
    }

    public void setContactLinks(List<ContactLink> contactLinks) {
        this.contactLinks = contactLinks;
    }
}
