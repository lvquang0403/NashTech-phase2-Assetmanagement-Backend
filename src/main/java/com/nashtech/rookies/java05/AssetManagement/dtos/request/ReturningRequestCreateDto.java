package com.nashtech.rookies.java05.AssetManagement.dtos.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.nashtech.rookies.java05.AssetManagement.entities.Returning} entity
 */
@Setter
@Getter
public class ReturningRequestCreateDto {
    @NotNull(message = "request sender is null")
    @NotEmpty(message = "assignment is empty")
    @NotBlank(message = "assignment is blank")
    private String requestById;

    @NotNull(message = "assignment is null")
    @NotEmpty(message = "assignment is empty")
    @NotBlank(message = "assignment is blank")
    private Integer assignmentId;

    public ReturningRequestCreateDto() {
    }
}