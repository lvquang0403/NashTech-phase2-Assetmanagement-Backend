package com.nashtech.rookies.java05.AssetManagement.dtos.request;
import lombok.*;
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
    private String state;
}
