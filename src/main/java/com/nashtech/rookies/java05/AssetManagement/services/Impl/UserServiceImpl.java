package com.nashtech.rookies.java05.AssetManagement.services.Impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.UserRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.UserResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Location;
import com.nashtech.rookies.java05.AssetManagement.entities.Role;
import com.nashtech.rookies.java05.AssetManagement.entities.User;
import com.nashtech.rookies.java05.AssetManagement.mappers.UserMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.LocationRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.RoleRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.UserRepository;
import com.nashtech.rookies.java05.AssetManagement.services.UserService;
import com.nashtech.rookies.java05.AssetManagement.utils.UserUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    UserUtils userUtils;

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) throws ParseException {
        User user= UserMapper.mapFromUserRequestDtoToEntity(userRequestDto);

        Integer maxCode=userUtils.getMaxCode();
        String id="SD"+String.format("%04d",maxCode);

        String prefixUsername=userUtils.getPrefixUsername(userRequestDto.getFirstName(),userRequestDto.getLastName());
        Integer maxCodeUsername=userUtils.getMaxUsernameCode(prefixUsername);
        String username=prefixUsername+maxCodeUsername;

        SimpleDateFormat format=new SimpleDateFormat("ddMMyyyy");
        String password=prefixUsername+"@"+format.format(user.getBirth());

        Role role= roleRepository.findById(userRequestDto.getRoleId()).get();
        Location location= locationRepository.findById(userRequestDto.getLocationId()).get();

        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        user.setLocation(location);

        userRepository.save(user);

        return UserMapper.mapFromEntityToUserResponseDto(user);
    }

    public UserResponseDto updateUser(String id,UserRequestDto userRequestDto) throws ParseException {
        Optional<User> option=userRepository.findById(id);
        if (option.isEmpty())   return null;
        User user=option.get();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date birth=format.parse(userRequestDto.getBirth());
        Date joinedDate=format.parse(userRequestDto.getJoinedDate());

        Role role = roleRepository.findById(userRequestDto.getRoleId()).get();
        Location location=locationRepository.findById(userRequestDto.getLocationId()).get();

        user.setGender(userRequestDto.getGender());
        user.setBirth(birth);
        user.setJoinedDate(joinedDate);
        user.setRole(role);
        user.setLocation(location);
        userRepository.save(user);

        return UserMapper.mapFromEntityToUserResponseDto(user);
    }
}
