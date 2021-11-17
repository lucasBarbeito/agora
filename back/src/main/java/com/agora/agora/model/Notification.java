package com.agora.agora.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;

@MappedSuperclass
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column
    private boolean isRead;

    @Column
    private LocalDate creationDate;

    public Notification() {
    }

    public Notification(User user, boolean isRead, LocalDate creationDate) {
        this.user = user;
        this.isRead = isRead;
        this.creationDate = creationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
}
