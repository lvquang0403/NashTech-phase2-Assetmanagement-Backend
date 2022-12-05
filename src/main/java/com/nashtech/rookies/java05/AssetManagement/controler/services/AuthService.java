package com.nashtech.rookies.java05.AssetManagement.controler.services;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.UserLoginRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.SuccessResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.UserLoginResponseDto;
import org.springframework.stereotype.Component;

@Component
public interface AuthService {
    SuccessResponse<UserLoginResponseDto> loginHandler(UserLoginRequestDto userLoginRequestDto);
}
