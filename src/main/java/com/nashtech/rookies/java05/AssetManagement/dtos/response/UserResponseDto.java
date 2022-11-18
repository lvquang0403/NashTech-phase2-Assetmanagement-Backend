package com.nashtech.rookies.java05.AssetManagement.dtos.response;


import com.nashtech.rookies.java05.AssetManagement.entities.enums.Gender;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class UserResponseDto {
    private String id;
    private String fullName;
    private String username;
    private Timestamp birth;
    private Gender gender;
    private Timestamp createdWhen;
    private String role;
    private String location;
}

