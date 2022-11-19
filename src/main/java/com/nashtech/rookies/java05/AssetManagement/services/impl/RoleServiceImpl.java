package com.nashtech.rookies.java05.AssetManagement.services.impl;


import com.nashtech.rookies.java05.AssetManagement.entities.Role;
import com.nashtech.rookies.java05.AssetManagement.repository.RoleRepository;
import com.nashtech.rookies.java05.AssetManagement.services.RoleService;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Builder
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public List<String> getAllRoleNames() {
        List<Role> roleList = roleRepository.findAll();
        List<String> result = new ArrayList<>();
        if (roleList.isEmpty()){
            return new ArrayList<>();
        }else{
            roleList.forEach(role -> {
                result.add(role.getName());
            });
        }
        return result;
    }
}
