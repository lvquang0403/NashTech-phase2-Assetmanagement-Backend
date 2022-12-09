package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.LocationResponseDto;
import com.nashtech.rookies.java05.AssetManagement.repository.LocationRepository;
import com.nashtech.rookies.java05.AssetManagement.services.LocationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    LocationRepository locationRepository;

    @Override
    public List<LocationResponseDto> getLocations() {
        return locationRepository.findAll().stream().map(location -> modelMapper.map(location, LocationResponseDto.class)).collect(Collectors.toList());
    }
}
