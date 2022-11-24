package com.nashtech.rookies.java05.AssetManagement.dtos.request;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder

public class UserLoginRequestDto {
    @NotBlank(message = "Username is required!")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;
}
