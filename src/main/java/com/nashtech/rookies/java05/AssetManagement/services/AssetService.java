package com.nashtech.rookies.java05.AssetManagement.services;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssetRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetViewResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public interface AssetService {

    public AssetResponseDto createAsset(AssetRequestDto dto);

    public AssetResponseDto updateAsset(AssetRequestDto dto, String id);


    APIResponse<List<AssetViewResponseDto>> getAssetsByPredicates
            (List<String> stateFilterList, List<String> categoryNames, String keyword, int locationId, int page, String orderBy);

    Set<String> getAllAssetStates();
    AssetResponseDto getAssetById(String id);

    boolean deleteAssetById(String id);

    boolean checkAssetValidToDelete(String id);
}
