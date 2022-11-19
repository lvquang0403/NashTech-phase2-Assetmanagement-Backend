package com.nashtech.rookies.java05.AssetManagement.dtos.response;

import com.nashtech.rookies.java05.AssetManagement.entities.Category;
import com.nashtech.rookies.java05.AssetManagement.entities.Location;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Setter
@Getter
@Builder
public class AssetResponseInsertDto {

    private String id;
    private String name;
    private String specification;
    private AssetState state;
    private Timestamp createdWhen;
    private Timestamp updatedWhen;
    private Date installedDate;
    private CategoryDto category;
    private LocationDto location;



    @Setter
    @Getter
    private class CategoryDto {
        private String id;
        private String name;

        public CategoryDto(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }



    @Setter
    @Getter
    private class LocationDto {
        private Integer id;
        private String cityName;

        public LocationDto(Integer id, String cityName) {
            this.id = id;
            this.cityName = cityName;
        }
    }

    public void setCategoryDto(Category category){
        this.category =  new CategoryDto(category.getId(), category.getName()) ;
    }

    public void setLocationDto(Location location){
        this.location = new LocationDto(location.getId(), location.getCityName()) ;
    }




}
