package com.agora.agora.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "labels", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Label implements Identifiable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @Column
    private String name;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "label",
            targetEntity = StudyGroupLabel.class)
    @JsonIgnore
    private List<StudyGroupLabel> studyGroups;

    public Label() {
        studyGroups = new ArrayList<>();
    }

    public Label(String name) {
        this.name = name;
        studyGroups = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StudyGroupLabel> getStudyGroups() {
        return studyGroups;
    }

    public void setStudyGroups(List<StudyGroupLabel> studyGroups) {
        this.studyGroups = studyGroups;
    }

    @Override
    public int getId() {
        return id;
    }
}
