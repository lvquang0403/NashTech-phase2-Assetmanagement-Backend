package com.nashtech.rookies.java05.AssetManagement.dtos.response;

import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentState;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@Builder
public class AssignmentDetailDto {
    private String assetId;
    private String assetName;
    private String specification;
    private String assignTo;
    private String assignBy;
    private Date assignedDate;
    private String state;
    private String note;
}
