package com.nashtech.rookies.java05.AssetManagement.dtos.request;

import com.nashtech.rookies.java05.AssetManagement.validator.GreaterThanCurrentDayConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentRequestPostDto {
    @NotEmpty
    private String assignBy;
    @NotEmpty
    private String assignTo;
    @NotEmpty
    private String assetId;
    @GreaterThanCurrentDayConstraint
    private Date assignedDate;
    private String note;
}
