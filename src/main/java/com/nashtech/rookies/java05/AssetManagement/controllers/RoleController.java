package com.nashtech.rookies.java05.AssetManagement.controllers;

import com.nashtech.rookies.java05.AssetManagement.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/roles")
public class RoleController {
    @Autowired
    RoleService roleService;

    @GetMapping
    public ResponseEntity getRoles(){
        return ResponseEntity.ok(roleService.getRoles());
    }
}
