package com.agora.agora.model;

import com.agora.agora.model.type.UserType;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "users",
    uniqueConstraints = @UniqueConstraint(columnNames = {"email","isVerified"}))
public class User implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @Column
    private String name;

    @Column
    private String surname;

    @Column
    @Email
    private String email;

    @Column
    private String password;

    @Column
    private boolean isVerified;

    @Column
    private String userVerificationToken;

    @NotNull
    @Column
    private UserType type;

    public User(String name, String surname, String email, String password, boolean isVerified, UserType userType) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.isVerified = isVerified;
        this.type=userType;
    }

    public User() {
    }

    @Override
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserType getType() {
        return type;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getUserVerificationToken() {
        return userVerificationToken;
    }

    public void setUserVerificationToken(String useVerificationToken) {
        this.userVerificationToken = useVerificationToken;
    }
}
