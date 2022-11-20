package com.nashtech.rookies.java05.AssetManagement.services.Impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.RoleResponeDto;
import com.nashtech.rookies.java05.AssetManagement.repository.RoleRepository;
import com.nashtech.rookies.java05.AssetManagement.services.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<RoleResponeDto> getAllRoles() {
        return roleRepository.findAll().stream().map((role)->modelMapper.map(role,RoleResponeDto.class)).collect(Collectors.toList());
    }
}
