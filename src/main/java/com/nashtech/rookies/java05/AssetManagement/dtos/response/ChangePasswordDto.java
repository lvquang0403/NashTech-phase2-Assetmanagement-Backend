package com.nashtech.rookies.java05.AssetManagement.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ChangePasswordDto {
//    @JsonIgnore
    private HttpStatus status;

    private String title;
    private String message;
}
