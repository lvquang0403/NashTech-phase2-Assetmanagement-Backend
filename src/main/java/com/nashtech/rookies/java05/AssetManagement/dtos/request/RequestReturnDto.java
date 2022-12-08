package com.nashtech.rookies.java05.AssetManagement.dtos.request;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestReturnDto {
    private Integer id;
    private String acceptBy;
    private String requestById;
    private Integer assignmentId;
}
