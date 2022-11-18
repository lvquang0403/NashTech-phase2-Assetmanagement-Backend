package com.nashtech.rookies.java05.AssetManagement.services.impl;

import com.nashtech.rookies.java05.AssetManagement.dtos.response.APIResponse;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetResponseDto;
import com.nashtech.rookies.java05.AssetManagement.dtos.response.AssetViewResponseDto;
import com.nashtech.rookies.java05.AssetManagement.entities.Asset;
import com.nashtech.rookies.java05.AssetManagement.entities.Category;
import com.nashtech.rookies.java05.AssetManagement.entities.Returning;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssetState;
import com.nashtech.rookies.java05.AssetManagement.entities.enums.AssignmentReturnState;
import com.nashtech.rookies.java05.AssetManagement.mappers.AssetMapper;
import com.nashtech.rookies.java05.AssetManagement.repository.AssetRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.CategoryRepository;
import com.nashtech.rookies.java05.AssetManagement.repository.ReturningRepository;
import com.nashtech.rookies.java05.AssetManagement.services.AssetService;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Builder
@Service
public class AssetServiceImpl implements AssetService {
    private static final int pageSize = 15;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private AssetMapper assetMapper;
    @Autowired
    private ReturningRepository returningRepository;

    @Override
    public APIResponse<List<AssetViewResponseDto>> getAssetsByPredicates
            (List<AssetState> states, List<String> categoryNames, String keyword, int locationId, int page) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "updatedWhen");

        //By default, filter by all categories, else filter by categories that user choose.
        List<Category> categories;
        if (categoryNames == null) {
            categories = categoryRepository.findAll();
        } else {
            categories = categoryRepository.findCategoriesByNameIsIn(categoryNames);
        }
        Page<Asset> result;
        result = assetRepository.findByKeywordWithFilter
                (categories, states, keyword.toLowerCase(), keyword.toLowerCase(), locationId, pageable);

        return new APIResponse<>(result.getTotalPages(), assetMapper.mapAssetListToAssetViewResponseDto(result.toList()));
    }

    @Override
    public List<String> getAllAssetStates() {
        List<String> result = EnumSet.allOf(AssetState.class)
                .stream()
                .map(assetState -> assetState.name())
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public AssetResponseDto getAssetById(String id) {
        Optional<Asset> assetFound = assetRepository.findById(id);
        if(assetFound.isEmpty()){
            return AssetResponseDto.builder().build();
        }

        List<Returning> returningHistoryList = returningRepository.findByAssetIdAndState(id, AssignmentReturnState.WAITING_FOR_RETURNING);
        assetFound.get().setReturningList(returningHistoryList);
        return assetMapper.mapAssetEntityToAssetResponseDto(assetFound.get());
    }


}
