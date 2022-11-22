package com.nashtech.rookies.java05.AssetManagement.entities.enums;

public enum AssignmentReturnState {
    WAITING_FOR_RETURNING ("Waiting for returning"),
    COMPLETED ("Completed");

    private String name;
    AssignmentReturnState(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
