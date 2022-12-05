package com.nashtech.rookies.java05.AssetManagement.controler.services;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.RoleResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface RoleService {
    public List<RoleResponseDto> getAllRoles();
}
