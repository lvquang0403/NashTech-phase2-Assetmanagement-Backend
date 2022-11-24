package com.nashtech.rookies.java05.AssetManagement.dtos.request;

import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;


@Setter
@Getter
@Builder
public class AssetRequestDto {
    private String name;
    private String specification;
    private String categoryId;
    private AssetState state;
    private Integer locationId;
    private Date installedDate;

    public AssetRequestDto(String name, String specification, String categoryId, AssetState state, Integer locationId, Date installedDate) {
        this.name = name;
        this.specification = specification;
        this.categoryId = categoryId;
        this.state = state;
        this.locationId = locationId;
        this.installedDate = installedDate;
    }
    public AssetRequestDto() {
    }

}