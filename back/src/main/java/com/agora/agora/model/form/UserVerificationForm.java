package com.agora.agora.model.form;

public class UserVerificationForm {

    String userVerificationToken;

    public UserVerificationForm(String userVerificationToken) {
        this.userVerificationToken = userVerificationToken;
    }

    public UserVerificationForm() {
    }

    public String getUserVerificationToken() {
        return userVerificationToken;
    }

    public void setUserVerificationToken(String userVerificationToken) {
        this.userVerificationToken = userVerificationToken;
    }
}
