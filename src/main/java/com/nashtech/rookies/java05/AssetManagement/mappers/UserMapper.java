package com.nashtech.rookies.java05.AssetManagement.mappers;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.UserViewResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {
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


}
