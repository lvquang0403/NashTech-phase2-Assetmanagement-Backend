package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.UserLoginRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.SuccessResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.UserLoginResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.User;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.UserState;
import com.nashtech.rookies.java05.AssetManagement.exceptions.UnauthorizedException;
import com.nashtech.rookies.java05.AssetManagement.mappers.UserMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.UserRepository;
import com.nashtech.rookies.java05.AssetManagement.utils.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceImplTest {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private JwtUtil jwtUtil;
    private PasswordEncoder passwordEncoder;
    private AuthServiceImpl authService;
    private UserLoginRequestDto userLoginRequestDto;
    @BeforeEach
    void beforeEach(){
        userRepository = mock(UserRepository.class);
        userMapper = mock(UserMapper.class);
        jwtUtil = mock(JwtUtil.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userLoginRequestDto = mock(UserLoginRequestDto.class);
        authService = AuthServiceImpl
                .builder()
                .jwtUtil(jwtUtil)
                .passwordEncoder(passwordEncoder)
                .userMapper(userMapper)
                .userRepository(userRepository)
                .build();
    }

    @Test
    void loginHandler_WhenDataIsValid(){

        User user = new User();
        user.setState(UserState.ACTIVE);
        user.setDisabled(false);
        String accessToken = "";
        UserLoginResponseDto userLoginResponseDto = UserLoginResponseDto
                .builder()
                .State(UserState.ACTIVE.getName())
                .build();
        when(userRepository.findUsersByUsername(userLoginRequestDto.getUsername()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(userLoginRequestDto.getPassword(), user.getPassword())).thenReturn(true);
        when(userMapper.mapUserEntityToUserLoginResponse(user)).thenReturn(userLoginResponseDto);
        when(jwtUtil.generateToken(user)).thenReturn(accessToken);

        SuccessResponse expected = new SuccessResponse<>(userLoginResponseDto);
        SuccessResponse result = authService.loginHandler(userLoginRequestDto);
        assertThat(expected, is(result));
    }

    @Test
    void loginHandler_ShouldReturnUnauthorizedException_WhenUserNameIsIncorrect(){
        UnauthorizedException thrown = assertThrows(UnauthorizedException.class,
                () -> {
                    when(userRepository.findUsersByUsername(userLoginRequestDto.getUsername()))
                            .thenReturn(Optional.empty());
                    authService.loginHandler(userLoginRequestDto);
                });
        Assertions.assertEquals("Username or password is incorrect!", thrown.getMessage());
    }

    @Test
    void loginHandler_ShouldReturnUnauthorizedException_WhenUserIsDisable(){
        User user = new User();
        user.setDisabled(true);
        UnauthorizedException thrown = assertThrows(UnauthorizedException.class,
                () -> {
                    when(userRepository.findUsersByUsername(userLoginRequestDto.getUsername()))
                            .thenReturn(Optional.of(user));
                    //Because user is disabled, so it will not run passwordEncoder check.
                    when(passwordEncoder.matches(userLoginRequestDto.getPassword(), user.getPassword())).thenReturn(true);
                    authService.loginHandler(userLoginRequestDto);
                });
        Assertions.assertEquals("Username or password is incorrect!", thrown.getMessage());
    }

    @Test
    void loginHandler_ShouldReturnUnauthorizedException_WhenPasswordIsIncorrect(){
        User user = new User();
        user.setDisabled(false);
        UnauthorizedException thrown = assertThrows(UnauthorizedException.class,
                () -> {
                    when(userRepository.findUsersByUsername(userLoginRequestDto.getUsername()))
                            .thenReturn(Optional.of(user));
                    when(passwordEncoder.matches(userLoginRequestDto.getPassword(), user.getPassword())).thenReturn(false);
                    authService.loginHandler(userLoginRequestDto);
                });
        Assertions.assertEquals("Username or password is incorrect!", thrown.getMessage());
    }


}