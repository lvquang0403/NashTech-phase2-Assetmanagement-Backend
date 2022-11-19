package com.nashtech.rookies.java05.AssetManagement.dtos.response;

import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentState;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * A DTO for the {@link com.nashtech.rookies.java05.AssetManagement.entities.Assignment} entity
 */
@Data
@Builder
public class AssignmentResponseDto {
    private Integer id;
    private Timestamp createdWhen;
    private Timestamp updatedWhen;
    private String note;
    private AssignmentState state;
}