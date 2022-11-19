package com.nashtech.rookies.java05.AssetManagement.mappers;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetViewResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Asset;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AssetMapper {
    @Autowired
    private ModelMapper modelMapper;


    public List<AssetViewResponseDto> mapAssetListToAssetViewResponseDto(List<Asset> assetList) {
        List<AssetViewResponseDto> result = new ArrayList<>();
        assetList.forEach(asset -> {

            AssetViewResponseDto assetViewResponseDto = AssetViewResponseDto.builder()
                    .id(asset.getId())
                    .name(asset.getName())
                    .state(asset.getState())
                    .category(asset.getCategory().getName())
                    .build();

            result.add(assetViewResponseDto);
        } );
        return result;
    }
}
