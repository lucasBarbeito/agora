package com.agora.agora.model;

import com.agora.agora.model.type.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users",
    uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
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

    @OneToMany
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private List<ContactLink> contactLinks;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "user",
            targetEntity = StudyGroupUser.class)
    @JsonIgnore
    private List<StudyGroupUser> studyGroups;

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
        this.contactLinks = new ArrayList<>();
        this.studyGroups = new ArrayList<>();
    }

    public User() {
        studyGroups = new ArrayList<>();
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

    public List<StudyGroupUser> getStudyGroups() {
        return studyGroups;
    }


    public List<ContactLink> getContactLinks() {
        return contactLinks;
    }

    public void setContactLinks(List<ContactLink> contactLinks) {
        this.contactLinks = contactLinks;
    }
}
