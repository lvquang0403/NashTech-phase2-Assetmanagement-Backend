package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.UserLoginRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.SuccessResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.UserLoginResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.User;
import com.nashtech.rookies.java05.AssetManagement.exceptions.ForbiddenException;
import com.nashtech.rookies.java05.AssetManagement.mappers.UserMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.UserRepository;
import com.nashtech.rookies.java05.AssetManagement.services.AuthService;
import com.nashtech.rookies.java05.AssetManagement.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
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
//                if (user.getState() == null) { //Login first time!
//                    if (user.getPassword().equals(userLoginRequestDto.getPassword())) {
//                        UserLoginResponseDto userLoginResponseDto = userMapper.mapUserEntityToUserLoginResponse(user);
//                        userLoginResponseDto.setAccessToken(jwtUtil.generateToken(user));
//                        return new SuccessResponse<>("Login successfully!", userLoginResponseDto);
//                    }
//                } else {
                if (passwordEncoder.matches(userLoginRequestDto.getPassword(), user.getPassword())) {
                    UserLoginResponseDto userLoginResponseDto = userMapper.mapUserEntityToUserLoginResponse(user);
                    userLoginResponseDto.setAccessToken(jwtUtil.generateToken(user));
                    return new SuccessResponse<>("Login successfully!", userLoginResponseDto);
                }
//                }
            }
        }
        throw new ForbiddenException("Username or password is incorrect!");
    }
}
