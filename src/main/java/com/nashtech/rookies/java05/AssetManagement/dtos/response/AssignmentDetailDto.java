package com.nashtech.rookies.java05.AssetManagement.dtos.response;

import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentState;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@Builder
public class AssignmentDetailDto {
    private Integer id;
    private String assetId;
    private String assetName;
    private String categoryName;
    private String specification;
    private String assignToUsername;
    private String assignByUsername;
    private Date assignedDate;
    private String state;
    private String note;
    private String assignToId;
    private String assignToFirstName;
    private String assignToLastName;
}
