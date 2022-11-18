package com.nashtech.rookies.java05.AssetManagement.mappers;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetViewResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Asset;
import com.nashtech.rookies.java05.AssetManagement.repository.AssetRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AssetMapper {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private ReturningMapper returningMapper;
    public AssetResponseDto mapAssetEntityToAssetResponseDto(Asset asset){
        AssetResponseDto assetResponseDto = AssetResponseDto.builder()
                .id(asset.getId())
                .name(asset.getName())
                .categoryName(asset.getCategory().getName())
                .location(asset.getLocation().getCityName())
                .specification(asset.getSpecification())
                .createdWhen(asset.getCreatedWhen())
                .updatedWhen(asset.getUpdatedWhen())
                .state(asset.getState())
                .returningDtoList(returningMapper.mapReturningEntityToReturningDto(asset.getReturningList()))
                .build();
        return assetResponseDto;
    }

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
