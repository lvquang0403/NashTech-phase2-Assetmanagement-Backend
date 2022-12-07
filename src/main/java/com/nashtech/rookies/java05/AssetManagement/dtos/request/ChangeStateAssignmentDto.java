package com.nashtech.rookies.java05.AssetManagement.dtos.request;


import com.nashtech.rookies.java05.AssetManagement.validator.GreaterThanCurrentDayConstraint;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.sql.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeStateAssignmentDto {
    private int id;
    private String state;
}
