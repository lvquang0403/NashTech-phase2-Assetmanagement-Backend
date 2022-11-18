package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.entities.Role;
import com.nashtech.rookies.java05.AssetManagement.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

class RoleServiceImplTest {

    private RoleRepository roleRepository;
    private RoleServiceImpl roleService;
    private Role role;

    @BeforeEach
    void beforeEach() {
        roleRepository = mock(RoleRepository.class);
        role = mock(Role.class);
        roleService = RoleServiceImpl.builder().roleRepository(roleRepository).build();
    }

    @Test
    void getAllRoleNames_WhenRoleIsNotEmpty() {
        List<Role> roleList = new ArrayList<>();
        roleList.add(role);
        List<String> stringList = new ArrayList<>();
        stringList.add(role.getName());
        when(roleRepository.findAll()).thenReturn(roleList);
        List<String> result = roleService.getAllRoleNames();
        assertThat(result, is(stringList));
    }
}