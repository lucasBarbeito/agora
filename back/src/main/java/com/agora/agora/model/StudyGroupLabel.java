package com.agora.agora.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "study_group_label", uniqueConstraints = @UniqueConstraint(columnNames = {"study_group_id", "label_id"}))
public class StudyGroupLabel implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @ManyToOne
    @JoinColumn(name = "label_id")
    private Label label;

    @ManyToOne
    @JoinColumn(name = "study_group_id")
    @JsonIgnore
    private StudyGroup studyGroup;

    public StudyGroupLabel() {
    }

    public StudyGroupLabel(Label label, StudyGroup studyGroup) {
        this.label = label;
        this.studyGroup = studyGroup;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public StudyGroup getStudyGroup() {
        return studyGroup;
    }

    public void setStudyGroup(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }

    @Override
    public int getId() {
        return id;
    }


}
