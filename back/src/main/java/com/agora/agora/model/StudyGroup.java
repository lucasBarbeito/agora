package com.agora.agora.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "study_group", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class StudyGroup implements Identifiable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @Column
    private String name;

    @Column
    private String description;

/* TODO
    @Column
    private List<Label> labels = new ArrayList<>();


 */
    @ManyToOne
    private User creator;

    @Column
    private LocalDate creationDate;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "studyGroup",
            targetEntity = StudyGroupUser.class)
    @JsonIgnore
    private List<StudyGroupUser> users;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "studyGroup",
            targetEntity = StudyGroupLabel.class)
    @JsonIgnore
    private List<StudyGroupLabel> labels;

    public StudyGroup(){
        users = new ArrayList<>();
        labels = new ArrayList<>();
    }

    public StudyGroup(String name, String description, User creator, LocalDate creationDate) {
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.creationDate = creationDate;
        labels = new ArrayList<>();
        users = new ArrayList<>();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<StudyGroupUser> getUsers() {
        return users;
    }

    public void setUsers(List<StudyGroupUser> users) {
        this.users = users;
    }

    public List<StudyGroupLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<StudyGroupLabel> labels) {
        this.labels = labels;
    }
}
