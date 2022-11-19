package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssetRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetResponseInsertDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetViewResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Asset;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import com.nashtech.rookies.java05.AssetManagement.mappers.AssetMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.AssetRepository;
import com.nashtech.rookies.java05.AssetManagement.services.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {

    @Autowired
    private AssetRepository repository;
    @Autowired
    private AssetMapper mapper;

    @Override
    public AssetResponseInsertDto insert(AssetRequestDto dto){
        dto.validateInsert();
        Asset asset = mapper.mapAssetRequestDtoToEntityInsert(dto);
        Asset newAsset = repository.save(asset);
        return mapper.mapEntityInsertToAssetResponseInsertDto(newAsset);
    }

    @Override
    public List<AssetViewResponseDto> getListAssets() {
        return null;
    }

    @Override
    public APIResponse<List<AssetViewResponseDto>> getAssetsByPredicates(List<AssetState> states, List<String> categoryNames, String keyword, int page) {
        return null;
    }
}
