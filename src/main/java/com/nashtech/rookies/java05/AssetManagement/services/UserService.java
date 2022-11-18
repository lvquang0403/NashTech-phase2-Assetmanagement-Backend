package com.nashtech.rookies.java05.AssetManagement.services;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.UserRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

public interface UserService {

    public UserResponseDto createUser(UserRequestDto userDto) throws ParseException;

    public UserResponseDto getUserById(String id);

    public UserResponseDto updateUser(String id, UserRequestDto userRequestDto) throws ParseException;

    public List<UserResponseDto> getAllUsers();
}
