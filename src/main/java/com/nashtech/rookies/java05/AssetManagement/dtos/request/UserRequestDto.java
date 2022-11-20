package com.nashtech.rookies.java05.AssetManagement.dtos.request;

import com.nashtech.rookies.java05.AssetManagement.entities.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
public class UserRequestDto {
    private String firstName;
    private String lastName;
    private String birth;
    private Gender gender;
    private String joinedDate;
    private Integer roleId;
    private Integer locationId;
}
