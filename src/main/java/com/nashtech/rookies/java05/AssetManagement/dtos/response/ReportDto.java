package com.nashtech.rookies.java05.AssetManagement.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportDto {
    private String category;
    private Integer total;
    private Integer assigned;
    private Integer available;
    private Integer notAvailable;
    private Integer waitingForRecycling;
    private Integer recycled;
}
