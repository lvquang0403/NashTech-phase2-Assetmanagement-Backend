package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.services.AssetService;
import com.nashtech.rookies.java05.AssetManagement.dtos.request.AssetRequestDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetResponseInsertDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetViewResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Asset;
import com.nashtech.rookies.java05.AssetManagement.entities.Assignment;
import com.nashtech.rookies.java05.AssetManagement.entities.Category;
import com.nashtech.rookies.java05.AssetManagement.entities.Returning;

import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentReturnState;
import com.nashtech.rookies.java05.AssetManagement.exceptions.ResourceNotFoundException;
import com.nashtech.rookies.java05.AssetManagement.mappers.AssetMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.AssetRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.CategoryRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.ReturningRepository;


import com.nashtech.rookies.java05.AssetManagement.utils.EntityCheckUtils;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Builder
public class AssetServiceImpl implements AssetService {
    private static final int pageSize = 15;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private AssetMapper assetMapper;
    @Autowired
    private ReturningRepository returningRepository;
    @Autowired
    private EntityCheckUtils entityCheckUtils;

    @Override
    public AssetResponseInsertDto createAsset(AssetRequestDto dto){
        entityCheckUtils.assetCheckInsert(dto);
        Asset asset = assetMapper.mapAssetRequestDtoToEntityInsert(dto);
        Asset newAsset = assetRepository.save(asset);
        return assetMapper.mapEntityInsertToAssetResponseInsertDto(newAsset);
    }

    @Override
    public AssetResponseInsertDto updateAsset(AssetRequestDto dto, String id) {
        entityCheckUtils.assetCheckUpdate(dto);
        Optional<Asset> optional = assetRepository.findById(id);
        if(optional.isEmpty()){
            throw new ResourceNotFoundException("this asset not exist");
        }
        Asset modifiedAsset = assetMapper.mapAssetRequestDtoToEntityUpdate(dto, optional.get());
        assetRepository.save(modifiedAsset);
        return assetMapper.mapEntityInsertToAssetResponseInsertDto(modifiedAsset);
    }

    @Override
    public APIResponse<List<AssetViewResponseDto>> getAssetsByPredicates
            (List<String> stateFilterList, List<String> categoryNames, String keyword, int locationId, int page, String orderBy) {

        String[] parts = orderBy.split("_");
        String columnName = parts[0];
        String order = parts[1];

        Pageable pageable;
        if("DESC".equals(order)){
            pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC , columnName);
        }else{
            pageable = PageRequest.of(page, pageSize, Sort.Direction.ASC , columnName);
        }
        List<AssetState> stateList = new ArrayList<>();

        //State name list to state list
        AssetState[] assetStates = AssetState.values();
        for (AssetState assetState : assetStates) {
            if(stateFilterList.contains(assetState.getName())){
                stateList.add(assetState);
            }
        }

        //By default, filter by all categories, else filter by categories that user choose.
        List<Category> categories;
        if (categoryNames == null) {
            categories = categoryRepository.findAll();
        } else {
            categories = categoryRepository.findCategoriesByNameIsIn(categoryNames);
        }
        Page<Asset> result;
        result = assetRepository.findByKeywordWithFilter
                (categories, stateList, keyword.toLowerCase(), keyword.toLowerCase(), locationId, pageable);

        return new APIResponse<>(result.getTotalPages(), assetMapper.mapAssetListToAssetViewResponseDto(result.toList()));
    }

    @Override
    public Set<String> getAllAssetStates() {
        Set<String> result = EnumSet.allOf(AssetState.class)
                .stream()
                .map(assetState -> assetState.getName())
                .collect(Collectors.toSet());
        return result;
    }

    @Override
    public AssetResponseDto getAssetById(String id) {
        Optional<Asset> assetFound = assetRepository.findById(id);
        if(assetFound.isEmpty()){
            return AssetResponseDto.builder().build();
        }

        List<Returning> returningHistoryList = returningRepository.findByAssetIdAndState(id, AssignmentReturnState.COMPLETED);
        assetFound.get().setReturningList(returningHistoryList);
        return assetMapper.mapAssetEntityToAssetResponseDto(assetFound.get());
    }

    @Override
    public boolean deleteAssetById(String id) {
        Optional<Asset> optionalAsset=assetRepository.findById(id);
        if (optionalAsset.isEmpty())    return false;
        Asset asset=optionalAsset.get();
        List<Assignment> listAssignments= asset.getListAssignments();
        if (listAssignments==null || listAssignments.isEmpty()) {
            assetRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkAssetValidToDelete(String id) {
        Optional<Asset> optionalAsset=assetRepository.findById(id);
        if (optionalAsset.isEmpty())    return false;
        Asset asset=optionalAsset.get();
        List<Assignment> listAssignments= asset.getListAssignments();
        if (listAssignments==null || listAssignments.isEmpty()) {
            return true;
        }
        return false;
    }

}
