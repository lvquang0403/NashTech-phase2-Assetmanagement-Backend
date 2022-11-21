package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.UserRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.UserResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.UserViewResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Location;
import com.nashtech.rookies.java05.AssetManagement.entities.Role;
import com.nashtech.rookies.java05.AssetManagement.entities.User;
import com.nashtech.rookies.java05.AssetManagement.mappers.UserMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.LocationRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.RoleRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.UserRepository;
import com.nashtech.rookies.java05.AssetManagement.services.UserService;
import com.nashtech.rookies.java05.AssetManagement.utils.UserUtils;
import lombok.Builder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Builder
@Service
public class UserServiceImpl implements UserService {
    private static final int pageSize = 15;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    UserUtils userUtils;


    @Override
    public UserResponseDto getUserById(String id) {
        Optional<User> option = userRepository.findById(id);
        if (option.isEmpty()) return null;
        return UserMapper.mapFromEntityToUserResponseDto(option.get());
    }

    @Override
    public APIResponse<List<UserViewResponseDto>> getUsersByPredicates(List<String> types, String keyword, int locationId, int page, String orderBy) {

        String[] parts = orderBy.split("_");
        String columnName = parts[0];
        String order = parts[1];

        Pageable pageable;
        if("DESC".equals(order)){
            pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC , columnName);
        }else{
            pageable = PageRequest.of(page, pageSize, Sort.Direction.ASC , columnName);
        }

        List<Role> roleList;
        if (types == null) {
            roleList = roleRepository.findAll();
        } else {
            roleList = roleRepository.findByNameIsIn(types);
        }
        Page<User> result;

        result = userRepository.findUsersWithFilter(roleList, keyword.toLowerCase(), locationId, pageable);
        return new APIResponse<>(result.getTotalPages(), userMapper.mapUserEntityListToUserViewResponseDtoList(result.toList()));
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) throws ParseException {
        User user = UserMapper.mapFromUserRequestDtoToEntity(userRequestDto);

        Integer maxCode = userUtils.getMaxCode();
        String id = "SD" + String.format("%04d", maxCode);

        String prefixUsername = userUtils.getPrefixUsername(userRequestDto.getFirstName(), userRequestDto.getLastName());
        Integer maxCodeUsername = userUtils.getMaxUsernameCode(prefixUsername);
        String username = prefixUsername + maxCodeUsername;

        SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
        String password = prefixUsername + "@" + format.format(user.getBirth());

        Role role = roleRepository.findById(userRequestDto.getRoleId()).get();
        Location location = locationRepository.findById(userRequestDto.getLocationId()).get();

        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        user.setLocation(location);

        userRepository.save(user);

        return UserMapper.mapFromEntityToUserResponseDto(user);
    }

    public UserResponseDto updateUser(String id, UserRequestDto userRequestDto) throws ParseException {
        Optional<User> option = userRepository.findById(id);
        if (option.isEmpty()) return null;
        User user = option.get();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date birth = format.parse(userRequestDto.getBirth());
        Date joinedDate = format.parse(userRequestDto.getJoinedDate());
        Date date=new Date();

        Role role = roleRepository.findById(userRequestDto.getRoleId()).get();
//        Location location=locationRepository.findById(userRequestDto.getLocationId()).get();

        user.setGender(userRequestDto.getGender());
        user.setBirth(birth);
        user.setJoinedDate(joinedDate);
        user.setRole(role);
        user.setUpdatedWhen(new Timestamp(date.getTime()));
        userRepository.save(user);

        return UserMapper.mapFromEntityToUserResponseDto(user);
    }
}