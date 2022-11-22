package com.nashtech.rookies.java05.AssetManagement.dtos.response;

import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.sql.Date;
import java.util.List;

@Builder
@Data
public class AssetResponseDto {

    private String id;
    private String name;
    private String specification;
    private String state;
    private Date installedDate;
    private Timestamp createdWhen;
    private Timestamp updatedWhen;
    private String categoryName;
    private String location;
    private List<ReturningDto> returningDtoList;
}
