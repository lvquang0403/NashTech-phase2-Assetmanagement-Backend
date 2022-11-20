package com.nashtech.rookies.java05.AssetManagement.dtos.response;

import com.nashtech.rookies.java05.AssetManagement.entities.Assignment;
import com.nashtech.rookies.java05.AssetManagement.entities.Location;
import com.nashtech.rookies.java05.AssetManagement.entities.Role;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.Gender;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
public class UserResponseDto {

    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String birth;
    private Gender gender;
    private String joinedDate;
    private Timestamp createdWhen;
    private Timestamp updatedWhen;
    private RoleDto role;
    private LocationDto location;

    public void setRole(Role role){
        if (this.role==null)    this.role=new RoleDto();
        this.role.setId(role.getId());
        this.role.setName(role.getName());
    }
    public void setLocation(Location location){
        if (this.location==null)    this.location=new LocationDto();
        this.location.setId(location.getId());
        this.location.setCityName(location.getCityName());
    }

    @Data
    static class RoleDto {
        private Integer id;
        private String name;
    }

    @Data
    static class LocationDto {
        private Integer id;
        private String cityName;
    }

}

