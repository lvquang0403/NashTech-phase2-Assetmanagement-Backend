package com.nashtech.rookies.java05.AssetManagement.dtos.response;

import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class AssetResponseDto {

    private String id;
    private String name;
    private String specification;
    private AssetState state;
    private Timestamp createdWhen;
    private Timestamp updatedWhen;
    private CategoryDto category;
    private LocationDto location;

    @Data
    static class CategoryDto {
        private Integer id;
        private Integer name;
    }

    @Data
    static class LocationDto {
        private Integer id;
        private Integer cityName;
    }


}
