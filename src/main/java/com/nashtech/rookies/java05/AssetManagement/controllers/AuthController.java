package com.nashtech.rookies.java05.AssetManagement.controllers;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.UserLoginRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.SuccessResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.UserLoginResponseDto;
import com.nashtech.rookies.java05.AssetManagement.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping(value = "api")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public SuccessResponse<UserLoginResponseDto> login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto){
        return authService.loginHandler(userLoginRequestDto);
    }

}
