package com.nashtech.rookies.java05.AssetManagement.dtos.response;

import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentReturnState;
import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
public class ReturningDtoFakeData {

    private Integer id;
    private Timestamp returnedDate;
    private String state;
    private String assignTo;
    private String acceptedBy;
    private Timestamp assignedDate;
    private String assetName;
    private String assetCode;

}
