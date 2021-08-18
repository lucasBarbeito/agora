package com.agora.agora.model.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserForm {

    @NotNull
    @Pattern(regexp = "^[a-zA-Z]+")
    private String name;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z]+")
    private String surname;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z]).{8,}")
    private String password;

    public UserForm() {
    }

    public UserForm(@NotNull String name, @NotNull String surname, @NotNull String email, @NotNull String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
