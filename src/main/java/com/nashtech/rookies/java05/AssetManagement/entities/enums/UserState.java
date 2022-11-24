package com.nashtech.rookies.java05.AssetManagement.entities.enums;

public enum UserState {
    ACTIVE ("Active"),
    INACTIVE ("Inactive");

    private String name;
    UserState(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
