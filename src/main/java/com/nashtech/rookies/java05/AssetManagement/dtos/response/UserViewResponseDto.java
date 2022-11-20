package com.nashtech.rookies.java05.AssetManagement.dtos.response;

import lombok.Builder;
import lombok.Data;


import java.util.Date;

@Data
@Builder
public class UserViewResponseDto {
    private String id;
    private String fullName;
    private String userName;
    private Date joinedDate;
    private String role;

}
