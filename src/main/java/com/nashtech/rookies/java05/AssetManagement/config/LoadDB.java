package com.nashtech.rookies.java05.AssetManagement.config;

import com.nashtech.rookies.java05.AssetManagement.entities.Location;
import com.nashtech.rookies.java05.AssetManagement.entities.Role;
import com.nashtech.rookies.java05.AssetManagement.repository.LocationRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class LoadDB{

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    LocationRepository locationRepository;

    @Bean
    CommandLineRunner initDataBase(){
        return args ->{
            String[] listRoles={"ADMIN","STAFF"};
            String[] listLocations={"HN","HCM","DN"};

            for (String roleName:listRoles){
                roleRepository.save(new Role(roleName));
            }
            for (String cityName:listLocations){
                locationRepository.save(new Location(cityName));
            }
        };
    }
}
