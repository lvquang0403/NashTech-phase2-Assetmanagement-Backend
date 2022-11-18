package com.nashtech.rookies.java05.AssetManagement.controler;

import com.nashtech.rookies.java05.AssetManagement.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping()
    public List<String> getAllRoleNames(){
        return roleService.getAllRoleNames();
    }
}
