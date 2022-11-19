package com.nashtech.rookies.java05.AssetManagement.mappers;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.UserResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.UserViewResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.User;
import org.springframework.stereotype.Component;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {
    Format formatter = new SimpleDateFormat("yyyy-MM-dd");


    public List<UserViewResponseDto> mapUserEntityListToUserViewResponseDtoList(List<User> userList) {
        List<UserViewResponseDto> userViewResponseDtoList = new ArrayList<>();
        userList.forEach(user -> {
            UserViewResponseDto result = UserViewResponseDto
                    .builder()
                    .id(user.getId())
                    .createdWhen(user.getCreatedWhen())
                    .fullName(user.getFirstName() + " " + user.getLastName())
                    .userName(user.getUsername())
                    .role(user.getRole().getName())
                    .build();
            userViewResponseDtoList.add(result);
        });
        return userViewResponseDtoList;
    }

    public UserResponseDto mapUserEntityToUserResponseDto(User user){
        UserResponseDto result = UserResponseDto.builder()
                .gender(user.getGender())
                .id(user.getId())
                .role(user.getRole().getName())
                .location(user.getLocation().getCityName())
                .username(user.getUsername())
                .birth(user.getBirth())
                .createdWhen(user.getCreatedWhen())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .joinedDate(formatter.format(user.getJoinedDate()))
                .build();
        return  result;
    }

}
