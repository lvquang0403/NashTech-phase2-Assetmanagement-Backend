package com.nashtech.rookies.java05.AssetManagement.services;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssetRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetResponseInsertDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetViewResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AssetService {

    public AssetResponseInsertDto insert(AssetRequestDto dto);

    List<AssetViewResponseDto> getListAssets();


    APIResponse<List<AssetViewResponseDto>> getAssetsByPredicates
            (List<AssetState> states, List<String> categoryNames, String keyword, int page);

}
