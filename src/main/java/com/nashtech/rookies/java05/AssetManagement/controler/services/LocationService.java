package com.nashtech.rookies.java05.AssetManagement.controler.services;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.LocationResponseDto;

import java.util.List;

public interface LocationService {
    public List<LocationResponseDto> getAllLocations();
}
