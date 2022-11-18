package com.nashtech.rookies.java05.AssetManagement.dtos.response;

import com.nashtech.rookies.java05.AssetManagement.entities.Assignment;
import com.nashtech.rookies.java05.AssetManagement.entities.Location;
import com.nashtech.rookies.java05.AssetManagement.entities.Role;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.Gender;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

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

