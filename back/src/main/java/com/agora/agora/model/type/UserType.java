package com.agora.agora.model.type;

public enum UserType {
    USER;

    public static String toString(UserType userType) {
        switch (userType) {
            case USER:
                return "USER";
        }
        return "";
    }
}
