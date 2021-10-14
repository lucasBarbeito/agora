package com.agora.agora.model;

import javax.persistence.*;
import java.time.LocalDate;

@MappedSuperclass
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @ManyToOne
    private User user;

    @Column
    private boolean isRead;

    @Column
    private LocalDate creationDate;

    public Notification() {
    }

    public Notification(int id, User user, boolean isRead, LocalDate creationDate) {
        this.id = id;
        this.user = user;
        this.isRead = isRead;
        this.creationDate = creationDate;
    }
}
