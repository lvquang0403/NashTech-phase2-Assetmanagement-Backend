package com.nashtech.rookies.java05.AssetManagement.dtos.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationResponseDto {
    private Integer id;
    private String cityName;
}