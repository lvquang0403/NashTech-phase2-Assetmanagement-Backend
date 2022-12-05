package com.nashtech.rookies.java05.AssetManagement.controler.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.RoleResponseDto;
import com.nashtech.rookies.java05.AssetManagement.repository.RoleRepository;
import com.nashtech.rookies.java05.AssetManagement.controler.services.RoleService;
import lombok.Builder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<RoleResponseDto> getAllRoles() {
        return roleRepository.findAll().stream().map((role)->modelMapper.map(role, RoleResponseDto.class)).collect(Collectors.toList());
    }
}
