package com.nashtech.rookies.java05.AssetManagement.services;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssetRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetResponseInsertDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetViewResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AssetService {

    public AssetResponseInsertDto insert(AssetRequestDto dto);


    APIResponse<List<AssetViewResponseDto>> getAssetsByPredicates
            (List<AssetState> states, List<String> categoryNames, String keyword, int locationId, int page);

    List<String> getAllAssetStates();
    AssetResponseDto getAssetById(String id);
}
