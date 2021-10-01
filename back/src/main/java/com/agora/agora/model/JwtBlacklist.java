package com.agora.agora.model;

import org.springframework.stereotype.Indexed;

import javax.persistence.*;

@Entity
@Table(name = "jwtblacklist")
public class JwtBlacklist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;
    @Column
    private String token;

    public JwtBlacklist(String token) {
        this.token = token;
    }

    public JwtBlacklist() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
