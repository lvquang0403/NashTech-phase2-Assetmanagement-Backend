package com.nashtech.rookies.java05.AssetManagement.dtos.response;

import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentReturnState;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * A DTO for the {@link com.nashtech.rookies.java05.AssetManagement.entities.Returning} entity
 */
@Data
@Builder
public class ReturningDto {
    private Integer id;
    private Date returnedDate;
    private String state;
    private String assignTo;
    private String assignBy;
    private String acceptedBy;
    private String requestedBy;
    private AssetViewResponseDto assetViewResponseDto;
    private AssignmentResponseDto assignmentResponseDto;
}