package com.nashtech.rookies.java05.AssetManagement.mappers;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.UserRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.UserResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.UserViewResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Location;
import com.nashtech.rookies.java05.AssetManagement.entities.Role;
import com.nashtech.rookies.java05.AssetManagement.entities.User;
import com.nashtech.rookies.java05.AssetManagement.repository.LocationRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class UserMapper {

    public List<UserViewResponseDto> mapUserEntityListToUserViewResponseDtoList(List<User> userList) {
        List<UserViewResponseDto> userViewResponseDtoList = new ArrayList<>();
        userList.forEach(user -> {
            UserViewResponseDto result = UserViewResponseDto
                    .builder()
                    .id(user.getId())
                    .joinedDate(user.getJoinedDate())
                    .fullName(user.getFirstName() + " " + user.getLastName())
                    .userName(user.getUsername())
                    .role(user.getRole().getName())
                    .build();
            userViewResponseDtoList.add(result);
        });
        return userViewResponseDtoList;
    }

    public static User mapFromUserRequestDtoToEntity(UserRequestDto userRequestDto) throws ParseException {
        User user=new User();
        SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
        Date birth=format.parse(userRequestDto.getBirth());
        Date joinedDate=format.parse(userRequestDto.getJoinedDate());
        Date date=new Date();
        Timestamp timestamp=new Timestamp(date.getTime());


        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setCreatedWhen(timestamp);
        user.setUpdatedWhen(timestamp);
        user.setGender(userRequestDto.getGender());
        user.setBirth(birth);
        user.setJoinedDate(joinedDate);
        return user;
    }

    public static UserResponseDto mapFromEntityToUserResponseDto(User user){
        UserResponseDto userResponseDto=new UserResponseDto();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");

        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setGender(user.getGender());
        userResponseDto.setBirth(format.format(user.getBirth()));
        userResponseDto.setCreatedWhen(user.getCreatedWhen());
        userResponseDto.setUpdatedWhen(user.getUpdatedWhen());
        userResponseDto.setJoinedDate(format.format(user.getJoinedDate()));
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setId(user.getId());
        userResponseDto.setRole(user.getRole());
        userResponseDto.setLocation(user.getLocation());

        return userResponseDto;
    }
}
