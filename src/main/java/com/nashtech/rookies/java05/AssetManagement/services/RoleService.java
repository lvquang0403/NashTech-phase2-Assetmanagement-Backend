package com.nashtech.rookies.java05.AssetManagement.services;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.RoleResponeDto;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface RoleService {
    List<String> getAllRoleNames();
    public List<RoleResponeDto> getAllRoles();
}
