package com.agora.agora.model.type;

public enum LinkType {
    EMAIL,
    PHONE,
    INSTAGRAM,
    FACEBOOK,
    TWITTER;

    public static String toString(LinkType linkType) {
        switch (linkType) {
            case EMAIL:
                return "EMAIL";
            case PHONE:
                return "PHONE";
            case INSTAGRAM:
                return "INSTAGRAM";
            case FACEBOOK:
                return "FACEBOOK";
            case TWITTER:
                return "TWITTER";
        }
        return "";
    }
}
