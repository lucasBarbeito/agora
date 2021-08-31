package com.agora.agora.model;

import javax.persistence.*;
import java.time.LocalDate;

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

    public StudyGroup(){
    }

    public StudyGroup(String name, String description, User creator, LocalDate creationDate) {
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.creationDate = creationDate;
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

}
