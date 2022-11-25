package com.nashtech.rookies.java05.AssetManagement.mappers;

import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssetRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetResponseInsertDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetViewResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Asset;
import com.nashtech.rookies.java05.AssetManagement.entities.Category;
import com.nashtech.rookies.java05.AssetManagement.entities.Location;
import com.nashtech.rookies.java05.AssetManagement.entities.PresentId;
import com.nashtech.rookies.java05.AssetManagement.exceptions.ResourceNotFoundException;
import com.nashtech.rookies.java05.AssetManagement.repository.AssetRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.CategoryRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.LocationRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.PresentIdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AssetMapper {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private PresentIdRepository presentIdRepository;
    @Autowired
    private ReturningMapper returningMapper;
    @Autowired
    private AssetRepository assetRepository;


    public Asset mapAssetRequestDtoToEntityInsert(AssetRequestDto dto) {
        Date dateNow = new Date();
        Timestamp now = new Timestamp(dateNow.getTime());
        PresentId presentId;
        Optional<Category> optionalCategory = categoryRepository.findById(dto.getCategoryId());
        Optional<Location> optionalLocation = locationRepository.findById(dto.getLocationId());
        Optional<PresentId> optionalPresentId = presentIdRepository.findById("count");
        if (optionalCategory.isEmpty()) {
            throw new ResourceNotFoundException("Category not exist");
        }
        if (optionalLocation.isEmpty()) {
            throw new ResourceNotFoundException("Location not exist");
        }
        if (optionalPresentId.isEmpty()) {
            presentId = new PresentId("count",1,1);
        }else{
            presentId = optionalPresentId.get();
        }

        Integer count = presentId.getAssetId();
//        create id
        String id= "";
        if(presentId.getAssetId()<10){
            id = optionalCategory.get().getId()+"00000"+presentId.getAssetId();
        }else if(count < 100){
            id = optionalCategory.get().getId()+"0000"+count;
        }else if(count < 1000){
            id = optionalCategory.get().getId()+"000"+count;
        }else if(count < 10000){
            id = optionalCategory.get().getId()+"00"+count;
        }else if(count < 100000){
            id = optionalCategory.get().getId()+"0"+count;
        }else if(count < 1000000){
            id = optionalCategory.get().getId()+count;
        }else{
            throw new IllegalArgumentException("Asset warehouse is full");
        }
        presentId.setAssetId(presentId.getAssetId()+1);
        presentIdRepository.save(presentId);
        return Asset.builder()
                .id(id)
                .name(dto.getName())
                .specification(dto.getSpecification())
                .createdWhen(now)
                .updatedWhen(now)
                .installedDate(dto.getInstalledDate())
                .category(optionalCategory.get())
                .state(dto.getState())
                .location(optionalLocation.get())
                .build();
    }

    public AssetResponseInsertDto mapEntityInsertToAssetResponseInsertDto(Asset asset) {

        AssetResponseInsertDto result = AssetResponseInsertDto.builder()
                .id(asset.getId())
                .name(asset.getName())
                .specification(asset.getSpecification())
                .state(asset.getState())
                .createdWhen(asset.getCreatedWhen())
                .updatedWhen(asset.getUpdatedWhen())
                .installedDate(asset.getInstalledDate())
                .build();
        result.setCategoryDto(asset.getCategory());
        result.setLocationDto(asset.getLocation());
        return result;
    }

    public Asset mapAssetRequestDtoToEntityUpdate(AssetRequestDto dto, Asset oldAsset) {
        Date dateNow = new Date();
        Timestamp now = new Timestamp(dateNow.getTime());
        oldAsset.setState(dto.getState());
        oldAsset.setName(dto.getName());
        oldAsset.setSpecification(dto.getSpecification());
        oldAsset.setInstalledDate(dto.getInstalledDate());
        oldAsset.setUpdatedWhen(now);

        return oldAsset;
    }

    public AssetResponseDto mapAssetEntityToAssetResponseDto(Asset asset){
        AssetResponseDto assetResponseDto = AssetResponseDto.builder()
                .id(asset.getId())
                .name(asset.getName())
                .categoryName(asset.getCategory().getName())
                .location(asset.getLocation().getCityName())
                .specification(asset.getSpecification())
                .createdWhen(asset.getCreatedWhen())
                .updatedWhen(asset.getUpdatedWhen())
                .installedDate(asset.getInstalledDate())
                .state(asset.getState().getName())
                .returningDtoList(returningMapper.mapReturningEntityToReturningDto(asset.getReturningList()))
                .build();
        return assetResponseDto;
    }

    public List<AssetViewResponseDto> mapAssetListToAssetViewResponseDto(List<Asset> assetList) {
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


}
