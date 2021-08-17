package com.agora.agora.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "user",
    uniqueConstraints = @UniqueConstraint(columnNames = {"email","isVerified"}))
public class User implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @Column
    @Pattern(regexp = "^[a-zA-Z]+")
    private String name;

    @Column
    @Pattern(regexp = "^[a-zA-Z]+")
    private String surname;

    @Column
    @Email
    private String email;

    @Column
    @Pattern(regexp = "^(?=.*[0-9])+(?=.*[A-Z])+.")
    private String password;

    @Column
    private boolean isVerified;

    public User(String name, String surname, String email, String password, boolean isVerified) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.isVerified = isVerified;
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
}
