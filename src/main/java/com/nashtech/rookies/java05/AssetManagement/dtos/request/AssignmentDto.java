package com.nashtech.rookies.java05.AssetManagement.dtos.request;

import com.nashtech.rookies.java05.AssetManagement.validator.GreaterThanCurrentDayConstraint;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignmentDto {
    private String assignBy;
    private String assignTo;
    private String assetId;
    private Date assignedDate;
    private String note;
}
