package com.nashtech.rookies.java05.AssetManagement.dtos.response;

import lombok.Builder;
import lombok.Data;
import java.sql.Timestamp;

@Data
@Builder
public class UserViewResponseDto {
    private String id;
    private String fullName;
    private String userName;
    private Timestamp createdWhen;
    private String role;
}
