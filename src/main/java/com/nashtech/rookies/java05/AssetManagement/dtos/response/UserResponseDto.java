package com.nashtech.rookies.java05.AssetManagement.dtos.response;

import com.nashtech.rookies.java05.AssetManagement.entities.Assignment;
import com.nashtech.rookies.java05.AssetManagement.entities.Location;
import com.nashtech.rookies.java05.AssetManagement.entities.Role;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.Gender;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
public class UserResponseDto {

    private String firstName;
    private String lastName;
    private String username;
    private Timestamp birth;
    private Gender gender;
    private Timestamp createdWhen;
    private Timestamp updatedWhen;
    private RoleDto role;
    private LocationDto location;

    @Data
    static class RoleDto {
        private Integer id;
        private Integer name;
    }

    @Data
    static class LocationDto {
        private Integer id;
        private Integer cityName;
    }

}

