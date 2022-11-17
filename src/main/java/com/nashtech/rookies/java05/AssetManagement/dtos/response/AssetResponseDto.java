package com.nashtech.rookies.java05.AssetManagement.dtos.response;

import com.nashtech.rookies.java05.AssetManagement.entities.Assignment;
import com.nashtech.rookies.java05.AssetManagement.entities.Category;
import com.nashtech.rookies.java05.AssetManagement.entities.Location;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.Recycle;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
public class AssetResponseDto {

    private String id;
    private String name;
    private String specification;
    private AssetState state;
    private Timestamp createdWhen;
    private Timestamp updatedWhen;
    private Recycle recycle;
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
        private String cityName;
    }

}
