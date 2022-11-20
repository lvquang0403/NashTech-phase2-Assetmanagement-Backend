package com.nashtech.rookies.java05.AssetManagement.repository;

import com.nashtech.rookies.java05.AssetManagement.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    List<Role> findByNameIsIn(List<String> roles);
}
