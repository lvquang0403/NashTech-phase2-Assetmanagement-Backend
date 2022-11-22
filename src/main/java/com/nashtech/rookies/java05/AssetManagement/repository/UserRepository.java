package com.nashtech.rookies.java05.AssetManagement.repository;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.UserResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Role;
import com.nashtech.rookies.java05.AssetManagement.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "SELECT u FROM User u join u.role" +
            " WHERE (u.role IN :roles OR :roles is null)" +
            " AND (LOWER(u.lastName) LIKE %:keyword% " +
            "OR LOWER(u.firstName) LIKE %:keyword% " +
            "OR CONCAT(LOWER(u.firstName), ' ', LOWER(u.lastName))  LIKE %:keyword% " +
            "OR LOWER(u.id) LIKE %:keyword%)" +
            " AND u.location.id = :locationId")
    Page<User> findUsersWithFilter(
            @Param("roles") List<Role> roles,
            @Param("keyword") String keyword,
            @Param("locationId") int locationId,
            Pageable pageable
    );

}
