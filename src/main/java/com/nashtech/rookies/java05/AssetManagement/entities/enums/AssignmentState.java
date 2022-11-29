package com.nashtech.rookies.java05.AssetManagement.entities.enums;

public enum AssignmentState {
    WAITING ("Waiting for acceptance"),
    ACCEPTED ("Accepted"),
    DECLINED ("Declined");

    private String name;
    AssignmentState(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
