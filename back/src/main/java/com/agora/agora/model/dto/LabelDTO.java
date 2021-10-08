package com.agora.agora.model.dto;

public class LabelDTO {

    private int id;

    private String name;

    public LabelDTO() {
    }

    public LabelDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
