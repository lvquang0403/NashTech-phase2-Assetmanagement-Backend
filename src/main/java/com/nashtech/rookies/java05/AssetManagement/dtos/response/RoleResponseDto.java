package com.nashtech.rookies.java05.AssetManagement.dtos.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class RoleResponseDto {
    private Integer id;
    private String name;
}
