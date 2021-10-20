package com.agora.agora.model;

import com.agora.agora.model.type.LinkType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "labels")
public class ContactLink implements Identifiable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @Column
    private LinkType linkType;

    @Column
    private String link;

    public ContactLink(LinkType linkType, String link) {
        this.linkType = linkType;
        this.link = link;
    }

    public ContactLink() {
    }

    @Override
    public int getId() {
        return id;
    }

    public LinkType getLinkType() {
        return linkType;
    }

    public void setLinkType(LinkType linkType) {
        this.linkType = linkType;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
