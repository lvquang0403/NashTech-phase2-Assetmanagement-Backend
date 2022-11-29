package com.nashtech.rookies.java05.AssetManagement.dtos.response;

import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentState;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class AssignmentListResponseDto {
    private int id;
    private String assetId;
    private String assetName;
    private String assignTo;
    private String assignBy;
    private String category;
    private Timestamp createdWhen;
    private AssignmentState state;
}
