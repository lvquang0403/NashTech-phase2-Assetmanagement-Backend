package com.nashtech.rookies.java05.AssetManagement.dtos.response;

import com.nashtech.rookies.java05.AssetManagement.entities.Location;
import com.nashtech.rookies.java05.AssetManagement.entities.Role;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class UserLoginResponseDto {
    private String id;
    private String firstName;
    private String lastName;
    private Date birth;
    private String gender;
    private Date joinedDate;
    private RoleResponseDto role;
    private LocationResponseDto location;
    private String accessToken;
}
