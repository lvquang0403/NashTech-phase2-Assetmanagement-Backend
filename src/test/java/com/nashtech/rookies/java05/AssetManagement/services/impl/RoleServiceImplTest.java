package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.entities.Role;
import com.nashtech.rookies.java05.AssetManagement.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

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
}