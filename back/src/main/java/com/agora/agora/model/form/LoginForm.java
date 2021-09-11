package com.agora.agora.model.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class LoginForm {

    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;

    public LoginForm(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginForm() {
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
