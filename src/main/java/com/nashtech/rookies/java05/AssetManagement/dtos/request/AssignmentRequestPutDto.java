package com.nashtech.rookies.java05.AssetManagement.dtos.request;

import com.nashtech.rookies.java05.AssetManagement.validator.GreaterThanCurrentDayConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentRequestPutDto {
    @NotEmpty
    private String assignTo;
    @NotEmpty
    private String assetId;
    @GreaterThanCurrentDayConstraint
    private Date assignedDate;
    private String note;
}
