package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.UserResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.UserViewResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Role;
import com.nashtech.rookies.java05.AssetManagement.entities.User;
import com.nashtech.rookies.java05.AssetManagement.mappers.UserMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.RoleRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.UserRepository;
import com.nashtech.rookies.java05.AssetManagement.services.UserService;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Builder
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private static final int pageSize = 15;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserMapper userMapper;

    @Override
    public APIResponse<List<UserViewResponseDto>> getUsersByPredicates(List<String> types, String keyword, int locationId, int page) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "updatedWhen");
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
    public UserResponseDto getUserById(String id) {
        Optional<User> userFound = userRepository.findById(id);
        if(userFound.isEmpty()){
            return UserResponseDto.builder().build();
        }
        return userMapper.mapUserEntityToUserResponseDto(userFound.get());
    }
}
