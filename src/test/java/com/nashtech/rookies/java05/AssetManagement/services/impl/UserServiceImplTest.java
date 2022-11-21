package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.UserRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.UserResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.UserViewResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Role;
import com.nashtech.rookies.java05.AssetManagement.entities.User;
import com.nashtech.rookies.java05.AssetManagement.mappers.UserMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.RoleRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    private static final int pageSize = 15;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserMapper userMapper;
    private UserServiceImpl userService;
    private User user;
    private Role role;

    @Mock
    List<UserViewResponseDto> userViewResponseDtoList;


    @BeforeEach
    void beforeEach() {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        userMapper = mock(UserMapper.class);
        userService = UserServiceImpl.builder()
                .userMapper(userMapper)
                .userRepository(userRepository)
                .roleRepository(roleRepository)
                .build();

        user = mock(User.class);
        role = mock(Role.class);
    }

    @Test
    void getUsersByPredicates_WhenTypesIsNotNull() {
        List<Role> roleList = new ArrayList<>();
        List<String> types = new ArrayList<>();
        String keyword = "";
        int locationId = 0;
        int page = 0;
        String orderBy = "updatedWhen_DESC";
        roleList.add(role);
        Page<User> result = mock(Page.class);
        APIResponse<List<UserViewResponseDto>> expected = new APIResponse<>(page, userViewResponseDtoList);


        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "updatedWhen");
        when(roleRepository.findByNameIsIn(types)).thenReturn(roleList);
        when(userRepository.findUsersWithFilter(roleList, keyword.toLowerCase(), locationId, pageable)).thenReturn(result);
        when(userMapper.mapUserEntityListToUserViewResponseDtoList(result.toList())).thenReturn(userViewResponseDtoList);
        APIResponse<List<UserViewResponseDto>> returnList = userService.getUsersByPredicates
                (types, keyword,locationId,page,orderBy);

        assertThat(expected, is(returnList));
    }

    @Test
    void getUsersByPredicates_WhenTypesIsNull() {
        List<Role> roleList = new ArrayList<>();
        List<String> types = new ArrayList<>();
        String keyword = "";
        int locationId = 0;
        int page = 0;
        String orderBy = "updatedWhen_DESC";

        roleList.add(role);
        Page<User> result = mock(Page.class);
        APIResponse<List<UserViewResponseDto>> expected = new APIResponse<>(page, userViewResponseDtoList);


        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "updatedWhen");
        when(roleRepository.findAll()).thenReturn(roleList);
        when(userRepository.findUsersWithFilter(roleList, keyword.toLowerCase(), locationId, pageable)).thenReturn(result);
        when(userMapper.mapUserEntityListToUserViewResponseDtoList(result.toList())).thenReturn(userViewResponseDtoList);
        APIResponse<List<UserViewResponseDto>> returnList = userService.getUsersByPredicates
                (null, keyword,locationId,page,orderBy);

        assertThat(expected, is(returnList));
    }

}