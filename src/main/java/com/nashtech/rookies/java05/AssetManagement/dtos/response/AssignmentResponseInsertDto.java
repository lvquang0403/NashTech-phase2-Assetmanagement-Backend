package com.nashtech.rookies.java05.AssetManagement.dtos.response;

import com.nashtech.rookies.java05.AssetManagement.entities.*;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignmentResponseInsertDto {
    private Integer id;
    private String createdWhen;
    private String updatedWhen;
    private String note;
    private String state;
    private String assignedTo;
    private String assignedBy;
    private Date assignedDay;
    private String assetId;
    private Integer returningId;
}
