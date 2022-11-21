package com.nashtech.rookies.java05.AssetManagement.services;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.UserRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.UserResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.UserViewResponseDto;

import java.text.ParseException;
import java.util.List;

public interface UserService {

    APIResponse<List<UserViewResponseDto>> getUsersByPredicates(List<String> types, String keyword, int locationId, int page, String orderBy);

    public UserResponseDto createUser(UserRequestDto userDto) throws ParseException;

    public UserResponseDto getUserById(String id);

    public UserResponseDto updateUser(String id, UserRequestDto userRequestDto) throws ParseException;

}

