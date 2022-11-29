package com.nashtech.rookies.java05.AssetManagement.entities.enums;

public enum AssetState {
    AVAILABLE ("Available"),
    NOT_AVAILABLE ("Not available"),
    ASSIGNED ("Assigned"),
    RECYCLED ("Recycled"),
    RECYCLING ("Waiting for recycling");

    private String name;

    AssetState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}