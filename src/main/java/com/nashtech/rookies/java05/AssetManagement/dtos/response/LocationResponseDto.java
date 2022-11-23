package com.nashtech.rookies.java05.AssetManagement.dtos.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LocationResponseDto {
    private Integer id;
    private String cityName;
}