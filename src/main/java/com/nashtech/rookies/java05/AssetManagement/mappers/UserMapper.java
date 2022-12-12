package com.nashtech.rookies.java05.AssetManagement.mappers;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.UserRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.*;
import com.nashtech.rookies.java05.AssetManagement.entities.User;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public List<UserViewResponseDto> toDtoViewList(List<User> userList) {

        List<UserViewResponseDto> responseDtoList = userList.stream().map(user -> {
            UserViewResponseDto result = UserViewResponseDto
                    .builder()
                    .id(user.getId())
                    .joinedDate(user.getJoinedDate())
                    .fullName(user.getFirstName() + " " + user.getLastName())
                    .userName(user.getUsername())
                    .role(user.getRole().getName())
                    .build();
            return result;
        }).collect(Collectors.toList());
        return responseDtoList;
    }

    public UserLoginResponseDto toLoginDto(User user){
        String state = "";
        if(user.getState()!=null){
            state = user.getState().getName();
        }
        UserLoginResponseDto responseDto = UserLoginResponseDto
                .builder()
                .id(user.getId())
                .joinedDate(user.getJoinedDate())
                .role(RoleResponseDto.builder().id(user.getRole().getId()).name(user.getRole().getName()).build())
                .birth(user.getBirth())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .gender(user.getGender().getName())
                .location(LocationResponseDto.builder().cityName(user.getLocation().getCityName()).id(user.getLocation().getId()).build())
                .status(state)
                .build();
        return responseDto;
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
        userResponseDto.setGender(user.getGender().getName());
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
