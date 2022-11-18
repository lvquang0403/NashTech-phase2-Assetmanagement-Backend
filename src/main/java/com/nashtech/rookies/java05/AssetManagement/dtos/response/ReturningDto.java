package com.nashtech.rookies.java05.AssetManagement.dtos.response;

import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentReturnState;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * A DTO for the {@link com.nashtech.rookies.java05.AssetManagement.entities.Returning} entity
 */
@Data
@Builder
public class ReturningDto {
    private Integer id;
    private Timestamp returnedDate;
    private AssignmentReturnState state;
    private String AssignTo;
    private String AssignBy;
    private AssignmentResponseDto assignmentResponseDto;
}