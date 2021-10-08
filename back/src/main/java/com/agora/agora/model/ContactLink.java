package com.agora.agora.model;

import com.agora.agora.model.type.LinkType;

public class ContactLink {

    private LinkType linkType;

    private String link;

    public ContactLink(LinkType linkType, String link) {
        this.linkType = linkType;
        this.link = link;
    }

    public ContactLink() {
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
