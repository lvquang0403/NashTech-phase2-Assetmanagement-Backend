package com.nashtech.rookies.java05.AssetManagement.entities.enums;
public enum Gender {
    MALE ("Male"), FEMALE ("Female");

    private String name;
    Gender(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
