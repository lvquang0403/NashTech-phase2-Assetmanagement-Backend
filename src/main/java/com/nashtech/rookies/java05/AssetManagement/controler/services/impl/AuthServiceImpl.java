package com.nashtech.rookies.java05.AssetManagement.controler.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.UserLoginRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.SuccessResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.UserLoginResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.User;
import com.nashtech.rookies.java05.AssetManagement.exceptions.UnauthorizedException;
import com.nashtech.rookies.java05.AssetManagement.mappers.UserMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.UserRepository;
import com.nashtech.rookies.java05.AssetManagement.controler.services.AuthService;
import com.nashtech.rookies.java05.AssetManagement.utils.JwtUtil;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Builder
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public SuccessResponse<UserLoginResponseDto> loginHandler(UserLoginRequestDto userLoginRequestDto) {
        Optional<User> userFound = userRepository.findUsersByUsername(userLoginRequestDto.getUsername());
        if (userFound.isPresent()) {
            User user = userFound.get();
            if (!user.isDisabled()) {
                    if (passwordEncoder.matches(userLoginRequestDto.getPassword(), user.getPassword())) {
                        UserLoginResponseDto userLoginResponseDto = userMapper.mapUserEntityToUserLoginResponse(user);
                        userLoginResponseDto.setAccessToken(jwtUtil.generateToken(user));
                        return new SuccessResponse<>(userLoginResponseDto);
                    }
            }
        }
        throw new UnauthorizedException("Username or password is incorrect!");
    }
}
