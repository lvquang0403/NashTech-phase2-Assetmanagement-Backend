package com.nashtech.rookies.java05.AssetManagement.mappers;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssetRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetViewResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Asset;
import com.nashtech.rookies.java05.AssetManagement.entities.Category;
import com.nashtech.rookies.java05.AssetManagement.entities.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AssetMapper {
    @Autowired
    private ReturningMapper returningMapper;


    public Asset toEntityCreate(String id, AssetRequestDto dto, Category  category, Location location) {
        Date dateNow = new Date();
        Timestamp now = new Timestamp(dateNow.getTime());
        return Asset.builder()
                .id(id)
                .name(dto.getName().trim())
                .specification(dto.getSpecification())
                .createdWhen(now)
                .updatedWhen(now)
                .installedDate(dto.getInstalledDate())
                .category(category)
                .state(dto.getState())
                .location(location)
                .build();
    }

    public AssetResponseDto toDto(Asset asset) {
        AssetResponseDto assetResponseDto = AssetResponseDto.builder()
                .id(asset.getId())
                .name(asset.getName())
                .specification(asset.getSpecification())
                .state(asset.getState().getName())
                .createdWhen(asset.getCreatedWhen())
                .updatedWhen(asset.getUpdatedWhen())
                .installedDate(asset.getInstalledDate())
                .categoryName(asset.getCategory().getName())
                .location(asset.getLocation().getCityName())
                .build();
        if (asset.getReturningList() != null) {
            assetResponseDto.setReturningDtoList(returningMapper.toDtoList(asset.getReturningList()));
        }
        return assetResponseDto;
    }

    public Asset toEntityUpdate(AssetRequestDto dto, Asset oldAsset) {
        Date dateNow = new Date();
        Timestamp now = new Timestamp(dateNow.getTime());
        oldAsset.setState(dto.getState());
        oldAsset.setName(dto.getName().trim());
        oldAsset.setSpecification(dto.getSpecification());
        oldAsset.setInstalledDate(dto.getInstalledDate());
        oldAsset.setUpdatedWhen(now);

        return oldAsset;
    }

    public List<AssetViewResponseDto> toDtoViewList(List<Asset> assetList) {
        List<AssetViewResponseDto> assetViewResponseDtoList = assetList.stream().map(asset -> {
            AssetViewResponseDto assetViewResponseDto = AssetViewResponseDto.builder()
                    .id(asset.getId())
                    .name(asset.getName())
                    .state(asset.getState().getName())
                    .category(asset.getCategory().getName())
                    .build();
            return assetViewResponseDto;
        }).collect(Collectors.toList());
        System.out.println(assetViewResponseDtoList);
        return assetViewResponseDtoList;
    }

    public AssetViewResponseDto toDtoView(Asset asset) {
        AssetViewResponseDto assetViewResponseDtoList = AssetViewResponseDto
                .builder()
                .category(asset.getCategory().getName())
                .state(asset.getState().getName())
                .name(asset.getName())
                .id(asset.getId())
                .build();

        return assetViewResponseDtoList;
    }

}
